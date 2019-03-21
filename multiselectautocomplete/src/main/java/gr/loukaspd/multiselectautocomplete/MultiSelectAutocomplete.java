package gr.loukaspd.multiselectautocomplete;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.MultiAutoCompleteTextView;

import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.AutocompletePolicy;
import com.otaliastudios.autocomplete.AutocompletePresenter.PopupDimensions;

import java.util.ArrayList;
import java.util.List;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectUi;

public class MultiSelectAutocomplete<T>
        extends MultiAutoCompleteTextView
        implements AutocompleteCallback
{

    //region Settings
    private boolean _supportMultiple = false;
    private boolean _showOptionsOnFocus = false;
    private boolean _clearUnmatchedText = false;
    private boolean _showKeyboardOnFocus = false;
    private float _popupElevation = 1;
    private Drawable _backgroundDrawable = null;
    private PopupDimensions _popupDimensions = new PopupDimensions();

    //Setters

    public void setSupportMultiple(boolean supportMultiple) {
        _supportMultiple = supportMultiple;
    }

    public void setShowOptionsOnFocus(boolean showOptionsOnFocus) {
        _showOptionsOnFocus = showOptionsOnFocus;
    }

    public void setClearUnmatchedText(boolean clearUnmatchedText) {
        _clearUnmatchedText = clearUnmatchedText;
    }

    public void setShowKeyboardOnFocus(boolean showKeyboardOnFocus) {
        _showKeyboardOnFocus = showKeyboardOnFocus;
    }

    public void setPopupElevation(float elevation) {
        _popupElevation = elevation;
    }

    public void setPopupBackground(Drawable drawable) {
        _backgroundDrawable = drawable;
    }

    public void setPopupWidth(int width) {
        _popupDimensions.width = width;
    }

    public void setPopupHeight(int height) {
        _popupDimensions.height = height;
    }
    //endregion

    //region Class Variables
    private final List<MultiSelectEditTextTagSpan<T>> _tagSpans = new ArrayList<>();
    private String _lastString = "";
    private boolean _isAfterTextWatcherEnabled = true;
    private CharSequence _query = "";
    private boolean _enabled = true;

    private Autocomplete _autocomplete;
    private IMultiSelectUi<T> _ui;
    private AutocompletePresenter<T> _presenter;
    private OnSelectedItemsChangedListener _onSelectedItemsChanged;
    //endregion

    //region Constructors

    public MultiSelectAutocomplete(Context context) {
        super(context);
        init(context, null);
    }

    public MultiSelectAutocomplete(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiSelectAutocomplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //endregion

    //region EditText Overrides

    //on selection move cursor to end of text
    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(this.length());
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (_showKeyboardOnFocus) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }else {
            // This will prevent keyboard from showing
            int inputType = this.getInputType();
            this.setInputType(InputType.TYPE_NULL);
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            this.setInputType(inputType);
        }


        // On Focus
        if (focused) {
            if (_enabled && _showOptionsOnFocus && _autocomplete != null) {
                _autocomplete.showPopup(_query);
            }
        }else {
            if (_clearUnmatchedText) clearUnmatchedText();
            _autocomplete.dismissPopup();
        }
    }



    //endregion

    // region Public API

    /**
     * Initialize the View
     * @param ui customize how items will be drawn
     * @param items the items that will be available for selection
     */
    public void initialize(IMultiSelectUi<T> ui, ArrayList<T> items) {
        _ui = ui;

        initializeAutocomplete(items);
    }

    /**
     *
     * @return a list with all selected items
     */
    public ArrayList<T> getSelectedItems() {
        ArrayList<T> items = new ArrayList<>(_tagSpans.size());
        for(MultiSelectEditTextTagSpan<T> span : _tagSpans) {
            items.add(span.getItem());
        }

        return items;
    }

    /**
     *
     * @return the first selected item or null
     */
    public T getSelectedItem() {
        if (_tagSpans.size() < 1) return null;
        return _tagSpans.get(0).getItem();
    }


    /**
     * Add item as Selected. This will replace the current selected Item if it's not multiple
     * @param item the item to add as selected
     */
    public void addSelectedItem(T item) {
        if (!_supportMultiple) {
            _tagSpans.clear();
        }
        addItem(item);
    }


    /**
     * Remove the specified item if it's selected
     * @param item the item to remove if it's selected
     * @return if the item was found
     */
    public boolean removeSelectedItem(T item) {
        for(int i=0; i<_tagSpans.size(); i++) {
            MultiSelectEditTextTagSpan<T> span = _tagSpans.get(i);
            if (span.getItem() == item) {
                _tagSpans.remove(i);
                updateText();
                toggleEnabled(true);
                return true;
            }
        }

        return false;
    }

    /**
     * Remove all (selected) tags from EditText
     */
    public void clear() {
        _tagSpans.clear();
        updateText();
        toggleEnabled(true);
    }


    /**
     * Set the items that will be available for selection. This will clear currently selected items.
     * @param items which will be available for selection
     */
    public void setAutocompleteItems(ArrayList<T> items) {
        clear();
        _presenter.setItems(items);
    }


    public void setOnSelectedItemsChangedListener(OnSelectedItemsChangedListener listener) {
        _onSelectedItemsChanged = listener;
    }

    //endregion

    //region Private Implementation

    private void initializeAutocomplete(ArrayList<T> items) {
        _presenter = new AutocompletePresenter<>(this.getContext(), _ui, items);
        _presenter.setPopupDimensions(_popupDimensions);

        if (_backgroundDrawable == null) _backgroundDrawable = new ColorDrawable(Color.WHITE);

        _autocomplete = Autocomplete.<T>on(this)
                .with(_popupElevation)
                .with(_backgroundDrawable)
                .with(new AutocompletePolicy() {
                    @Override
                    public boolean shouldShowPopup(Spannable text, int cursorPos) {
                        return (_showOptionsOnFocus && _enabled) ? true : getQuery(text).length() > 0;
                    }

                    @Override
                    public boolean shouldDismissPopup(Spannable text, int cursorPos) {
                        return !shouldShowPopup(text, cursorPos);
                    }

                    @Override
                    public CharSequence getQuery(Spannable text) {
                        MultiSelectEditTextTagSpan[] spans = text.getSpans(0,text.length(),MultiSelectEditTextTagSpan.class);
                        _query = text.toString();
                        for(MultiSelectEditTextTagSpan span : spans) {
                            int charsToRemove = span.getSource().length() + 1;
                            if (charsToRemove > _query.length()) {
                                _query = "";
                            }
                            else {
                                _query = _query.subSequence(charsToRemove, _query.length());
                            }
                        }
                        return _query;
                    }

                    @Override
                    public void onDismiss(Spannable text) {

                    }
                })
                .with(_presenter)
                .with(this)
                .build();

        _autocomplete.setGravity(Gravity.START);
    }

    void addItem(T item) {
        // Create the Ui
        View view = getView(item);
        Drawable bd = convertViewToDrawable(view);
        bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

        // Initialize the span
        final MultiSelectEditTextTagSpan span = new MultiSelectEditTextTagSpan(bd, _ui.getItemText(item));
        span.setItem(item);
        // Set span position - index
        int spansSize = _tagSpans.size();


        if (spansSize == 0) {
            span.setIndex(0);
            span.setPosition(0);
        }else {
            span.setIndex(spansSize);

            MultiSelectEditTextTagSpan lastSpan = _tagSpans.get(_tagSpans.size()-1);
            span.setPosition(lastSpan.getPosition() + lastSpan.getLength() + 1);
        }
        _tagSpans.add(span);

        updateText();

        if (_onSelectedItemsChanged != null) {
            _onSelectedItemsChanged.onSelectedItemsChanged();
        }
    }

    private void updateText() {
        _isAfterTextWatcherEnabled = false;     //disable TextWatcher

        SpannableStringBuilder sb = new SpannableStringBuilder();
        for (final MultiSelectEditTextTagSpan tagSpan : _tagSpans) {
            addTagSpan(sb, tagSpan);
        }

        getText().clear();
        getText().append(sb);
        setMovementMethod(LinkMovementMethod.getInstance());
        setSelection(sb.length());

        _lastString = sb.toString();
        _isAfterTextWatcherEnabled = true;
    }

    private void toggleEnabled(boolean enabled) {
        if (_supportMultiple) return;

        _enabled = enabled;

        if (!enabled) {
            Helpers.releaseFocus(this);
            this.clearFocus();
            this.setFocusableInTouchMode(false);
            this.setFocusable(false);
        }else {
            setFocusableInTouchMode(true);
            Helpers.requestFocus(this);
        }
    }

    private void clearUnmatchedText() {
        updateText();
    }

    //endregion

    //region AutocompleteCallback

    @Override
    public boolean onPopupItemClicked(Editable editable, Object item) {
        addItem((T)item);
        if (_supportMultiple && _showOptionsOnFocus) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    _autocomplete.showPopup("");
                }
            });
        }
        return true;
    }

    @Override
    public void onPopupVisibilityChanged(boolean shown) {

    }

    //endregion

    //region Add-Remove item
    private void addTagSpan(SpannableStringBuilder sb, final MultiSelectEditTextTagSpan<T> tagSpan) {
        String source = tagSpan.getSource();
        sb.append(source).append(" ");
        int length = sb.length();
        int startSpan = length - source.length() -1;
        int endSpan = length - 1;
        sb.setSpan(tagSpan, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                removeTagSpan(tagSpan, true);
            }
        }, startSpan, endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        _presenter.removeItem(tagSpan.getItem());    // stop showing as autocomplete option
        toggleEnabled(false);                       // can't add more items (if not multiple)
    }

    private void removeTagSpan(MultiSelectEditTextTagSpan<T> span, boolean includeSpace) {
        _isAfterTextWatcherEnabled = false;     //disable TextWatcher

        int extraLength = includeSpace ? 1 : 0;
        // include space
        int tagPosition = span.getPosition();
        int tagIndex = span.getIndex();
        int tagLength = span.getSource().length() + extraLength;
        getText().replace(tagPosition, tagPosition + tagLength, "");
        int size = _tagSpans.size();
        for (int i = tagIndex + 1; i < size; i++) {
            MultiSelectEditTextTagSpan tagSpan = _tagSpans.get(i);
            tagSpan.setIndex(i - 1);
            tagSpan.setPosition(tagSpan.getPosition() - tagLength);
        }
        _tagSpans.remove(tagIndex);

        this.post(new Runnable() {
            @Override
            public void run() {
                setSelection(length());
            }
        });


        _isAfterTextWatcherEnabled = true;

        _presenter.addItem(span.getItem());         //show item in as autocomplete option
        toggleEnabled(true);                       // can add more items (if not multiple)

        if (_onSelectedItemsChanged != null) {
            _onSelectedItemsChanged.onSelectedItemsChanged();
        }
    }

    //endregion

    //region Ui

    private View getView(T item) {
        View view = Helpers.inflateView(getContext(), _ui.selectedItemLayoutRes());
        if (view == null) return null;

        _ui.selectedItemUpdateView(view, item);

        return view;
    }

    private Drawable convertViewToDrawable(View view) {
        int spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(c);
        view.setDrawingCacheEnabled(true);
        Bitmap cacheBmp = view.getDrawingCache();
        Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        view.destroyDrawingCache();
        return new BitmapDrawable(getResources(), viewBmp);
    }

    //endregion

    //region TextWatcher for Deletion

    private void init(Context context, AttributeSet attrs) {
        // Read Settings
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MultiSelectAutocomplete,
                0, 0);

        float popupWidth ,popupHeight;

        try {
            _supportMultiple = a.getBoolean(R.styleable.MultiSelectAutocomplete_supportMultiple, false);
            _showOptionsOnFocus = a.getBoolean(R.styleable.MultiSelectAutocomplete_showOptionsOnFocus, false);
            _clearUnmatchedText = a.getBoolean(R.styleable.MultiSelectAutocomplete_clearUnmatchedText, false);
            _showKeyboardOnFocus = a.getBoolean(R.styleable.MultiSelectAutocomplete_showKeyboardOnFocus, false);
            _popupElevation = a.getFloat(R.styleable.MultiSelectAutocomplete_popupElevation, 1);
            popupWidth = a.getDimension(R.styleable.MultiSelectAutocomplete_popupWidth, Float.MAX_VALUE);
            popupHeight = a.getDimension(R.styleable.MultiSelectAutocomplete_popupHeight, Float.MAX_VALUE);
        } finally {
            a.recycle();
        }

        // Apply popup dimensions
        if (popupHeight != Float.MAX_VALUE) {
            _popupDimensions.height = (int) popupHeight;
        }
        if (popupWidth != Float.MAX_VALUE) {
            _popupDimensions.width = (int) popupWidth;
        }

        // register textWatcher
        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    addTextChangedListener(mTextWatcher);
                    mTextWatcher.afterTextChanged(getText());
                }
            });
        }
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable str) {
            if (!_isAfterTextWatcherEnabled) return;

            boolean isDeleting = _lastString.length() > str.length();
            if (_lastString.endsWith(" ")
                    && !str.toString().endsWith("\n")
                    && isDeleting
                    && !_tagSpans.isEmpty()) {
                MultiSelectEditTextTagSpan toRemoveSpan = _tagSpans.get(_tagSpans.size() - 1);
                if (toRemoveSpan.getPosition() + toRemoveSpan.getSource().length() == str.length()) {
                    removeTagSpan(toRemoveSpan, false);
                }
            }

            _lastString = str.toString();
        }
    };

    //endregion

    //region OnSelectedItemsChangedListener

    public interface OnSelectedItemsChangedListener {
        void onSelectedItemsChanged();
    }

    //endregion
}
