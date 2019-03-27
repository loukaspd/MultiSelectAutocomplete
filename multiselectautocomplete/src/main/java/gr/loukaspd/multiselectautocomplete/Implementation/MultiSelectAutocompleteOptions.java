package gr.loukaspd.multiselectautocomplete.Implementation;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import com.otaliastudios.autocomplete.AutocompletePresenter.PopupDimensions;

import gr.loukaspd.multiselectautocomplete.R;

public class MultiSelectAutocompleteOptions {
    public boolean SupportMultiple = false;
    public boolean ShowOptionsOnFocus = false;
    public boolean ClearUnmatchedText = false;
    public boolean ShowKeyboardOnFocus = false;
    public float PopupElevation = 1;
    public Drawable BackgroundDrawable = null;
    public PopupDimensions PopupDimensions = new PopupDimensions();


    public void readOptions(TypedArray a) {
        SupportMultiple = a.getBoolean(R.styleable.MultiSelectAutocomplete_supportMultiple, false);
        ShowOptionsOnFocus = a.getBoolean(R.styleable.MultiSelectAutocomplete_showOptionsOnFocus, false);
        ClearUnmatchedText = a.getBoolean(R.styleable.MultiSelectAutocomplete_clearUnmatchedText, false);
        ShowKeyboardOnFocus = a.getBoolean(R.styleable.MultiSelectAutocomplete_showKeyboardOnFocus, false);
        PopupElevation = a.getFloat(R.styleable.MultiSelectAutocomplete_popupElevation, 1);
        float popupWidth = a.getDimension(R.styleable.MultiSelectAutocomplete_popupWidth, Float.MAX_VALUE);
        float popupHeight = a.getDimension(R.styleable.MultiSelectAutocomplete_popupHeight, Float.MAX_VALUE);

        if (popupHeight != Float.MAX_VALUE) {
            PopupDimensions.height = (int) popupHeight;
        }
        if (popupWidth != Float.MAX_VALUE) {
            PopupDimensions.width = (int) popupWidth;
        }
    }
}