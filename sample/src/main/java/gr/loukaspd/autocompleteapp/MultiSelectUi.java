package gr.loukaspd.autocompleteapp;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectUi;

public class MultiSelectUi implements IMultiSelectUi<AutocompleteItem> {

    @Override
    public String getItemText(AutocompleteItem item) {
        return item.Text;
    }

    @Override
    public int autocompleteLayoutRes() {
        return R.layout.item_autocomplete;
    }

    @Override
    public void autocompleteUpdateView(View rootView, AutocompleteItem item) {
        updateView(rootView, item);
    }

    @Override
    public int selectedItemLayoutRes() {
        return R.layout.item_chip;
    }

    @Override
    public void selectedItemUpdateView(View rootView, AutocompleteItem item) {
        updateView(rootView, item);
    }


    private void updateView(View rootView, AutocompleteItem item) {
        TextView textView = rootView.findViewById(R.id.tv);
        ImageView color = rootView.findViewById(R.id.iv);

        textView.setText(item.Text);
        color.getDrawable().setColorFilter(Color.parseColor(item.Color), PorterDuff.Mode.SRC_ATOP);
    }
}
