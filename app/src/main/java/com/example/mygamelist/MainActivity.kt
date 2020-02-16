package com.example.mygamelist

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.StringRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.json.JSONException

class MainActivity : AppCompatActivity(),OnDataListAvailableRecepient,OnRecyclerClickListener {

    private var TAG = "MainActivity"


    //---------Adapter for recycler view, required globally to load new data into recyclerview
    val gameRecyclerViewAdapter = EntryRecyclerViewAdapter(ArrayList<EntryModel>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        AndroidNetworking.initialize(applicationContext)

        //-----------Configure recyclerview
        recycler_view_games.layoutManager = LinearLayoutManager(this)
        recycler_view_games.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view_games,this))
        recycler_view_games.adapter = gameRecyclerViewAdapter


        //start fetching data, retrieved in onDownloadComplete method below
        //val rawData = GetRawData(this)
        //val url = createUri("http://118.179.70.140:3693/articles")
        //rawData.execute(url)

        AndroidNetworking.get("http://118.179.70.140:3693/articles").build()
            .getAsString(object: StringRequestListener{
                override fun onResponse(response: String) {
                    Log.d(TAG,"onResponse: got raw data in lib $response")
                    val jsonData = GetEntryListJsonData(this@MainActivity)
                    jsonData.execute(response)
                }

                override fun onError(anError: ANError?) {
                    Log.e(TAG,"onResponse: download failed")
                }
            })


    }



    //-----------Making the options menu
    //---------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    //-----------handling taps on specific items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    //---------------------------------------------------------------------




    //----------Create a url
    //---------------------------------------------------------------------
//    private fun createUri(baseUri:String):String{
//        Log.d(TAG,"createUri: start")
//        return Uri.parse(baseUri)
//            .buildUpon()
//            .build().toString()
//    }
    //---------------------------------------------------------------------




    //-----------When Raw data download completes
    //---------------------------------------------------------------------
//    override fun onDownloadComplete(data: String, status: DownloadStatus) {
//        if(status == DownloadStatus.OK){
//            Log.d(TAG,"onDownloadComplete: got raw data ")
//            val jsonData = GetEntryListJsonData(this)
//            jsonData.execute(data)
//        }
//        else{
//            Log.e(TAG,"onDownloadComplete: download failed")
//        }
//    }
    //---------------------------------------------------------------------




    //-----------When we get JSON from Raw data
    //---------------------------------------------------------------------
    override fun onDataListAvailable(data: ArrayList<EntryModel>) {
        Log.d(TAG,"onDataAvailable: started with ${data.size} entries")
        gameRecyclerViewAdapter.loadNewData(data)
    }
    //-----------When getting JSON from data has error
    override fun onError(err: JSONException) {
        Log.e(TAG,"onError: ${err.message}")
    }
    //---------------------------------------------------------------------




    //-----------Responding to item clicks on items from the list
    //---------------------------------------------------------------------
    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG,"onItemClick: on item $position")
        val game = gameRecyclerViewAdapter.getEntryModel(position)
        if(game!=null){
            val intent = Intent(this,DetailsActivity::class.java)
            intent.putExtra("_id",game._id)
            startActivity(intent)
        }
        else{
            Log.e(TAG,"onItemClick: Item doesn't exist")
        }

    }
    //-----------Long click
    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG,"onItemLongClick: on item $position")
    }
    //---------------------------------------------------------------------

}
