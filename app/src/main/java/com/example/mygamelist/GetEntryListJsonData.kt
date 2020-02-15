package com.example.mygamelist

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

interface OnDataListAvailableRecepient{
    fun onDataListAvailable(data: ArrayList<EntryModel>)
    fun onError(err: JSONException)
}

class GetEntryListJsonData(private val listener:
                        OnDataListAvailableRecepient):AsyncTask<String,Void,ArrayList<EntryModel>>() {
    private val TAG = "GetFlickrJsonData"

    override fun doInBackground(vararg params: String): ArrayList<EntryModel> {
        Log.d(TAG,"doInBackground starts")
        val photoList = ArrayList<EntryModel>();
        try{
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("articles")
            for(i in 0 until itemsArray.length()){
                val photoJSON = itemsArray.getJSONObject(i)
                val _id = photoJSON.getString("_id")
                val title = photoJSON.getString("title")
                val content = photoJSON.getString("content")
                val imgurl = photoJSON.getString("imgurl")


                val photo = EntryModel(_id, title, content, imgurl)

                photoList.add(photo)
                Log.d(TAG,"doInBackground $photo")
            }
        }
        catch(err: JSONException){
            Log.d(TAG,"doInBackground error parsing json ${err.message}")

            //-------------Stop async task on error, so onPostExecute isn't called
            cancel(true)
            listener.onError(err)
        }
        return photoList
    }

    override fun onPostExecute(result: ArrayList<EntryModel>) {
        Log.d(TAG,"onPostExecute: starts")
        listener.onDataListAvailable(result)
        Log.d(TAG,"onPostExecute: finished")
    }
}