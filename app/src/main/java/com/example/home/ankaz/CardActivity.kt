package com.example.home.ankaz

import android.os.AsyncTask
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agreement.*

import kotlinx.android.synthetic.main.activity_card.*
import java.io.PrintWriter
import java.io.StringWriter

class CardActivity : AppCompatActivity() {

    companion object {
        const val DEAL_NUM = "0"
    }

    private var itemArrayList: ArrayList<Deal>? = null  //List items Array
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

        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        dealN = intent.getStringExtra(AgreementActivity.DEAL_NUM)
        //Subj_name.setText(dealN)
        loadData().execute()

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
                Toast.makeText(this@CardActivity, msg, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@CardActivity, msg, Toast.LENGTH_SHORT).show()
            }
        }
    }


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
