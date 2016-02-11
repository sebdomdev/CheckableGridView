package de.mxapplications.checkablegridview;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sebastian Dombrowski on 11/17/2015.
 */
class CheckableGridViewFirstColumnAdapter extends BaseAdapter {
    private List<String> mFirstColumnValues =new ArrayList<>();
    private OnRowSelectedListener mRowSelectedListener;

    public CheckableGridViewFirstColumnAdapter(OnRowSelectedListener rowSelectedListener) {
        this.mRowSelectedListener = rowSelectedListener;
    }

    @Override
    public int getCount() {
        return mFirstColumnValues.size();
    }

    @Override
    public Object getItem(int position) {
        return mFirstColumnValues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FrameLayout layout = new FrameLayout(parent.getContext());

        TextView valueTextView = new TextView(parent.getContext());
        valueTextView.setText(mFirstColumnValues.get(position));
        valueTextView.setSingleLine(false);
        valueTextView.setGravity(Gravity.CENTER_VERTICAL);

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(Helper.convertDpToPixels(100), Helper.convertDpToPixels(48));
        valueTextView.setLayoutParams(layoutParams);

        valueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRowSelectedListener !=null){
                    mRowSelectedListener.onRowSelected(position);
                }
            }
        });

        layout.addView(valueTextView);
        return layout;
    }

    public void setFirstColumnValues(List<String> firstColumnValues) {
        this.mFirstColumnValues = firstColumnValues;
    }

    public interface OnRowSelectedListener{
        void onRowSelected(int rowNumber);
    }
}
