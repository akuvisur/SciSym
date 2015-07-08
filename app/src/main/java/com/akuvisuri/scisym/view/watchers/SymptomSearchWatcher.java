package com.akuvisuri.scisym.view.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.akuvisuri.scisym.view.adapters.SymptomListAdapter;

/**
 * Created by Aku on 8.7.2015.
 */
public class SymptomSearchWatcher implements TextWatcher {

    ListView searchable;
    ArrayAdapter<SymptomListAdapter.SymptomListItem> adapter;

    public SymptomSearchWatcher(ListView v) {
        searchable = v;
        adapter = (ArrayAdapter<SymptomListAdapter.SymptomListItem>) v.getAdapter();
    }

    public void onTextChanged(CharSequence c, int start, int before, int count) {
        if (count > 3) {
            for (int i = 0; i < adapter.getCount();i++) {
                adapter.getItem(i).setVisible(
                    adapter.getItem(i).label.toLowerCase()
                    .contains(c.toString().toLowerCase())
                );
            }
            adapter.notifyDataSetChanged();
        }
        else {
            for (int i = 0; i < adapter.getCount(); i++) {
                adapter.getItem(i).setVisible(true);
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void afterTextChanged(Editable s) {};
    public void beforeTextChanged(CharSequence c, int start, int before, int count) {};
}
