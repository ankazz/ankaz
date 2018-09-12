package com.example.home.ankaz

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.home.ankaz.R
import kotlinx.android.synthetic.main.activity_agreement.*
import java.io.PrintWriter
import java.io.StringWriter
import java.sql.Connection
import android.widget.TextView



class AgreementActivity : AppCompatActivity() {

    companion object {
        const val DEAL_NUM = "0"
    }

    private var itemArrayList: ArrayList<Deal>? = null  //List items Array
    private var success = false // boolean
    private var connectionClass: Conn? = null //Connection Class Variable
    var dealN: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
        dealN = intent.getStringExtra(DEAL_NUM)
        //Subj_name.setText(dealN)
        loadData().execute()
        //fill()
    }

    private fun setText(text: TextView, value: String) {
        runOnUiThread {
            text.text = value
            //Subj_name.setText(value)
        }
    }

    inner class loadData : AsyncTask<String, String, String>() {

        internal var msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!"
        //internal var progress: ProgressDialog

        override fun onPreExecute() //Starts the progress dailog
        {
            //progress = ProgressDialog.show(this@MainActivity, "Synchronising","RecyclerView Loading! Please Wait...", true)
        }

        override fun doInBackground(vararg strings: String) : String {
            try {
                var conn = Conn()
                var con = conn.connectionclass()
                if (con == null) {
                    success = false
                } else {
                    // Change below query according to your own database.
                    //val searchText = mSearchText.getText().toString().trim()
                    val query = "SELECT deal_num,subject_name,address_name FROM deals WHERE deal_num = '"+dealN+"'"
                    val stmt = con.createStatement()
                    val rs = stmt.executeQuery(query)
                    if (rs != null)
                    // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs!!.next()) {
                            try {
                                setText(Subj_name,rs.getString("subject_name"))
                                setText(Subj_deal,rs.getString("deal_num"))
                                setText(Subj_address,rs.getString("address_name"))
                                /*
                                Subj_name.setText(rs.getString("subject_name"))
                                Subj_deal.setText(rs.getString("deal_num"))
                                Subj_address.setText(rs.getString("address_name"))*/

                                //itemArrayList?.add(Deal("10", rs!!.getString("subject_name"), rs!!.getString("address_name")))
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }

                        }
                        msg = "Найдено"
                        success = true
                    } else {
                        msg = "Данные не найдены!"
                        success = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val writer = StringWriter()
                e.printStackTrace(PrintWriter(writer))
                msg = writer.toString()
                success = false
            }

            return msg
        }

        override fun onPostExecute(msg: String) // disimissing progress dialoge, showing error and setting up my listview
        {
            if (success!!) {
                Toast.makeText(this@AgreementActivity, msg, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@AgreementActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
