package com.ks.flowerrecognition

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object {
        private const val DATABASE_VER = 1
        private const val DATABASE_NAME = "LOCDB.db"

        private const val TABLE_NAME = "Flower"
        private const val COL_ID = "Id"
        private const val COL_NAME = "Name"
        private const val COL_DESC = "Description"
        private const val COL_PHOTO = "PhotoURL"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY, $COL_NAME VARCHAR, $COL_DESC TEXT, $COL_PHOTO VARCHAR)"
        db?.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    val allFlowers:ArrayList<Flower>
        @SuppressLint("Recycle")
        get(){
            val flowers = ArrayList<Flower>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if(cursor.moveToFirst()){
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    val desc = cursor.getString(cursor.getColumnIndex(COL_DESC))
                    val url = cursor.getString(cursor.getColumnIndex(COL_PHOTO))

                    flowers.add(Flower(id, name, desc, url))
                }while (cursor.moveToNext())
            }
            db.close()
            return flowers
        }

    fun addFlower(flower: Flower){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, flower.id)
        values.put(COL_NAME, flower.name)
        values.put(COL_DESC, flower.description)
        values.put(COL_PHOTO, flower.photo)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateFlower(flower: Flower): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_ID, flower.id)
        values.put(COL_NAME, flower.name)
        values.put(COL_DESC, flower.description)
        values.put(COL_PHOTO, flower.photo)

        return db.update(TABLE_NAME,values, "$COL_ID = ?", arrayOf(flower.id.toString()))
    }

    fun deleteFlower(flower: Flower){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(flower.id.toString()))
        db.close()
    }


}