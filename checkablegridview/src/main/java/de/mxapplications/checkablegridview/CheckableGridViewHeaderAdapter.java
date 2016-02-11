package de.mxapplications.checkablegridview;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian Dombrowski on 11/17/2015.
 */
class CheckableGridViewHeaderAdapter extends BaseAdapter {
    private List<String> mHeadings = new ArrayList<>();
    private OnColumnSelectedListener mColumnSelectedListener;

    public CheckableGridViewHeaderAdapter(OnColumnSelectedListener columnSelectedListener) {
        this.mColumnSelectedListener = mColumnSelectedListener;
    }

    @Override
    public int getCount() {
        return mHeadings.size();
    }

    @Override
    public Object getItem(int position) {
        return mHeadings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FrameLayout layout = new FrameLayout(parent.getContext());

        TextView headingTextView = new TextView(parent.getContext());
        headingTextView.setText(mHeadings.get(position));
        headingTextView.setSingleLine(false);
        headingTextView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(Helper.convertDpToPixels(96-4), ViewGroup.LayoutParams.WRAP_CONTENT);
        int margin = Helper.convertDpToPixels(2);
        layoutParams.setMargins(margin, 0, margin, 0);
        headingTextView.setLayoutParams(layoutParams);

        headingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mColumnSelectedListener !=null){
                    mColumnSelectedListener.onColumnSelected(position);
                }
            }
        });

        layout.addView(headingTextView);

        return layout;
    }

    public List<String> getHeadings() {
        return mHeadings;
    }

    public void setHeadings(List<String> headings) {
        this.mHeadings = headings;
    }

    public interface OnColumnSelectedListener{
        void onColumnSelected(int columnNumber);
    }
}
