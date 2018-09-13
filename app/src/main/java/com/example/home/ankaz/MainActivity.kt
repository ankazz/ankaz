package com.example.home.ankaz

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.AsyncTask
import java.io.PrintWriter
import java.io.StringWriter
import android.content.Intent
import android.widget.*
import com.example.home.ankaz.AgreementActivity
import com.example.home.ankaz.addOnItemClickListener
import java.sql.Connection


class MainActivity : AppCompatActivity() {

    lateinit var mSearchText : EditText
    lateinit var mRecyclerView: RecyclerView
    private var itemArrayList: ArrayList<Deal>? = null  //List items Array
    private var myAppAdapter: MyAppAdapter? = null //Array Adapter
    private var success = false // boolean
    var dealNumer: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSearchText = findViewById(R.id.searchText)
        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))


        var connectionClass = Conn() // Connection Class Initialization
        itemArrayList = ArrayList() // Arraylist Initialization


        //Agreement("1003")

        mSearchText.addTextChangedListener(object  : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                val searchText = mSearchText.getText().toString().trim()

                val orderData = SyncData()
                orderData.execute("")
            }
        })

        mRecyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                dealNumer = itemArrayList!!.get(position).deal_num
                //Toast.makeText(this@MainActivity, dealNumer + " was clicked!", Toast.LENGTH_SHORT).show()
                Agreement(dealNumer)
            }
        })

    }

    fun Agreement (dealN: String?){
        val agreementIntent = Intent(this, CardActivity::class.java)
        agreementIntent.putExtra(CardActivity.DEAL_NUM, dealN)
        startActivity(agreementIntent)
    }


    private inner class SyncData : AsyncTask<String, String, String>() {
        internal var msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!"
        //internal var progress: ProgressDialog

        override fun onPreExecute() //Starts the progress dailog
        {
            //progress = ProgressDialog.show(this@MainActivity, "Synchronising","RecyclerView Loading! Please Wait...", true)
        }

        override fun doInBackground(vararg strings: String)  // Connect to the database, write query and add items to array list
                : String {
            try {
                itemArrayList?.clear()

                var conn = Conn()
                var con = conn.connectionclass()
                if (con == null) {
                    success = false
                } else {
                    // Change below query according to your own database.
                    val searchText = mSearchText.getText().toString().trim()
                    val query = "SELECT TOP 10  deal_num,subject_name,address_name FROM deals WHERE deal_num LIKE '%"+searchText+"%'"
                    val stmt = con.createStatement()
                    val rs = stmt.executeQuery(query)
                    if (rs != null)
                    // if resultset not null, I add items to itemArraylist using class created
                    {
                        while (rs!!.next()) {
                            try {
                                //itemArrayList?.add(User(rs!!.getString("user_name"), rs!!.getString("user_id")))
                                itemArrayList?.add(Deal(rs!!.getString("deal_num"), rs!!.getString("subject_name"), rs!!.getString("address_name")))
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
            //progress.dismiss()
            Toast.makeText(this@MainActivity, msg + "", Toast.LENGTH_SHORT).show()
            if (success === false) {
            } else {
                try {
                    myAppAdapter = MyAppAdapter(itemArrayList, this@MainActivity)
                    mRecyclerView.setAdapter(myAppAdapter)
                } catch (ex: Exception) {

                }

            }
        }
    }

    // Constructor
    inner class MyAppAdapter(private val values: ArrayList<Deal>?, var context: Context) : RecyclerView.Adapter<MyAppAdapter.ViewHolder>() {

        inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout){
            private var view: View = layout
            // public image title and image url
            var dealNum: TextView
            var subjectName: TextView
            var addressName: TextView

            init {
                dealNum = layout.findViewById<View>(R.id.dealNum) as TextView
                subjectName = layout.findViewById<View>(R.id.subjectName) as TextView
                addressName = layout.findViewById<View>(R.id.addressName) as TextView
            }
        }

        // Create new views (invoked by the layout manager) and inflates
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAppAdapter.ViewHolder {
            // create a new view
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.list_content, parent, false)
            return ViewHolder(v)
        }

        // Binding items to the view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val Deal = values!![position]
            holder.dealNum.setText(Deal.deal_num)
            holder.subjectName.setText(Deal.subject_name)
            holder.addressName.setText(Deal.address_name)

            //Picasso.with(context).load("http://" + classListItems.getImg()).into(holder.imageView)
        }

        // get item count returns the list item count
        override fun getItemCount(): Int {
            return values!!.size
        }

    }



}
