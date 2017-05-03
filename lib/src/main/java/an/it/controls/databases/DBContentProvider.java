package an.it.controls.databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class DBContentProvider extends ContentProvider {
    //content Provider Override
    private DBAdapter mDBAdapter;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public int delete(Uri uri, String where, String[] selectionArgs) {
        String table = getTableName(uri);
        return mDBAdapter.delete(table, where, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDBAdapter.insert(getTableName(uri),  values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(Uri.parse("content://" + "abc")/*DBAContentProvider.CONTENT_URI*/, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Log.e("aaaaaaaaaa",uri.toString());
        String table = getTableName(uri);
        Cursor cursor = mDBAdapter.query(false,table, projection, selection, selectionArgs, null, null, sortOrder,null);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        return mDBAdapter.update(table, values, selection, selectionArgs);
    }

    public String getTableName(Uri uri) {
        String value = uri.getLastPathSegment();
        //Log.e("----------TABLE--------------",value);
        return value;
    }

}