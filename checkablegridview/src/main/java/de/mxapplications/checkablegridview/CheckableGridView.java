package de.mxapplications.checkablegridview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A view that displays checkboxes in a grid with column and row titles. The user can select checkboxes.
 * Rows and columns are 0-based.
 * <p>Here is a simple example of how to use CheckableGridView:
 *    <pre>
 *        checkableGridView.setData(rowTitles, columnTitles);
 *        checkableGridView.setOnCheckChangedListener(new CheckableGridView.OnCheckChangedListener() {
 *           public void onChecked(int row, int column, boolean checked) {
 *              Toast.makeText(CheckableGridViewActivity.this, "Cell in row "+(row+1)+" and column "+(column+1)+(checked?" selected":" unselected"), Toast.LENGTH_SHORT).show();
 *           }
 *        });
 *    </pre>
 *     You can receive the checked boxes by either using the OnCheckChangeListener (see above) or by using {@link #getCheckedArray()}
 * </p>
 *
 * Created by Sebastian Dombrowski on 11/17/2015.
 */
public class CheckableGridView extends FrameLayout {
    private LinearLayout mHeader;
    private ListView mFirstColumn;
    private ListView mCheckboxesListView;

    private CheckableGridViewHeaderAdapter mHeaderAdapter;
    private CheckableGridViewFirstColumnAdapter mFirstColumnAdapter;
    private CheckableGridViewGridViewAdapter mGridViewAdapter;

    private Set<Integer> mSingleSelectionColumns = new HashSet<>();
    private Set<Integer> mSingleSelectionRows = new HashSet<>();
    private List<List<Boolean>> mDisabledCellsArray =new ArrayList<>();

    /***
     * Constructs the CheckableGridView .
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     */
    public CheckableGridView(Context context) {
        super(context);
        init();
    }

    /***
     * Constructs the CheckableGridView .
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     */
    public CheckableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /***
     * Constructs the CheckableGridView .
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     */
    public CheckableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /***
     * Constructs the CheckableGridView .
     *
     * @param context The Context the view is running in, through which it can
     *        access the current theme, resources, etc.
     * @param attrs The attributes of the XML tag that is inflating the view.
     * @param defStyleAttr An attribute in the current theme that contains a
     *        reference to a style resource that supplies default values for
     *        the view. Can be 0 to not look for defaults.
     * @param defStyleRes A resource identifier of a style resource that
     *        supplies default values for the view, used only if
     *        defStyleAttr is 0 or can not be found in the theme. Can be 0
     *        to not look for defaults.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CheckableGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.checkable_gridview, this);
        mHeader = (LinearLayout)findViewById(R.id.checkablegridview_header_grid_view);
        mFirstColumn = (ListView)findViewById(R.id.checkablegridview_first_column_list_view);
        mCheckboxesListView = (ListView)findViewById(R.id.checkablegridview_list_view);

        final HorizontalScrollView headerScrollView = (HorizontalScrollView)findViewById(R.id.checkablegridview_header_horizontal_scroll_view);
        final HorizontalScrollView contentScrollView = (HorizontalScrollView)findViewById(R.id.checkablegridview_content_horizontal_scroll_view);
        headerScrollView.setHorizontalScrollBarEnabled(false);
        contentScrollView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                headerScrollView.scrollTo(v.getScrollX(), 0);
                return false;
            }
        });

        mHeaderAdapter = new CheckableGridViewHeaderAdapter(new CheckableGridViewHeaderAdapter.OnColumnSelectedListener() {
            @Override
            public void onColumnSelected(int columnNumber) {
                selectAllInColumn(columnNumber);
            }
        });

        mFirstColumnAdapter = new CheckableGridViewFirstColumnAdapter(new CheckableGridViewFirstColumnAdapter.OnRowSelectedListener() {
            @Override
            public void onRowSelected(int rowNumber) {
                selectAllInRow(rowNumber);
            }
        });
        mFirstColumn.setAdapter(mFirstColumnAdapter);

        mGridViewAdapter = new CheckableGridViewGridViewAdapter();
        mCheckboxesListView.setAdapter(mGridViewAdapter);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        setListViewHeightBasedOnChildren(mFirstColumn);
        setListViewHeightBasedOnChildren(mCheckboxesListView);
        super.onLayout(changed, left, top, right, bottom);
    }

    /***
     * Set the row and column titles that will be displayed.
     * @param rowTitles An array of String objects containing the row titles.
     * @param columnTitles An array of String objects containing the column titles.
     */
    public void setData(String[] rowTitles, String[] columnTitles){
        setData(Arrays.asList(rowTitles), Arrays.asList(columnTitles));
    }

    /***
     * Set the row and column titles that will be displayed.
     * @param rowTitles A List of String objects containing the row titles.
     * @param columnTitles A List of String objects containing the column titles.
     */
    public void setData(List<String> rowTitles, List<String> columnTitles){
        mHeaderAdapter.setHeadings(columnTitles);
        mHeaderAdapter.notifyDataSetChanged();
        mHeader.removeAllViews();
        applyAdapterToLinearLayout(mHeaderAdapter, mHeader);

        FrameLayout space = new FrameLayout(mHeader.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Helper.convertDpToPixels(10), 0);
        space.setLayoutParams(layoutParams);
        mHeader.addView(space);

        mFirstColumnAdapter.setFirstColumnValues(rowTitles);
        mFirstColumnAdapter.notifyDataSetChanged();


        mGridViewAdapter.setCheckedArray(new boolean[rowTitles.size()][columnTitles.size()]);
        mGridViewAdapter.setSingleSelectionColumns(mSingleSelectionColumns);
        mGridViewAdapter.setSingleSelectionRows(mSingleSelectionRows);
        mGridViewAdapter.setDisabledCellsArray(mDisabledCellsArray);
        mGridViewAdapter.notifyDataSetChanged();

    }

    /***
     * Returns a 2-dimensional array of boolean values representing the checked cells.
     * The array is row-based, i.e. the returned object is an array containing a boolean array for each row, which in turn contains the values for each column.
     * The array has the size boolean[number_of_rows][number_of_rows] where number_of_rows equals the size of the array/list rowTitles (first argument) passed into {@link #setData(String[], String[])}/{@link #setData(List, List)}.
     * The array has the size boolean[number_of_rows][number_of_columns] where number_of_columns equals the size of the array/list columnTitles (second argument) passed into {@link #setData(String[], String[])}/{@link #setData(List, List)}.
     * An entry in the 2-dimensional array is true of the checkbox was checked by the user, false otherwise.
     *
     * @return A 2-dimensional row-based array, where a value is true if the according cell had been checked by the user.
     */
    public boolean[][] getCheckedArray(){
        return mGridViewAdapter.getCheckedArray();
    }

    /***
     * Set a cell in the grid view that cannot be selected by the user.
     * @param row The row index of the cell that cannot be selected.
     * @param column The column index of the cell that cannot be selected.
     */
    public void setDisabledCell(int row, int column){
        setDisabledCell(row, column, true);
    }
    private void setDisabledCell(int row, int column, boolean update){
        if(row>= mDisabledCellsArray.size()){
            for(int i=0;i<=row;i++) {
                mDisabledCellsArray.add(new ArrayList<Boolean>());
            }
        }

        if(column>= mDisabledCellsArray.get(row).size()){
            for(int i=0;i<column;i++){
                mDisabledCellsArray.get(row).add(false);
            }
            mDisabledCellsArray.get(row).add(column, true);
        }else{
            mDisabledCellsArray.get(row).set(column, true);
        }
        if(update) {
            mGridViewAdapter.notifyDataSetChanged();
        }
    }

    /***
     * Set cells that cannot be selected by the user in the grid view. The parameter is a 2-dimensional array of booleans.
     * The array is row-based, i.e. disabledCellsMatrix has a boolean array for each row, which in turn contains the values for each column.
     * If a value is true, the cell cannot be selected by the user, if false, it can be selected.
     *
     * @param disabledCellsMatrix The 2-dimensional array indicating if a cell can be selected by a user or not.
     */
    public void setDisabledCells(boolean[][] disabledCellsMatrix){
        for(int i=0;i<disabledCellsMatrix.length;i++){
            for(int j=0;j<disabledCellsMatrix[i].length;j++){
                if(disabledCellsMatrix[i][j]){
                    setDisabledCell(i, j, false);
                }
            }
        }
        mGridViewAdapter.notifyDataSetChanged();
    }

    /***
     * Clears the disabled cells. That means after calling this method, all cells are selectable by the user.
     */
    public void clearDisabledCells(){
        mDisabledCellsArray.clear();
        mGridViewAdapter.notifyDataSetChanged();
    }

    /***
     * Set a column in which only one cell at a time can be selected. If a user selects a cell, any prior selected cell will be unselected.
     * @param column The column in which only one cell at a time is selectable.
     * @param singleSelection Indicates if only one cell in the column is selectable by the user or if multiple cells are selectable.
     */
    public void setSingleSelectionColumn(int column, boolean singleSelection){
        if(singleSelection) {
            mSingleSelectionColumns.add(column);
        }else{
            mSingleSelectionColumns.remove(column);
        }
    }

    /***
     * Returns an array which contains the column numbers of columns in which only one cell is selectable.
     * @return An array which contains the column numbers of columns in which only one cell is selectable.
     */
    public Integer[] getSingleSectionColumnsArray(){
        return mSingleSelectionColumns.toArray(new Integer[mSingleSelectionColumns.size()]);
    }

    /***
     * Set a row in which only one cell at a time can be selected. If a user selects a cell, any prior selected cell will be unselected.
     * @param row The row in which only one cell at a time is selectable.
     * @param singleSelection Indicates if only one cell in the row is selectable by the user or if multiple cells are selectable.
     */
    public void setSingleSelectionRow(int row, boolean singleSelection){
        if(singleSelection) {
            mSingleSelectionRows.add(row);
        }else{
            mSingleSelectionRows.remove(row);
        }
    }
    /***
     * Returns an array which contains the row numbers of rows in which only one cell is selectable.
     * @return An array which contains the row numbers of rows in which only one cell is selectable.
     */
    public Integer[] getSingleSectionRowsArray(){
        return mSingleSelectionRows.toArray(new Integer[mSingleSelectionRows.size()]);
    }

    /***
     * This interface contains a method that will be called when the user selects a cell.
     */
    public interface OnCheckChangedListener{
        /***
         * This method will be called when the user selects a cell.
         * @param row The 0-based row index of the selected cell.
         * @param column The 0-based column index of the selected cell.
         * @param checked True if the cell has been selected by the user, false otherwise.
         */
        void onChecked(int row, int column, boolean checked);
    }

    /***
     * Set the {@link de.mxapplications.checkablegridview.CheckableGridView.OnCheckChangedListener}.
     * @param listener The OnCheckedChangeListener that will be notified if the user selects cells in the grid view.
     */
    public void setOnCheckChangedListener(CheckableGridView.OnCheckChangedListener listener) {
        mGridViewAdapter.setOnCheckChangedListener(listener);
    }

    /***
     * Set icons that will be used to indicate a selected/unselected cell.
     * @param checkedIcon The drawable that will be used to indicate a checked cell.
     * @param unCheckedIcon The drawable that will be used to indicate an unchecked cell.
     */
    public void setIcons(Drawable checkedIcon, Drawable unCheckedIcon){
        if(mGridViewAdapter!=null){
            mGridViewAdapter.setIcons(checkedIcon, unCheckedIcon);
        }
    }

    /***
     * Select or unselect a cell in the grid view, i.e. the cell will be shown as checked/unchecked.
     * @param row The 0-based row index of the cell that will be selected/unselected.
     * @param column The 0-based column index of the cell that will selected/unselected.
     * @param selected If true, the cell will be shown as checked, if false, it will be shown as unchecked.
     */
    public void selectItem(int row, int column, boolean selected){
        if(mGridViewAdapter!=null&&mGridViewAdapter.getCheckedArray()!=null){
            mGridViewAdapter.setCheckedCell(row, column, selected);
        }
    }

    /***
     * Set cells that will be checked in the grid view. The parameter is a 2-dimensional array of booleans.
     * The array is row-based, i.e. selectionMatrix has a boolean array for each row, which in turn contains the values for each column.
     * If a value is true, the cell will be shown as checked, if false, it will be shown as unchecked.
     *
     * @param selectionMatrix The 2-dimensional array indicating if a cell is shown as checked (true) or unchecked (false).
     */
    public void selectItems(boolean[][] selectionMatrix){
        if(getCheckedArray().length==selectionMatrix.length){
            mGridViewAdapter.setCheckedArray(selectionMatrix);
        }
    }
    private void selectAllInColumn(int columnNumber){
        int selectedCount=0;
        boolean[][] checkedArray = mGridViewAdapter.getCheckedArray();
        if(getCheckedArray().length<=0||columnNumber>=getCheckedArray().length)return;
        for(int i=0;i<checkedArray.length;i++){
            if(checkedArray[i][columnNumber]) {
                selectedCount++;
            }
        }
        boolean selected = Math.round((float)selectedCount/(float)checkedArray.length)<0.5f;
        for(int i=0;i<checkedArray.length;i++){
            selectItem(i, columnNumber, selected);
        }
    }
    private void selectAllInRow(int rowNumber){
        int selectedCount=0;
        boolean[][] checkedArray = mGridViewAdapter.getCheckedArray();
        if(getCheckedArray().length<rowNumber)return;
        for(int i=0;i<checkedArray[rowNumber].length;i++){
            if(checkedArray[rowNumber][i]) {
                selectedCount++;
            }
        }
        boolean selected = Math.round((float)selectedCount/(float)checkedArray[rowNumber].length)<0.5f;
        for(int i=0;i<checkedArray[rowNumber].length;i++){
            selectItem(rowNumber, i, selected);
        }
    }
    private void applyAdapterToLinearLayout(BaseAdapter adapter, LinearLayout linearLayout){
        for(int i=0;i<adapter.getCount();i++){
            linearLayout.addView(adapter.getView(i, null, linearLayout));
        }
    }
    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
