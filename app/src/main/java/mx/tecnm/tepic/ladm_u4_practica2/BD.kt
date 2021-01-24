package mx.tecnm.tepic.ladm_u4_practica2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class BD(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        try {
            db.execSQL("CREATE TABLE PRODUCTO_D(ID_PRODUCTO VARCHAR(50),TIENDA VARCHAR(50),CANTIDAD Int)")
        } catch (error: SQLiteException) {}
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}