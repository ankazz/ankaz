package com.example.home.ankaz

import android.content.Context
import android.os.AsyncTask
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_card.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.PrintWriter
import java.io.StringWriter
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.app.Dialog
import android.graphics.Color
import android.support.v4.view.ViewPager.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import com.example.home.ankaz.R.id.pointName

class AgreementActivity : AppCompatActivity() {

    companion object {
        const val DEAL_NUM = "0"
    }

    lateinit var mRecyclerView: RecyclerView
    private var itemArrayList: ArrayList<Point>? = null  //List items Array
    private var myAppAdapter: MyAppAdapter? = null //Array Adapter

    lateinit var bRecyclerView: RecyclerView
    private var myBalancesAdapter: MyBalancesAdapter? = null //Array Adapter
    private var itemBalancesList: ArrayList<DealBalances>? = null

    private var success = false // boolean
    private var connectionClass: Conn? = null //Connection Class Variable
    var dealN: String? = null

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        container.setOffscreenPageLimit(3);

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        container.setOnPageChangeListener(object : OnPageChangeListener {

            override fun onPageSelected(position: Int) {


            }

            override fun onPageScrolled(position: Int, positionOffset: Float,
                                        positionOffsetPixels: Int) {
                if(position==1 && itemArrayList == null) home()
                if(position==2 && itemBalancesList == null) bHome()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        dealN = intent.getStringExtra(AgreementActivity.DEAL_NUM)
        //Subj_name.setText(dealN)

        loadData().execute()

        //home()

/*

        mRecyclerView = findViewById(R.id.recyclerViewPoints)
        //mRecyclerView = recyclerViewPoints
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        itemArrayList = ArrayList() // Arraylist Initialization


        mRecyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                var dealNumer = itemArrayList!!.get(position).deal_num
                //Toast.makeText(this@MainActivity, dealNumer + " was clicked!", Toast.LENGTH_SHORT).show()
                //Agreement(dealNumer)
            }
        })*/

    }

    fun home(){
        mRecyclerView = findViewById(R.id.recyclerViewPoints)
        //mRecyclerView = recyclerViewPoints
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        itemArrayList = ArrayList() // Arraylist Initialization


        mRecyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                var dealNumer = itemArrayList!!.get(position).Serial_Number
                Toast.makeText(this@AgreementActivity, dealNumer + " was clicked!", Toast.LENGTH_SHORT).show()
                //Agreement(dealNumer)
            }
        })


