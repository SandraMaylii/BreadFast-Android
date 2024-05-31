package com.upao.panaderia.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.upao.panaderia.handlerSQLite.DBOpenHelper
import com.upao.panaderia.handlerSQLite.TABLES
import com.upao.panaderia.models.UserRequest
import com.upao.panaderia.models.UserResponse
import com.upao.panaderia.service.UserService

class UserRepository(context: Context) {

    private val dbHandler: DBOpenHelper = DBOpenHelper(context)
    private var db: SQLiteDatabase? = null

    private val allColumns = arrayOf(
        TABLES().USER_ID,
        TABLES().USER_NAME,
        TABLES().USER_LASTNAME,
        TABLES().USER_EMAIL,
        TABLES().USER_PASSWORD,
        TABLES().USER_ROLE,
        TABLES().USER_ISACTIVE,
        TABLES().USER_ISDELETE,
        TABLES().USER_CREATEDAT,
        TABLES().USER_UPDATEDAT
    )

    fun open() {
        Log.i(UserService.LOGTAG, "Users: Database opened")
        db = dbHandler.writableDatabase
    }

    fun close() {
        Log.i(UserService.LOGTAG, "Users: Database closed")
        dbHandler.close()
    }

    fun findByEmail(email: String): Boolean {
        val cursor = db?.query(
            TABLES().TABLE_USER,
            allColumns,
            "${TABLES().USER_EMAIL} = ?",
            arrayOf(email),
            null,
            null,
            null
        )
        val result = cursor?.count ?: 0
        cursor?.close()
        return result > 0
    }

    fun register(user: UserRequest): Boolean {
        val values = ContentValues()
        values.put(TABLES().USER_NAME, user.nombre)
        values.put(TABLES().USER_LASTNAME, user.apellido)
        values.put(TABLES().USER_EMAIL, user.email)
        values.put(TABLES().USER_PASSWORD, user.password)
        values.put(TABLES().USER_ROLE, user.rol)
        values.put(TABLES().USER_ISACTIVE, user.isActive)
        values.put(TABLES().USER_ISDELETE, user.isDelete)
        values.put(TABLES().USER_CREATEDAT, user.createdAT)
        values.put(TABLES().USER_UPDATEDAT, user.updatedAT)

        val insertId = db?.insert(TABLES().TABLE_USER, null, values)
        return insertId != -1L
    }
}