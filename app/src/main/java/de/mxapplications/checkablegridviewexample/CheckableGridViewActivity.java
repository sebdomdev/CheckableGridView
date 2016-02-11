package de.mxapplications.checkablegridviewexample;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import de.mxapplications.checkablegridview.CheckableGridView;

public class CheckableGridViewActivity extends AppCompatActivity {
    private CheckableGridView mCheckableGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkable_grid_view);

        mCheckableGridView = (CheckableGridView)findViewById(R.id.checkablegridview);

        String[] rowTitles = new String[]{"Row 1", "Row 2", "Row 3", "Row 4", "Row 5", "Row 6", "Row 7", "Row 8", "Row 9", "Row 10", "Row 11", "Row 12", "Row 13", "Row 14", "Row 15", };
        String[] columnTitles = new String[]{"Column 1", "Column 2", "Column 3", "Column 4", "Column 5", "Column 6", "Column 7", "Column 8", "Column 9", "Column 10"};
        mCheckableGridView.setData(rowTitles, columnTitles);
        mCheckableGridView.setOnCheckChangedListener(new CheckableGridView.OnCheckChangedListener() {
            @Override
            public void onChecked(int row, int column, boolean checked) {
                Toast.makeText(CheckableGridViewActivity.this, "Cell in row "+(row+1)+" and column "+(column+1)+(checked?" selected":" unselected"), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_check:
                mCheckableGridView.selectItem(1, 2, true);
                break;
            case R.id.action_uncheck:
                mCheckableGridView.selectItem(1, 2, false);
                break;
            case R.id.action_disable:
                mCheckableGridView.setDisabledCell(3, 1);
                break;
            case R.id.action_enable:
                mCheckableGridView.clearDisabledCells();
                break;
            case R.id.action_icons:
                mCheckableGridView.setIcons(ContextCompat.getDrawable(this, R.drawable.ic_check_circle_black_48dp), ContextCompat.getDrawable(this, R.drawable.ic_lens_black_48dp));
                break;
            case R.id.action_single_selection_column_on:
                mCheckableGridView.setSingleSelectionColumn(1, true);
                break;
            case R.id.action_single_selection_column_off:
                mCheckableGridView.setSingleSelectionColumn(1, false);
                break;
            case R.id.action_single_selection_row_on:
                mCheckableGridView.setSingleSelectionRow(1, true);
                break;
            case R.id.action_single_selection_row_off:
                mCheckableGridView.setSingleSelectionRow(1, false);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
