package com.example.mygamelist.utility

import android.os.AsyncTask
import android.util.Log
import com.example.mygamelist.models.EntryModel
import org.json.JSONException
import org.json.JSONObject

interface OnDataListAvailableRecepient{
    fun onDataListAvailable(data: ArrayList<EntryModel>)
    fun onError(err: JSONException)
}

class GetEntryListJsonData(private val listener:
                           OnDataListAvailableRecepient
):AsyncTask<String,Void,ArrayList<EntryModel>>() {
    private val TAG = "GetFlickrJsonData"

    override fun doInBackground(vararg params: String): ArrayList<EntryModel> {
        Log.d(TAG,"doInBackground starts")
        val entryList = ArrayList<EntryModel>();
        try{
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("articles")
            for(i in 0 until itemsArray.length()){
                val entryJSON = itemsArray.getJSONObject(i)
                val _id = entryJSON.getString("_id")
                val title = entryJSON.getString("title")
                val content = entryJSON.getString("content")
                val imgurl = entryJSON.getString("imgurl")


                val entry = EntryModel(
                    _id,
                    title,
                    content,
                    imgurl
                )

                entryList.add(entry)
                Log.d(TAG,"doInBackground $entry")
            }
        }
        catch(err: JSONException){
            Log.d(TAG,"doInBackground error parsing json ${err.message}")

            //-------------Stop async task on error, so onPostExecute isn't called
            cancel(true)
            listener.onError(err)
        }
        return entryList
    }

    override fun onPostExecute(result: ArrayList<EntryModel>) {
        Log.d(TAG,"onPostExecute: starts")
        listener.onDataListAvailable(result)
        Log.d(TAG,"onPostExecute: finished")
    }
}