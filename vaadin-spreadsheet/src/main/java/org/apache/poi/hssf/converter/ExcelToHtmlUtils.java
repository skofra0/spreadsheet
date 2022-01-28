
package org.apache.poi.hssf.converter;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Beta;

@Beta
public class ExcelToHtmlUtils extends AbstractExcelUtils {
    public static void appendAlign(StringBuilder style, HorizontalAlignment alignment) {
        String cssAlign = getAlign(alignment);
        if (isEmpty(cssAlign))
            return;

        style.append("text-align:");
        style.append(cssAlign);
        style.append(";");
    }

    /**
     * Creates a map (i.e. two-dimensional array) filled with ranges. Allow fast
     * retrieving {@link CellRangeAddress} of any cell, if cell is contained in
     * range.
     * 
     * @see #getMergedRange(CellRangeAddress[][], int, int)
     */
    public static CellRangeAddress[][] buildMergedRangesMap(Sheet sheet) {
        CellRangeAddress[][] mergedRanges = new CellRangeAddress[1][];
        for (final CellRangeAddress cellRangeAddress : sheet.getMergedRegions()) {
            final int requiredHeight = cellRangeAddress.getLastRow() + 1;
            if (mergedRanges.length < requiredHeight) {
                CellRangeAddress[][] newArray = new CellRangeAddress[requiredHeight][];
                System.arraycopy(mergedRanges, 0, newArray, 0, mergedRanges.length);
                mergedRanges = newArray;
            }

            for (int r = cellRangeAddress.getFirstRow(); r <= cellRangeAddress.getLastRow(); r++) {
                final int requiredWidth = cellRangeAddress.getLastColumn() + 1;

                CellRangeAddress[] rowMerged = mergedRanges[r];
                if (rowMerged == null) {
                    rowMerged = new CellRangeAddress[requiredWidth];
                    mergedRanges[r] = rowMerged;
                } else {
                    final int rowMergedLength = rowMerged.length;
                    if (rowMergedLength < requiredWidth) {
                        final CellRangeAddress[] newRow = new CellRangeAddress[requiredWidth];
                        System.arraycopy(rowMerged, 0, newRow, 0, rowMergedLength);

                        mergedRanges[r] = newRow;
                        rowMerged = newRow;
                    }
                }

                Arrays.fill(rowMerged, cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn() + 1, cellRangeAddress);
            }
        }
        return mergedRanges;
    }
}
