package gr.loukaspd.autocompleteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectItem;
import gr.loukaspd.multiselectautocomplete.MultiSelectAutocomplete;

public class MainActivity extends AppCompatActivity {

    MultiSelectAutocomplete input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (MultiSelectAutocomplete) findViewById(R.id.input);

        input.initialize(new MultiSelectUi(), getItems(), true);

        findViewById(R.id.replace).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setAutocompleteItems(getWrongItems());
            }
        });

        findViewById(R.id.set_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.addSelectedItem(new AutocompleteItem("Red", "#FF0000"));
            }
        });

        findViewById(R.id.show_selected).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectedItemsDesc();
            }
        });

        input.setOnSelectedItemsChangedListener(new MultiSelectAutocomplete.OnSelectedItemsChangedListener() {
            @Override
            public void onSelectedItemsChanged() {
                showSelectedItemsDesc();
            }
        });
    }


    private void showSelectedItemsDesc() {
        int items = input.getSelectedItems().size();
        String text = String.format(Locale.ENGLISH, "found %d items%ntext:%s", items, input.getText().toString());
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }


    private ArrayList<IMultiSelectItem> getItems() {
        ArrayList<IMultiSelectItem> items = new ArrayList<>();
        items.add(new AutocompleteItem("Red", "#FF0000"));
        items.add(new AutocompleteItem("Green", "#00FF00"));
        items.add(new AutocompleteItem("Blue", "#0000FF"));
        items.add(new AutocompleteItem("Black", "#000000"));
        items.add(new AutocompleteItem("White", "#FFFFFF"));

        return items;
    }


    private ArrayList<IMultiSelectItem> getWrongItems() {
        ArrayList<IMultiSelectItem> items = new ArrayList<>();
        items.add(new AutocompleteItem("Green", "#FF0000"));
        items.add(new AutocompleteItem("Red", "#00FF00"));
        items.add(new AutocompleteItem("Black", "#0000FF"));
        items.add(new AutocompleteItem("White", "#000000"));
        items.add(new AutocompleteItem("Blue", "#FFFFFF"));

        return items;
    }
}
