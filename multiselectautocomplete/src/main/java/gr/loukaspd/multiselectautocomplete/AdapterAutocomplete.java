package gr.loukaspd.multiselectautocomplete;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectItem;
import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectUi;

class AdapterAutocomplete<T extends IMultiSelectItem>
        extends android.support.v7.widget.RecyclerView.Adapter
{
    //region Properties

    private final OnItemClicked<T> _onItemClicked;
    private ArrayList<T> _items;
    private ArrayList<T> _currentItems;
    private String _filter;

    private final IMultiSelectUi<T> _ui;

    //endregion

    //region Public API

    public AdapterAutocomplete(IMultiSelectUi<T> ui, ArrayList<T> items, OnItemClicked<T> onItemClicked) {
        _ui = ui;
        _onItemClicked = onItemClicked;
        _items = Helpers.cloneList(items);

        applyFiltering(null);
    }

    public void setFilter(String text) {
        applyFiltering(text);
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        if (!_items.remove(item)) return;
        applyFiltering(_filter);
    }

    public void addItem(T item) {
        if (_items.contains(item)) return;
        _items.add(item);
        applyFiltering(_filter);
    }

    public void setItems(ArrayList<T> items) {
        _items = Helpers.cloneList(items);
        applyFiltering(_filter);
    }

    //endregion

    //region Adapter Methods

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate View
        View view = LayoutInflater.from(parent.getContext()).inflate(_ui.autocompleteLayoutRes(), null);
        // Create ViewHolder
        final ViewHolderAutocomplete<T> viewHolder = new ViewHolderAutocomplete<>(view);

        // handle Click
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _onItemClicked.onClick(viewHolder.Item);
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        T item = _currentItems.get(position);
        @SuppressWarnings("unchecked")
        ViewHolderAutocomplete<T> viewHolder = (ViewHolderAutocomplete<T>)holder;

        // Set Data
        viewHolder.Item = item;
        // Update View
        _ui.autocompleteUpdateView(viewHolder.getView(), item);
    }

    @Override
    public int getItemCount() {
        return _currentItems.size();
    }

    //endregion

    //region Implementation

    private void applyFiltering(String filter) {
        _currentItems = new ArrayList<>();
        for(T item : _items) {
            if (filter == null || item.getText().toLowerCase().contains(filter.toLowerCase())) {
                _currentItems.add(item);
            }
        }
        // keep filter
        _filter = filter;
    }

    //endregion

    interface OnItemClicked<T extends IMultiSelectItem> {
        void onClick(T item);
    }
}
