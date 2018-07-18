# MultiSelectAutocomplete

EditText with Autocomplete Functionality based on [Autocomplete library](https://github.com/natario1/Autocomplete) that supports customizing how the items will be drawn and Multiple selected items

```
    implementation 'gr.loukaspd:multiselectautocomplete:1.0.2'
    implementation 'com.otaliastudios:autocomplete:1.1.0'
    implementation 'com.android.support:recyclerview-v7:27.1.1'
```

To see it in action, take a look at the sample app in the `sample` module.

## Dependencies
This library uses the [Autocomplete library](https://github.com/natario1/Autocomplete).

## Usage
1. include `<gr.loukaspd.multiselectautocomplete.MultiSelectAutocomplete>` element in your layout file instead on EditText
2. Stup the Autocomplete Element by calling `input.initialize(IMultiSelectUi, ArrayList<IMultiSelectItem>, supportMultiple)`


### IMultiSelectItem

The class of the items that will populate the MultiSelectAutocomplete must implement this Interface, which exposes the `String getText();` method

### IMultiSelectUi

This is an interface that controls how to draw the items inside the AutocompleteDropdown and the selected items inside the EditText

```java
public interface IMultiSelectUi<T extends IMultiSelectItem>  {

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
```

- `autocompleteLayoutRes()`: returns the resource id of the layout file that will be used to draw the items inside the autocomplete dialog
- `autocompleteUpdateView(View rootView, T item)`: update the view with data from the specified item inside the autocomplete dialog
- `selectedItemLayoutRes()`: returns the resource id of the layout file that will be used to draw the selected items inside the EditText
- `selectedItemUpdateView(View rootView, T item)`: update the view with data from the specified item inside the EditText

