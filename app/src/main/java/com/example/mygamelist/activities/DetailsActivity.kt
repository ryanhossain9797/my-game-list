package com.example.mygamelist.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.StringRequestListener
import com.example.mygamelist.R
import com.example.mygamelist.adapters.SubEntryRecyclerViewAdapter
import com.example.mygamelist.models.EntryModel
import com.example.mygamelist.models.SubEntryModel
import com.example.mygamelist.utility.GetSingleEntryJsonData
import com.example.mygamelist.utility.GetSubEntryListJsonData
import com.example.mygamelist.utility.OnSingleDataAvailableRecepient
import com.example.mygamelist.utility.OnSubDataListAvailableRecepient
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.json.JSONException

class DetailsActivity : AppCompatActivity(),
    OnSingleDataAvailableRecepient,
    OnSubDataListAvailableRecepient {

    private val TAG = "DetailsActivity"

    private var currentEntry = EntryModel("","","No Data Available","https://tada")

    private val detailsRecyclerViewAdapter =
        SubEntryRecyclerViewAdapter(
            currentEntry, ArrayList<SubEntryModel>()
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate:")
        if(savedInstanceState!=null){
            Log.d(TAG,"saved instatnce state exists")
            currentEntry = savedInstanceState.getParcelable("saved")!!
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fab.setOnClickListener { _ ->
            if(currentEntry.title.isNotEmpty()){
                val intent = Intent(this,ReviewActivity::class.java)
                intent.putExtra("title",currentEntry.title)
                intent.putExtra("_id",currentEntry._id)
                startActivity(intent)
            }

        }


        //-----------Configure recyclerview
        recycler_view_details.layoutManager = LinearLayoutManager(this)
//        recycler_view_details.addOnItemTouchListener(RecyclerItemClickListener(this,recycler_view_games,this))
        recycler_view_details.adapter = detailsRecyclerViewAdapter

        val _id = intent.getStringExtra("_id")
        getData(_id)

    }

    override fun onStart() {
        Log.d(TAG,"onStart:")
        super.onStart()
    }

    override fun onResume() {
        Log.d(TAG,"onResume:")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG,"onPause:")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG,"onStop:")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG,"onDestroy:")
        super.onDestroy()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG,"onSavedInstanceState:")
        outState.putParcelable("saved",currentEntry)
        super.onSaveInstanceState(outState)
    }



    fun getData(id: String?){
        AndroidNetworking.get("http://118.179.70.140:3693/articles/$id").build()
            .getAsString(object: StringRequestListener {
                override fun onResponse(response: String) {
                    Log.d(TAG,"onResponse: got raw data in lib $response")
                    val jsonData =
                        GetSingleEntryJsonData(
                            this@DetailsActivity
                        )
                    jsonData.execute(response)
                }

                override fun onError(anError: ANError?) {
                    Log.e(TAG,"onResponse: download failed")
                }
            })
    }

    //----------Create a url
//    //---------------------------------------------------------------------
//    private fun createUri(baseUri:String):String{
//        Log.d(TAG,"createUri: start")
//        return Uri.parse(baseUri)
//            .buildUpon()
//            .build().toString()
//    }
    //---------------------------------------------------------------------


    //----------Create a url
    //---------------------------------------------------------------------
    private fun createUriWithEntryId(baseUri:String, id: String):String{
        Log.d(TAG,"createUri: start")
        return Uri.parse(baseUri)
            .buildUpon()
            .appendQueryParameter("article",id)
            .build()
            .toString()
    }
    //---------------------------------------------------------------------




    //-----------When Raw data download completes
    //---------------------------------------------------------------------
//    override fun onDownloadComplete(data: String, status: DownloadStatus) {
//        if(status == DownloadStatus.OK){
//            Log.d(TAG,"onDownloadComplete: got subentry data")
//            GetSubEntryListJsonData(this).execute(data)
//        }
//        else{
//            Log.e(TAG,"onDownloadComplete: download failed")
//        }
//    }
    //---------------------------------------------------------------------




    //-----------When we get JSON for main entry from Raw data
    //---------------------------------------------------------------------
    override fun onSingleDataAvailable(data: EntryModel) {
        Log.d(TAG,"onSingleDataAvailable: started with ${data.title} entry")
        currentEntry = data
        detailsRecyclerViewAdapter.loadNewHeadData(data)
//        val rawData = GetRawData(this)
//        val uri = createUriWithEntryId("http://118.179.70.140:3693/comments",data._id)
//        rawData.execute(uri)
        AndroidNetworking.get("http://118.179.70.140:3693/comments").addQueryParameter("article",data._id)
            .build()
            .getAsString(object: StringRequestListener {
                override fun onResponse(response: String) {
                    Log.d(TAG,"onResponse: got raw data in lib $response")
                    val jsonData =
                        GetSubEntryListJsonData(
                            this@DetailsActivity
                        )
                    jsonData.execute(response)
                }

                override fun onError(anError: ANError?) {
                    Log.e(TAG,"onResponse: download failed")
                }
            })

    }
    //-----------When getting JSON from data has error
    override fun onError(err: JSONException) {
        Log.e(TAG,"onError: ${err.message}")
    }
    //---------------------------------------------------------------------



    //-----------When we get JSON for main entry from Raw data
    //---------------------------------------------------------------------
    override fun onSubDataListAvailable(data: ArrayList<SubEntryModel>) {
        Log.d(TAG,"onSubDataListAvailable: started with ${data.size} entries")
        detailsRecyclerViewAdapter.loadNewSubEntryData(data)
    }

    override fun onSubError(err: JSONException) {
        Log.e(TAG,"onSubError: ${err.message}")
    }
    //---------------------------------------------------------------------
}

