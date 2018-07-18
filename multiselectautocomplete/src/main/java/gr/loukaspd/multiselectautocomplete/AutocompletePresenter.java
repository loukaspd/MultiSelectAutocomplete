package gr.loukaspd.multiselectautocomplete;

import android.content.Context;
import android.support.annotation.Nullable;

import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.lang.reflect.Array;
import java.util.ArrayList;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectItem;
import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectUi;

class AutocompletePresenter<T extends IMultiSelectItem>
        extends RecyclerViewPresenter
        implements AdapterAutocomplete.OnItemClicked<T>
{
    private AdapterAutocomplete<T> _adapter;

    AutocompletePresenter(Context context, IMultiSelectUi<T> ui, ArrayList<T> items){
        super(context);
        _adapter = new AdapterAutocomplete<>(ui, items, this);
    }

    public void removeItem(T item) {
        _adapter.removeItem(item);
    }

    public void addItem(T item) {
        _adapter.addItem(item);
    }

    public void setItems(ArrayList<T> items) {
        _adapter.setItems(items);
    }



    @Override
    protected android.support.v7.widget.RecyclerView.Adapter instantiateAdapter() {
        return _adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        String text = null;
        if (query != null) text = query.toString();
        _adapter.setFilter(text);
    }

    @Override
    public void onClick(T item) {
        dispatchClick(item);
    }
}
