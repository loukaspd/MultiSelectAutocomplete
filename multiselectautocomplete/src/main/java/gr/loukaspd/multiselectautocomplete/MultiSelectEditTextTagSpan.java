package gr.loukaspd.multiselectautocomplete;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import gr.loukaspd.multiselectautocomplete.Interfaces.IMultiSelectItem;

class MultiSelectEditTextTagSpan<T extends IMultiSelectItem>
        extends ImageSpan
{
    public MultiSelectEditTextTagSpan(Drawable d, String source) {
        super(d, source);
    }

    /**
     * index inside the array
     */
    private int index;

    /**
     * position inside the StringBuilder
     */
    private int position;

    /**
     * The Item it represents
     */
    private T item;

    /**
     * @return the text of the item inside the Span
     */
    public String getItemText() {
        return item == null ? null : item.getText();
    }


    //region Getters - Setters

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    /**
     * {@link MultiSelectEditTextTagSpan#position}
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    //endregion

}
