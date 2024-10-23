package edu.nus.adproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.ArrayAdapter;

import java.util.List;

import edu.nus.adproject.R;
import edu.nus.adproject.models.Label;

public class LabelAdapter extends ArrayAdapter<Label>{
    private Context context;
    private List<Label> labels;
    private int selectedPosition = -1; // No selection by default

    public LabelAdapter(Context context, List<Label> labels) {
        super(context, 0, labels);
        this.context = context;
        this.labels = labels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_label, parent, false);
        }

        Label label = labels.get(position);

        RadioButton radioButton = convertView.findViewById(R.id.radioButton);
        radioButton.setText(label.getLabel());
        radioButton.setChecked(position == selectedPosition);

        radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged(); // Update the ListView to reflect the change
        });

        return convertView;
    }

    public int getSelectedLabelId() {
        if (selectedPosition != -1) {
            return labels.get(selectedPosition).getId();
        }
        return -1; // No selection
    }



}
