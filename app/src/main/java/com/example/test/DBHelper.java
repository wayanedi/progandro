package com.example.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbMahasiswa.db";
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_ID = "id";
    public static final String USER_COLUMN_NAMA = "nama";
    public static final String USER_COLUMN_NIM = "nim";
    public static final String USER_COLUMN_UMUR = "umur";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table user " +
                        "(id integer primary key, nama text,nim text, umur integer)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);

    }

    public boolean insertMahasiswa(String nama, String nim, int umur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nama", nama);
        contentValues.put("nim", nim);
        contentValues.put("umur", umur);
        db.insert("user", null, contentValues);
        return true;
    }

    public Mahasiswa getData(int id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user where id="+id+"", null );

        res.moveToFirst();

        Mahasiswa mhs = new Mahasiswa();

        mhs.setNama(res.getString(res.getColumnIndex(USER_COLUMN_NAMA)));
        mhs.setNim(res.getString(res.getColumnIndex(USER_COLUMN_NIM)));
        mhs.setUmur(res.getInt(res.getColumnIndex(USER_COLUMN_UMUR)));

        if(!res.isClosed()){
            res.close();
        }

        return mhs;

    }

    public ArrayList<Mahasiswa> getAllCotacts() {
        ArrayList<Mahasiswa> mhs = new ArrayList<Mahasiswa>();

        Mahasiswa m = new Mahasiswa();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from user", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){

            m.setNama(res.getString(res.getColumnIndex(USER_COLUMN_NAMA)));
            m.setNim(res.getString(res.getColumnIndex(USER_COLUMN_NIM)));
            m.setUmur(res.getInt(res.getColumnIndex(USER_COLUMN_UMUR)));

            mhs.add(m);
            res.moveToNext();
        }
        return mhs;
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE_NAME, null, null);
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, USER_TABLE_NAME);
        return numRows;
    }
}
