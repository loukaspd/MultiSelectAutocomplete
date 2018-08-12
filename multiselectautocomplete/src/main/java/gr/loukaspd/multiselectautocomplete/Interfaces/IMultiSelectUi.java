package gr.loukaspd.multiselectautocomplete.Interfaces;

import android.view.View;

public interface IMultiSelectUi<T>  {

    /**
     * Extract the text from an item
     * @param item
     * @return
     */
    String getItemText(T item);

    /**
     * Layout file that will be used to inflate the autocomplete items
     * @return the layout file Resource Id
     */
    int autocompleteLayoutRes();

    /**
     * Update the view of the layout item
     * @param rootView the view inflated from {@link #autocompleteLayoutRes()}
     * @param item
     */
    void autocompleteUpdateView(View rootView, T item);


    /**
     * Layout file that will be used to inflate the selected items inside the input
     * @return the layout file Resource Id
     */
    int selectedItemLayoutRes();


    /**
     * Update the view of the layout item
     * @param rootView the view inflated from {@link #selectedItemLayoutRes()}
     * @param item
     */
    void selectedItemUpdateView(View rootView, T item);
}
