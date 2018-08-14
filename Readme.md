# MultiSelectAutocomplete

EditText with Autocomplete Functionality based on [Autocomplete library](https://github.com/natario1/Autocomplete) that supports customizing how the items will be drawn and Multiple selected items

```
    implementation 'gr.loukaspd:multiselectautocomplete:1.2.4'
    implementation 'com.otaliastudios:autocomplete:1.1.0'
    implementation 'com.android.support:recyclerview-v7:27.1.1'
```

To see it in action, take a look at the sample app in the `sample` module.

## Dependencies
This library uses the [Autocomplete library](https://github.com/natario1/Autocomplete).

## Usage
1. include `<gr.loukaspd.multiselectautocomplete.MultiSelectAutocomplete>` element in your layout file instead on EditText
2. Stup the Autocomplete Element by calling `input.initialize(IMultiSelectUi, ArrayList<T>)`

### IMultiSelectUi

This is an interface that controls how to draw the items inside the AutocompleteDropdown and the selected items inside the EditText

```java
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
```

- `getItemText()`: returns the text representation from an item, used for filtering autocomplete items
- `autocompleteLayoutRes()`: returns the resource id of the layout file that will be used to draw the items inside the autocomplete dialog
- `autocompleteUpdateView(View rootView, T item)`: update the view with data from the specified item inside the autocomplete dialog
- `selectedItemLayoutRes()`: returns the resource id of the layout file that will be used to draw the selected items inside the EditText
- `selectedItemUpdateView(View rootView, T item)`: update the view with data from the specified item inside the EditText


### XML Attributes

You can change view attributes directly from your layout's xml file or in your java/kotlin code:

| Variable                   | XML Attribute              | Type      | Description                                                                            |
| :------------------------- | :------------------------- | :-------- | :--------------------------------------------------------------------------------------|
| supportMultiple                 | supportMultiple             | boolean     | if EditText can have multiple selected items |
| showOptionsOnFocus         | showOptionsOnFocus     | boolean     | show the popup with autocomplete options when EditText gets focus                                        |
| clearUnmatchedText         | clearUnmatchedText     | boolean     | clear all text that is not matched when EditText loses focus                                        |
| popupElevation         | popupElevation     | float     | sets the elevtion of the autocomplete popup                                        |
| popupWidth         | popupWidth     | dimension     | set the width of the autocomplete popup window                                        |
| popupHeight        | popupHeight     | dimension     | set the height of the autocomplete popup window                                        |


### Programmatic Attributes

These must be set before calling the initialize method

- `setPopupBackground(Drawable drawable)`: set the background of the autocomplete popup window