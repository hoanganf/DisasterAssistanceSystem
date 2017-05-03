package an.it.controls.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import an.it.controls.databases.datas.Field;
import an.it.controls.databases.datas.TableData;


public class DBAdapter {
    private DBHelper mDBHelper;
    protected Context mContext;

    public DBAdapter(Context mContext, String databaseName, int databaseVersion) {
        this.mContext = mContext;
        this.mDBHelper = new DBHelper(mContext, databaseName, databaseVersion) {
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                DBAdapter.this.onUpgrade(db, oldVersion, newVersion);
            }
        };
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        if (mDBHelper != null) {
            SQLiteDatabase database = mDBHelper.getReadableDatabase();
            if (database != null && database.isOpen()) {
                Cursor cursor = database.query(distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
                return cursor;
            } else return null;
        } else return null;
    }

    protected int delete(String tableName, String where, String[] selectionArgs) {
        if (mDBHelper != null) {
            SQLiteDatabase dataBase = mDBHelper.getWritableDatabase();
            if (dataBase != null) return dataBase.delete(tableName, where, selectionArgs);
        }
        throw new SQLException("Failed to delete a record");
    }

    protected long insert(String table, ContentValues values) {
        if (mDBHelper != null) {
            SQLiteDatabase mDB = mDBHelper.getWritableDatabase();
            if (mDB != null) {
                long rowID = mDB.insert(table, null, values);
                if (rowID > 0) {
                    return rowID;
                }
            }
        }
        throw new SQLException("Failed to add a record into " + values.toString());
    }

    protected int update(String table, ContentValues values, String selection, String[] selectionArgs) {
        if (mDBHelper != null) {
            SQLiteDatabase database = mDBHelper.getWritableDatabase();
            if (database != null) {
                return database.update(table, values, selection, selectionArgs);
            }
        }
        throw new SQLException("Failed to update a record into " + values.toString());
    }

    protected void createDatabase(ArrayList<TableData> lstQuery) {
        if (mDBHelper != null) {
            mDBHelper.createDataBaseFromAssets();
            if (lstQuery != null) {
                try {
                    SQLiteDatabase database = mDBHelper.getWritableDatabase();
                    if (database != null) {
                        for (TableData sqLQuery : lstQuery) {
                            if (sqLQuery != null) {
                                if (sqLQuery.getTableName() != null && sqLQuery.getLstTableField() != null) {
                                    String sqlQueryEntries = "CREATE TABLE IF NOT EXISTS " + sqLQuery.getTableName() + " (";
                                    for (int i = 0; i < sqLQuery.getLstTableField().size(); i++) {
                                        Field field = sqLQuery.getLstTableField().get(i);
                                        sqlQueryEntries += field.getKey() + " " + field.getValue();
                                        if (i < sqLQuery.getLstTableField().size() - 1)
                                            sqlQueryEntries += ", ";
                                        else sqlQueryEntries += ")";
                                    }
                                    database.execSQL(sqlQueryEntries);
                                }

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void reCreateDatabase(ArrayList<TableData> lstQuery) {
        if (lstQuery != null) {
            try {
                SQLiteDatabase database = mDBHelper.getWritableDatabase();
                if (database != null) {
                    for (TableData sqLQuery : lstQuery) {
                        if (sqLQuery != null && sqLQuery.getTableName() != null) {
                            String sqlQueryEntries = "DROP TABLE IF EXISTS " + sqLQuery.getTableName();
                            database.execSQL(sqlQueryEntries);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            createDatabase(lstQuery);
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    ;

    public String getDatabaseName() {
        return mDBHelper != null ? mDBHelper.getDatabaseName() : null;
    }

    public int getDatabaseVersion() {
        return mDBHelper != null ? mDBHelper.getDatabaseVersion() : 1;
    }

    public boolean setDatabaseVersion(int databaseVersion) {
        if (mDBHelper != null) {
            mDBHelper.setDatabaseVersion(databaseVersion);
            return true;
        } else return false;
    }

    public boolean setDatabaseName(String name) {
        if (mDBHelper != null) {
            mDBHelper.setDatabaseName(name);
            return true;
        } else return false;
    }

    public void execSQL(String sql) {
        try {
            SQLiteDatabase database = mDBHelper.getWritableDatabase();
            if (database != null) {
                database.execSQL(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}