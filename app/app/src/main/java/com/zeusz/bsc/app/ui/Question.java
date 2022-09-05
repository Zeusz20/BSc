package com.zeusz.bsc.app.ui;

import android.app.Activity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zeusz.bsc.app.R;
import com.zeusz.bsc.app.adapter.ValueListAdapter;
import com.zeusz.bsc.core.Attribute;


public final class Question {

    public static String render(Activity ctx, @Nullable View view, Attribute attribute) {
        if(view == null)
            view = ctx.getWindow().getDecorView().findViewById(android.R.id.content);

        String[] text = attribute.getQuestion().split("\\{\\$attr\\}");

        TextView name = view.findViewById(R.id.selected_attribute_name);
        TextView question1 = view.findViewById(R.id.question_part_1);
        TextView question2 = view.findViewById(R.id.question_part_2);
        Spinner spinner = view.findViewById(R.id.attribute_spinner);

        name.setText(attribute.getName());
        spinner.setAdapter(new ValueListAdapter(ctx, R.layout.value_item, attribute.getValues()));
        question1.setText(text[0]);

        // the substitute pattern may be at the end
        if(text.length > 1)
            question2.setText(text[1]);

        return question1.getText().toString() + spinner.getSelectedItem().toString() + question2.getText().toString();
    }

}
