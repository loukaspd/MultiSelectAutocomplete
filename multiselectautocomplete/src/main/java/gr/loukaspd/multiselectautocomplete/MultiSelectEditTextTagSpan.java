package gr.loukaspd.multiselectautocomplete;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

class MultiSelectEditTextTagSpan<T>
        extends ImageSpan
{
    public MultiSelectEditTextTagSpan(Drawable d, String source) {
        super(d, source);
        this.length = source.length();
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
     * the length of the string
     */
    private int length;

    /**
     * The Item it represents
     */
    private T item;


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

    public int getLength() { return length; }


    //endregion

}
