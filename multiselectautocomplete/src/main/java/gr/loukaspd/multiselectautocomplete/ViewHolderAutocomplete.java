package gr.loukaspd.multiselectautocomplete;

import android.support.v7.widget.RecyclerView;
import android.view.View;

class ViewHolderAutocomplete<T>
        extends RecyclerView.ViewHolder
{
    private final View _rootView;
    T Item;

    public ViewHolderAutocomplete(View itemView) {
        super(itemView);
        _rootView = itemView;
    }

    public View getView() {
        return _rootView;
    }
}
