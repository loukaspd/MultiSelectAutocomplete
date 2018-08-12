package gr.loukaspd.autocompleteapp;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolderAutocomplete extends RecyclerView.ViewHolder {
    private TextView _textView;
    private ImageView _color;

    public ViewHolderAutocomplete(View itemView) {
        super(itemView);
        _textView = itemView.findViewById(R.id.tv);
        _color = itemView.findViewById(R.id.iv);
    }

    public void updateView(AutocompleteItem item) {
        _textView.setText(item.Text);
        AutocompleteItem myItem = (item instanceof AutocompleteItem ? (AutocompleteItem)item : null);
        if (myItem != null) {
            _color.getDrawable().setColorFilter(Color.parseColor(myItem.Color), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
