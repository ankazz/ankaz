package com.example.home.ankaz

import android.annotation.SuppressLint
import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class Conn (){
    @SuppressLint("NewApi")
    fun connectionclass(): Connection? {
        val usernam = "energosystema"
        val passwordd = "uebdR4Y82b"
        val Host = "aktobe.akteh.kz;" // provide the username
        val dbName = "billing"


        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var connection: Connection? = null

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            val ConnectionURL = "jdbc:jtds:sqlserver://" + Host + dbName + ";user=" + usernam+ ";password=" + passwordd + ";"
            connection = DriverManager.getConnection(ConnectionURL)
        } catch (se: SQLException) {
            Log.e("error no 1", se.message)
        } catch (e: ClassNotFoundException) {
            Log.e("error no 2", e.message)
        } catch (e: Exception) {
            Log.e("error no 3", e.message)
        }

        return connection
    }
}