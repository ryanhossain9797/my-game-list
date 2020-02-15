package com.example.mygamelist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.content_details.*
import org.json.JSONException

class DetailsActivity : AppCompatActivity(),OnSingleDataAvailableRecepient,OnDownloadCompleteRecepient{

    private val TAG = "DetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val rawData = GetRawData(this)
        val _id = intent.getStringExtra("_id")
        val uri = createUri("http://118.179.70.140:3693/articles/$_id")
        rawData.execute(uri)
    }

    //----------Create a url
    //---------------------------------------------------------------------
    private fun createUri(baseUri:String):String{
        Log.d(TAG,"createUri: start")
        return Uri.parse(baseUri)
            .buildUpon()
            .build().toString()
    }
    //---------------------------------------------------------------------




    //-----------When Raw data download completes
    //---------------------------------------------------------------------
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if(status == DownloadStatus.OK){
            Log.d(TAG,"onDownloadComplete: got raw data ")
            val jsonData = GetSingleEntryJsonData(this)
            jsonData.execute(data)
        }
        else{
            Log.e(TAG,"onDownloadComplete: download failed")
        }
    }
    //---------------------------------------------------------------------




    //-----------When we get JSON from Raw data
    //---------------------------------------------------------------------
    override fun onSingleDataAvailable(data: EntryModel) {
        Log.d(TAG,"onDataAvailable: started with ${data.title} entry")
        title_details.text = data.title
        content_details.text = data.content
        Picasso.get()
            .load(data.imgurl)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(game_photo_details)
    }
    //-----------When getting JSON from data has error
    override fun onError(err: JSONException) {
        Log.e(TAG,"onError: ${err.message}")
    }
    //---------------------------------------------------------------------


}
