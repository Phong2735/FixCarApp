package com.example.fixcarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME="db";
    private static final int DATABASE_VERSION=1;
    public DBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table if not exists db_center(id integer primary key autoincrement,logo int, tenCenter text, sdt text,diachiCenter text,email text, mota text)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS db_center");
        onCreate(sqLiteDatabase);
    }
    public boolean insertCenter(byte[] logo,String tenCenter, String sdt, String diachiCenter,String email, String mota) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put("logo",logo);
        values.put("tenCenter",tenCenter);
        values.put("sdt",sdt);
        values.put("diachiCenter",diachiCenter);
        values.put("email",email);
        values.put("mota",mota);
        long result = db.insert("db_center",null,values);
        if(result==-1)
            return false;
        return true;
    }
//    public int updateCenter(byte[] logo,String tenCenter, String sdt, String diachiCenter,String email, String mota)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values =new ContentValues();
//        values.put("logo",logo);
//        values.put("tenCenter",tenCenter);
//        values.put("sdt",sdt);
//        values.put("diachiCenter",diachiCenter);
//        values.put("email",email);
//        values.put("mota",mota);
//        String selectQuery = "SELECT id FROM centers WHERE email = ?";
//        Cursor cursor = db.rawQuery(selectQuery, new String[] {email});
//
//        int rowsUpdated = 0;
//        if (cursor != null && cursor.moveToFirst()) {
//            // Lấy id từ cursorx
//            int id = cursor.getInt(cursor.getColumnIndex("id"));
//
//            // Cập nhật dữ liệu
//            String whereClause = "id = ?";
//            String[] whereArgs = new String[] {String.valueOf(id)};
//            rowsUpdated = db.update("centers", values, whereClause, whereArgs);
//
//            cursor.close();
//        }
//
//        return rowsUpdated;
//    }
    public Cursor getData(String sql){
        SQLiteDatabase dtb = getReadableDatabase();
        return dtb.rawQuery(sql,null);
    }
    public void queryData(String sql){
        SQLiteDatabase dtb = getWritableDatabase();
        dtb.execSQL(sql);
    }
}
