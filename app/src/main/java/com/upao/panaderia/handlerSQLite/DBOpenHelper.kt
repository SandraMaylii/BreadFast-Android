package com.upao.panaderia.handlerSQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLES().CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLES().TABLE_USER)
        db.execSQL(TABLES().CREATE_TABLE_USER)
        onCreate(db)
    }

    companion object {
        val DATABASE_NAME = "panaderia.db"
        val DATABASE_VERSION = 1
    }
}