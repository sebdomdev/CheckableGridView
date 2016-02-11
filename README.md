# CheckableGridView

A view that displays checkboxes in a grid with column and row titles.

## Screenshots

![alt tag](https://cloud.githubusercontent.com/assets/12089383/12970453/e2a35326-d043-11e5-8b1b-5b6659f6ed83.png)
![alt tag](https://cloud.githubusercontent.com/assets/12089383/12970454/e2bb070a-d043-11e5-8a13-8e56185ed9fd.png)
[![ScreenShot](https://cloud.githubusercontent.com/assets/12089383/12970455/e2be30ba-d043-11e5-9cbb-d3e52832ef1c.png)](https://youtu.be/AGe-LnhXk-g)

## Code Example

In your layout xml file:
```xml
<de.mxapplications.checkablegridview.CheckableGridView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/checkablegridview" />
```

In your Activity/Fragment:
```java
CheckableGridView checkableGridView = (CheckableGridView)findViewById(R.id.checkablegridview);
checkableGridView.setData(new String[]{"Row 1", "Row 2"}, new String[]{"Col 1", "Col 2"});
checkableGridView.setOnCheckChangedListener(new CheckableGridView.OnCheckChangedListener() {
            @Override
            public void onChecked(int row, int column, boolean checked) {
                Toast.makeText(MyActivity.this, "Cell in row " + (row + 1) + " and column " + (column + 1) + (checked ? " selected" : " unselected"), Toast.LENGTH_SHORT).show();
            }
        });
 ```

## Installation

###1. Gracle dependency (JCenter)
Add the following to your build.gradle:
```gradle
compile 'com.github.sebdomdev:checkable-grid-view:1.0'
```
###2. Maven dependency (JCenter)
Add the following to your pom.xml:
```maven
<dependency> <groupId>com.github.sebdomdev</groupId> <artifactId>checkable-grid-view</artifactId> <version>1.0</version> <type>pom</type> </dependency>
```

## Optional Settings

```java
CheckableGridView checkableGridView = (CheckableGridView)findViewById(R.id.checkablegridview);
//Set the checkbox in row 2, column 3 as checked.
checkableGridView.selectItem(1, 2, true);
//Set the checkbox in row 2, column 3 as unchecked.
checkableGridView.selectItem(1, 2, false);
//Set a cell (containing the checkbox) in the grid view that cannot be selected by the user.
checkableGridView.setDisabledCell(3, 1);
//Clear all disabled cells, so that all checkboxes can be selected again.
checkableGridView.clearDisabledCells();
//Set your own icons for a checked/unchecked checkbox.
checkableGridView.setIcons(ContextCompat.getDrawable(this, R.drawable.my_checked_drawable), ContextCompat.getDrawable(this, R.drawable.my_unchecked_drawable));
//Set a column in which only one checkbox at a time can be checked.
checkableGridView.setSingleSelectionColumn(1, true);
//Set a column in which all checkboxes can be checked.
checkableGridView.setSingleSelectionColumn(1, false);
//Set a row in which only one checkbox at a time can be checked.
checkableGridView.setSingleSelectionRow(1, true);
//Set a row in which all checkboxes can be checked.
checkableGridView.setSingleSelectionRow(1, false);
```

## MIT License

Copyright (c) 2016 Sebastian Dombrowski

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
