package gr.loukaspd.multiselectautocomplete;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

class Helpers {

    public static final String TAG = "MultiselectAutocomplete";

    public static View inflateView(Context context, int layoutRes) {
        try {
            return LayoutInflater.from(context).inflate(layoutRes, null);
        }catch (Exception e) {
            Log.e(Helpers.TAG, "Could not inflate the specified Layout Resource File");
        }
        return null;
    }


    public static void releaseFocus(EditText view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void requestFocus(EditText editText) {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static <T> ArrayList<T> cloneList(ArrayList<T> list) {
        ArrayList<T> result = new ArrayList<>();
        result.addAll(list);

        return result;
    }
}
