package atua.anddev.globaltv.models;

import javax.swing.table.DefaultTableModel;

public class TableModelWithIcons extends DefaultTableModel {

    public TableModelWithIcons(Object obj[][], String str[]) {
        super(obj, str);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (getRowCount() > 0) {
            Object value = getValueAt(0, column);
            if (value != null) {
                return getValueAt(0, column).getClass();
            }
        }

        return super.getColumnClass(column);
    }
}