        val orderData = SyncData()
        orderData.execute("")

    }

    fun bHome(){
        bRecyclerView = findViewById(R.id.recyclerViewBalances)
        //mRecyclerView = recyclerViewPoints
        bRecyclerView.setHasFixedSize(true)
        bRecyclerView.setLayoutManager(LinearLayoutManager(this))
        itemBalancesList = ArrayList() // Arraylist Initialization

        bRecyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                var dealNumer = itemBalancesList!!.get(position).Period_Name
                //Toast.makeText(this@MainActivity, dealNumer + " was clicked!", Toast.LENGTH_SHORT).show()
                //Agreement(dealNumer)
            }
        })

        val orderData = SyncDataBalances()
        orderData.execute("")
    }

    private fun setText(text: TextView, value: String) {
        runOnUiThread {
            text.text = value
            //Subj_name.setText(value)
        }
    }

    inner class loadData() : AsyncTask<String, String, String>() {

        internal var msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!"
        var dialog = Dialog(this@AgreementActivity,android.R.style.Theme_Translucent_NoTitleBar)

        override fun onPreExecute() //Starts the progress dailog
        {
            val view = this@AgreementActivity.layoutInflater.inflate(R.layout.full_screen_progress_bar,null)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.show()
            super.onPreExecute()
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
                    val query = "SELECT * FROM esCard WHERE deal_num = '"+dealN+"'"
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

                                setText(Subj_phone,rs.getString("phone_number"))
                                setText(Subj_mail,rs.getString("mail"))
                                setText(Subj_inn,rs.getString("id_num"))
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
                dialog.dismiss()
            } else {
                Toast.makeText(this@AgreementActivity, msg, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }



    //Запрос для ТУ
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
                    val query = "SELECT * FROM esPoints WHERE deal_num = '"+dealN+"'"
                    val stmt = con.createStatement()
                    val rs = stmt.executeQuery(query)
                    if (rs != null)
                    {
                        while (rs.next()) {
                            try {
                                itemArrayList?.add(Point(
                                        rs.getString("Point_Name"),
                                        rs.getString("Status_Name"),
                                        rs.getString("Serial_Number"),
                                        rs.getString("AssetModel_Name"),
                                        rs.getString("Address_Name")
                                ))
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
            Toast.makeText(this@AgreementActivity, msg + "", Toast.LENGTH_SHORT).show()
            if (success === false) {
            } else {
                try {
                    myAppAdapter = MyAppAdapter(itemArrayList, this@AgreementActivity)
                    mRecyclerView.setAdapter(myAppAdapter)
                } catch (ex: Exception) {

                }

            }
        }
    }

    // Constructor
    inner class MyAppAdapter(private val values: ArrayList<Point>?, var context: Context) : RecyclerView.Adapter<MyAppAdapter.ViewHolder>() {

        inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout){
            private var view: View = layout
            // public image title and image url
            var pointName: TextView
            //var pointStatus: TextView
            var pointAssets: TextView
            //var pointAssetModelName: TextView
            var pointAddressName: TextView

            init {
                pointName = layout.findViewById<View>(R.id.Point_Name) as TextView
                //pointStatus = layout.findViewById<View>(R.id.Status_Name) as TextView
                pointAssets = layout.findViewById<View>(R.id.Serial_Number) as TextView
                //pointAssetModelName = layout.findViewById<View>(R.id.AssetModel_Name) as TextView
                pointAddressName = layout.findViewById<View>(R.id.Address_Name) as TextView
            }
        }

        // Create new views (invoked by the layout manager) and inflates
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAppAdapter.ViewHolder {
            // create a new view
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.list_points, parent, false)
            return ViewHolder(v)
        }

        // Binding items to the view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val Point = values!![position]

            if (Point.Status_Name == "Подключена"){
                holder.pointName.setTextColor(Color.parseColor("#00cc00"))
            }else{
                holder.pointName.setTextColor(Color.parseColor("#bdbdbd"))
            }
            holder.pointName.setText(Point.Point_Name)
            //holder.pointStatus.setText(Point.Status_Name)
            holder.pointAssets.setText(Point.Serial_Number)
            //holder.pointAssetModelName.setText(Point.AssetModel_Name)
            holder.pointAddressName.setText(Point.Address_Name)

            //Picasso.with(context).load("http://" + classListItems.getImg()).into(holder.imageView)
        }

        // get item count returns the list item count
        override fun getItemCount(): Int {
            return values!!.size
        }

    }
    //Закрытие для ТУ

    //Запрос для Balances
    private inner class SyncDataBalances : AsyncTask<String, String, String>() {
        internal var msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!"
        //internal var progress: ProgressDialog

        override fun onPreExecute() //Starts the progress dailog
        {
            //progress = ProgressDialog.show(this@MainActivity, "Synchronising","RecyclerView Loading! Please Wait...", true)
        }

        override fun doInBackground(vararg strings: String)  // Connect to the database, write query and add items to array list
                : String {
            try {
                itemBalancesList?.clear()

                var conn = Conn()
                var con = conn.connectionclass()
                if (con == null) {
                    success = false
                } else {
                    // Change below query according to your own database.
                    val query = "SELECT  top 12 * FROM esDealBalances WHERE deal_num = '"+dealN+"' ORDER BY date_begin DESC"
                    val stmt = con.createStatement()
                    val rs = stmt.executeQuery(query)
                    if (rs != null)
                    {
                        while (rs.next()) {
                            try {
                                itemBalancesList?.add(DealBalances(
                                        rs.getString("Period_Name"),
                                        rs.getString("StartBalance_Value"),
                                        rs.getString("Consumption_Value"),
                                        rs.getString("Debit_Value"),
                                        rs.getString("Credit_Value"),
                                        rs.getString("EndBalance_Value")
                                ))
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
            Toast.makeText(this@AgreementActivity, msg + "", Toast.LENGTH_SHORT).show()
            if (success === false) {
            } else {
                try {
                    myBalancesAdapter = MyBalancesAdapter(itemBalancesList, this@AgreementActivity)
                    bRecyclerView.setAdapter(myBalancesAdapter)
                } catch (ex: Exception) {

                }

            }
        }
    }

    // Constructor
    inner class MyBalancesAdapter(private val values: ArrayList<DealBalances>?, var context: Context) : RecyclerView.Adapter<MyBalancesAdapter.ViewHolder>() {

        inner class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout){
            private var view: View = layout
            var view_type: Int = 0
            var Period_Name: TextView
            var StartBalance_Value: TextView
            var Consumption_Value: TextView
            var Debit_Value: TextView
            var Credit_Value: TextView
            var EndBalance_Value: TextView

            init {
                Period_Name = layout.findViewById<View>(R.id.Period_Name) as TextView
                StartBalance_Value = layout.findViewById<View>(R.id.StartBalance_Value) as TextView
                Consumption_Value = layout.findViewById<View>(R.id.Consumption_Value) as TextView
                Debit_Value = layout.findViewById<View>(R.id.Debit_Value) as TextView
                Credit_Value = layout.findViewById<View>(R.id.Credit_Value) as TextView
                EndBalance_Value = layout.findViewById<View>(R.id.EndBalance_Value) as TextView
            }

        }

        // Create new views (invoked by the layout manager) and inflates
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBalancesAdapter.ViewHolder {
            // create a new view
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.list_balances, parent, false)
            return ViewHolder(v)
        }

        // Binding items to the view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val DealBalances = values!![position]
            holder.Period_Name.setText(DealBalances.Period_Name)
            holder.StartBalance_Value.setText("%.1f".format((DealBalances.StartBalance_Value)!!.toFloat()))
            holder.Consumption_Value.setText("%.1f".format((DealBalances.Consumption_Value)!!.toFloat()))
            holder.Debit_Value.setText("%.1f".format((DealBalances.Debit_Value)!!.toFloat()))
            holder.Credit_Value.setText("%.1f".format((DealBalances.Credit_Value)!!.toFloat()))
            holder.EndBalance_Value.setText("%.1f".format((DealBalances.EndBalance_Value)!!.toFloat()))

        }

        // get item count returns the list item count
        override fun getItemCount(): Int {
            return values!!.size
        }

    }
    //Закрытие для Balances


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_card, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            when(position){
                0 -> {
                    return HomeActivity()
                }
                1 -> {
                    return PointsActivity()
                }
                2 -> {
                    return TrafficActivity()
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position){
                0 ->  return "Home"
                1 ->  return "Points"
                2 ->  return "Traffic"
            }
            return null
        }
    }
}
