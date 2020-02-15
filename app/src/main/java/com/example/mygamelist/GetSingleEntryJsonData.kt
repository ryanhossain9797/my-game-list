package com.example.mygamelist


import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

interface OnSingleDataAvailableRecepient{
    fun onSingleDataAvailable(data: EntryModel)
    fun onError(err: JSONException)
}

class GetSingleEntryJsonData(private val listener:
                           OnSingleDataAvailableRecepient):AsyncTask<String,Void,EntryModel>() {
    private val TAG = "GetSingleEntryJs"

    override fun doInBackground(vararg params: String): EntryModel{
        Log.d(TAG,"doInBackground starts")
        var entry = EntryModel("","","","")
        try{
            val jsonData = JSONObject(params[0])
                val photoJSON = jsonData.getJSONObject("article")
                val _id = photoJSON.getString("_id")
                val title = photoJSON.getString("title")
                val content = photoJSON.getString("content")
                val imgurl = photoJSON.getString("imgurl")


                entry = EntryModel(_id, title, content, imgurl)
                Log.d(TAG,"doInBackground $entry")
        }
        catch(err: JSONException){
            Log.d(TAG,"doInBackground error parsing json ${err.message}")

            //-------------Stop async task on error, so onPostExecute isn't called
            cancel(true)
            listener.onError(err)
        }
        return entry
    }

    override fun onPostExecute(result: EntryModel) {
        Log.d(TAG,"onPostExecute: starts")
        listener.onSingleDataAvailable(result)
        Log.d(TAG,"onPostExecute: finished")
    }
}
