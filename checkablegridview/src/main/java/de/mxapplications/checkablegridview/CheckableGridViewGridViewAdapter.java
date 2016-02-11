package de.mxapplications.checkablegridview;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * Created by Sebastian Dombrowski on 11/17/2015.
 */
class CheckableGridViewGridViewAdapter extends BaseAdapter {
    private Drawable mCheckedIcon;
    private Drawable mUnCheckedIcon;
    private boolean[][] mCheckedArray =new boolean[][]{};
    private Set<Integer> mSingleSelectionColumns;
    private Set<Integer> mSingleSelectionRows;
    private List<List<Boolean>> mDisabledCellsArray =new ArrayList<>();
    private CheckableGridView.OnCheckChangedListener mOnCheckChangedListener;

    @Override
    public int getCount() {
        return mCheckedArray.length;
    }

    @Override
    public Object getItem(int position) {
        return mCheckedArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout linearLayout = new LinearLayout(parent.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        for(int i=0; i< mCheckedArray[position].length; i++){
            RelativeLayout innerContainer = new RelativeLayout(parent.getContext());
            final ImageView checkBox = new ImageView(parent.getContext());
            final int col = i;

            if(mCheckedIcon ==null|| mUnCheckedIcon ==null) {
                initiateIcons(parent.getContext());
            }
            if(!(position< mDisabledCellsArray.size()&&i< mDisabledCellsArray.get(position).size()&& mDisabledCellsArray.get(position).get(i))) {
                checkBox.setImageDrawable(mCheckedArray[position][i] ? mCheckedIcon : mUnCheckedIcon);
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = !mCheckedArray[position][col];
                    mCheckedArray[position][col] = checked;
                    if (mSingleSelectionColumns.size() > 0) {
                        if(mSingleSelectionColumns.contains(col)){
                            for(int i=0;i< mCheckedArray.length;i++){
                                if(i!=position) {
                                    mCheckedArray[i][col]=false;
                                }
                            }
                        }
                    }
                    if (mSingleSelectionRows.size() > 0) {
                        if(mSingleSelectionRows.contains(position)){
                            for(int i=0;i< mCheckedArray[position].length;i++){
                                if(i!=col) {
                                    mCheckedArray[position][i]=false;
                                }
                            }
                        }
                    }

                    notifyDataSetChanged();

                    mOnCheckChangedListener.onChecked(position, col, checked);
                }
            });

            LinearLayout.LayoutParams outerLayoutParams = new LinearLayout.LayoutParams(Helper.convertDpToPixels(96), Helper.convertDpToPixels(48));
            innerContainer.setLayoutParams(outerLayoutParams);

            RelativeLayout.LayoutParams innerLayoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            innerLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            checkBox.setLayoutParams(innerLayoutParams);

            innerContainer.addView(checkBox);
            linearLayout.addView(innerContainer);
        }

        return linearLayout;
    }

    public boolean[][] getCheckedArray() {
        return mCheckedArray;
    }

    public void setCheckedArray(boolean[][] checkedArray) {
        boolean[][] oldArray = new boolean[this.mCheckedArray.length][];
        for(int i=0;i<this.mCheckedArray.length;i++){
            oldArray[i]=Arrays.copyOf(this.mCheckedArray[i], this.mCheckedArray[i].length);
        }

        this.mCheckedArray = checkedArray;
        notifyDataSetChanged();

        for(int i=0;i<oldArray.length&&i< checkedArray.length;i++){
            for(int j=0;j<oldArray[i].length&&j< checkedArray[i].length;j++){
                if(oldArray[i][j]!= checkedArray[i][j]){
                    mOnCheckChangedListener.onChecked(i, j, checkedArray[i][j]);
                }
            }
        }
    }
    public void setCheckedCell(int row, int column, boolean selected){
        if(row< mCheckedArray.length&&column< mCheckedArray[row].length) {
            mCheckedArray[row][column]=selected;
        }
        notifyDataSetChanged();
        mOnCheckChangedListener.onChecked(row, column, selected);

    }
    public void setIcons(Drawable checkedIcon, Drawable unCheckedIcon){
        this.mCheckedIcon = checkedIcon;
        this.mUnCheckedIcon = unCheckedIcon;
        notifyDataSetChanged();
    }
    private void initiateIcons(Context context){
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int primaryColor = typedValue.data;

        mCheckedIcon = ContextCompat.getDrawable(context, R.drawable.ic_done_black_48dp);
        mCheckedIcon.mutate();
        mCheckedIcon.setColorFilter(primaryColor, PorterDuff.Mode.SRC_ATOP);

        mUnCheckedIcon = ContextCompat.getDrawable(context, R.drawable.ic_done_black_48dp);
        mUnCheckedIcon.mutate();
        mUnCheckedIcon.setAlpha(138);
    }

    public Set<Integer> getSingleSelectionColumns() {
        return mSingleSelectionColumns;
    }

    public void setSingleSelectionColumns(Set<Integer> singleSelectionColumns) {
        this.mSingleSelectionColumns = singleSelectionColumns;
    }

    public Set<Integer> getSingleSelectionRows() {
        return mSingleSelectionRows;
    }

    public void setSingleSelectionRows(Set<Integer> singleSelectionRows) {
        this.mSingleSelectionRows = singleSelectionRows;
    }

    public List<List<Boolean>> getDisabledCellsArray() {
        return mDisabledCellsArray;
    }

    public void setDisabledCellsArray(List<List<Boolean>> disabledCellsArray) {
        this.mDisabledCellsArray = disabledCellsArray;
    }

    public CheckableGridView.OnCheckChangedListener getOnCheckChangedListener() {
        return mOnCheckChangedListener;
    }

    public void setOnCheckChangedListener(CheckableGridView.OnCheckChangedListener listener) {
        this.mOnCheckChangedListener = listener;
    }
}
