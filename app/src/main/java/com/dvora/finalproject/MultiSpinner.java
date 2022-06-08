package com.dvora.finalproject;

import android.app.AlertDialog;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.util.AttributeSet;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Spinner;

        import java.util.ArrayList;
        import java.util.List;

public class MultiSpinner extends androidx.appcompat.widget.AppCompatSpinner implements
        DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private ArrayList<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    public String spinnerText = "...";
    public String addText ="";
    public  AlertDialog.Builder builder;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (isChecked)
        {
            selected[which] = true;
            /*if(items.get(which).equals("הלכה"))
            {
                ArrayList<String> cloneItems = new ArrayList<>();
                for(int i = 0; i<which; i++)
                {
                    cloneItems.add(items.get(i));
                }
                cloneItems.add("    הלכה כללי");
                cloneItems.add("הלכה תת1");
                cloneItems.add("הלכה תת2");
                cloneItems.add("הלכה תת3");
                cloneItems.add("הלכה תת4");
                cloneItems.add("הלכה תת5");
                for (int i = which+1; i<items.size(); i++)
                {
                    cloneItems.add(items.get(i));
                }
                ArrayList<String> saveItems = new ArrayList<>(items);
                setItems(cloneItems, defaultText, listener);
                performClick();

            }*/
        }
        else
            selected[which] = false;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someSelected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i).trim());
                spinnerBuffer.append(", ");
                spinnerBuffer.append(addText);
                someSelected = true;
            }
        }
        if (someSelected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2); //spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        //dialog.dismiss();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(ArrayList<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        // all didn't selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = false;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }

    public String getSpinnerText() { return spinnerText; }

    public void setSelected(String list)
    {
        // must be after setItems
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
        {
            if(list.contains(items.get(i)))
                selected[i] = true;
            else
                selected[i] = false;
        }

        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someSelected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                String item = items.get(i).trim();
                spinnerBuffer.append(item);
                spinnerBuffer.append(", ");
                someSelected = true;
            }
        }
        if (someSelected) {
            spinnerText = spinnerBuffer.toString().trim();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 1); // and not -2
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }
}