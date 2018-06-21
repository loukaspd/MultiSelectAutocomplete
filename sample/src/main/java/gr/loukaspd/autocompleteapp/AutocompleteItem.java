package gr.loukaspd.autocompleteapp;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectItem;

public class AutocompleteItem implements IMultiSelectItem {
    public String Text;
    public String Color;

    public AutocompleteItem(String text, String color) {
        Text = text;
        Color = color;
    }

    @Override
    public String getText() {
        return Text;
    }
}
