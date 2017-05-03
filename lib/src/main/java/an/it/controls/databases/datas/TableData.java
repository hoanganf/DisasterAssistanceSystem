package an.it.controls.databases.datas;

import java.util.ArrayList;

/**
 * Created by hoang on 6/5/2016.
 */
public class TableData {
    private String tableName;
    private ArrayList<Field> lstTableField;

    public TableData(String tableName,ArrayList<Field> lstTableField) {
        this.lstTableField = lstTableField;
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<Field> getLstTableField() {
        return lstTableField;
    }

    public void setLstTableField(ArrayList<Field> lstTableField) {
        this.lstTableField = lstTableField;
    }
}
