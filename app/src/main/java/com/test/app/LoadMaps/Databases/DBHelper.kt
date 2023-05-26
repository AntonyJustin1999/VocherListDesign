package com.test.app.LoadMaps.Databases

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    // below is the method for creating a database by a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // below is a sqlite query, where column names
        // along with their data types is given
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                EMAIL_COl + " TEXT," +
                NAME_COl + " TEXT," +
                IS_LOGIN + " TEXT" + ")")

        // we are calling sqlite
        // method for executing our query
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        // this method is to check if table already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    // This method is for adding data in our database
    fun addLoginDetails(email : String, isLogin : String){
        val values = ContentValues()
        //values.put(NAME_COl, name)
        values.put(EMAIL_COl, email)
        values.put(IS_LOGIN, isLogin)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun deleledata(email : String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "emailID =$email", null)
        db.close()
    }

    fun clearDatabase() {
        val db = this.writableDatabase
        val clearDBQuery = "DELETE FROM $TABLE_NAME"
        db.execSQL(clearDBQuery)
    }

    // all data from our database
    fun getAllLoginDetails(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
    }

    fun getLoginEmail():String{
        var emailId:String = ""
        val cursor = getAllLoginDetails()
        cursor!!.moveToFirst()
        emailId = cursor.getString(cursor.getColumnIndex(DBHelper.EMAIL_COl))
        return emailId;
    }

    fun isLogin(emailId:String): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE  emailID = " + emailId, null)
        //return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE  emailID = " + emailId + " and name = " + name, null)
    }

    fun getLoginCount(): Long {
        val db = this.readableDatabase
        val count = DatabaseUtils.queryNumEntries(db, TABLE_NAME)
        db.close()
        return count
    }

//    fun selectOne(emailId: String): LoginDetails? {
//        val db = this.readableDatabase
//        val Query: String
//        Query =
//            "SELECT * FROM " + TABLE_NAME + " WHERE  batch = " + BatchNo + " and ttc = " + TTC
//        val cursor: Cursor = db.rawQuery(Query, null)
//        var sku: ISO? = null
//        if (cursor != null && cursor.moveToNext()) {
//            sku = AddData(
//                cursor.getInt(0),
//                cursor.getInt(1),
//                cursor.getString(2),
//                cursor.getInt(3),
//                cursor.getString(4),
//                cursor.getString(5),
//                cursor.getString(6),
//                cursor.getString(7),
//                cursor.getString(8),
//                cursor.getString(9),
//                cursor.getString(10),
//                cursor.getString(11),
//                cursor.getString(12),
//                cursor.getString(13),
//                cursor.getString(14),
//                cursor.getInt(15),
//                cursor.getInt(16),
//                cursor.getInt(17),
//                cursor.getString(18),
//                cursor.getString(19),
//                cursor.getString(20),
//                cursor.getString(21),
//                cursor.getString(22),
//                cursor.getString(23),
//                cursor.getString(24),
//                cursor.getInt(25),
//                cursor.getString(26),
//                cursor.getString(27),
//                cursor.getString(28),
//                cursor.getString(29),
//                cursor.getInt(30),
//                cursor.getInt(31),
//                cursor.getInt(32),
//                cursor.getInt(33),
//                cursor.getInt(34),
//                cursor.getString(35),
//                cursor.getString(36),
//                cursor.getInt(37),
//                cursor.getString(38),
//                cursor.getString(39),
//                cursor.getString(40),
//                cursor.getString(41),
//                cursor.getString(42),
//                cursor.getString(43),
//                cursor.getString(44),
//                cursor.getString(45),
//                cursor.getString(46),
//                cursor.getString(47),
//                cursor.getString(48),
//                cursor.getString(49),
//                cursor.getString(50),
//                cursor.getString(51),
//                cursor.getString(52),
//                cursor.getString(53),
//                cursor.getString(54),
//                cursor.getString(55),
//                cursor.getString(56),
//                cursor.getString(57),
//                cursor.getString(58),
//                cursor.getString(59),
//                cursor.getString(60),
//                cursor.getString(61),
//                cursor.getString(62),
//                cursor.getString(63),
//                cursor.getString(64),
//                cursor.getString(65),
//                cursor.getString(66),
//                cursor.getString(67),
//                cursor.getString(68),
//                cursor.getString(69),
//                cursor.getString(70),
//                cursor.getString(71),
//                cursor.getString(72),
//                cursor.getString(73),
//                cursor.getString(74)
//            )
//        }
//        cursor.close()
//        closeDB()
//        return sku
//    }

    // below is the method for updating our courses
    fun updateLoginDetails(email: String, isLogin: String?) {

        // calling a method to get writable database.
        val db = this.writableDatabase
        val values = ContentValues()

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(IS_LOGIN, isLogin)

        db.update(TABLE_NAME, values, "emailID=?", arrayOf(email))
        db.close()
    }

    companion object{
        // here we have defined variables for our database

        // below is variable for database name
        private val DATABASE_NAME = "LOGIN_USERS"

        // below is the variable for database version
        private val DATABASE_VERSION = 1

        val TABLE_NAME = "users_table"
        val ID_COL = "id"
        val EMAIL_COl = "emailID"
        val NAME_COl = "name"
        val IS_LOGIN = "isLogin"
    }
}