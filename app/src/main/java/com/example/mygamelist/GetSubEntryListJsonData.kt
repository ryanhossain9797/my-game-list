package com.example.mygamelist

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject

interface OnSubDataListAvailableRecepient{
    fun onSubDataListAvailable(data: ArrayList<SubEntryModel>)
    fun onSubError(err: JSONException)
}

class GetSubEntryListJsonData(private val listener:
                           OnSubDataListAvailableRecepient):AsyncTask<String,Void,ArrayList<SubEntryModel>>() {
    private val TAG = "GetSubEntryListJson"

    override fun doInBackground(vararg params: String): ArrayList<SubEntryModel> {
//        Log.d(TAG,"doInBackground starts with ${params[0]}")
        val subEntryList = ArrayList<SubEntryModel>();
        try{
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("comments")
            Log.d(TAG,"doInBackground: there are ${itemsArray.length()} comments")
            for(i in 0 until itemsArray.length()){
                val subEntryJSON = itemsArray.getJSONObject(i)
                val _id = subEntryJSON.getString("_id")
                val username= subEntryJSON.getString("username")
                val comment = subEntryJSON.getString("comment")


                val subEntry = SubEntryModel(_id, username, comment)

                subEntryList.add(subEntry)
                Log.d(TAG,"doInBackground ${subEntry.comment}")
            }
        }
        catch(err: JSONException){
            Log.d(TAG,"doInBackground error parsing json ${err.message}")

            //-------------Stop async task on error, so onPostExecute isn't called
            cancel(true)
            listener.onSubError(err)
        }
        return subEntryList
    }

    override fun onPostExecute(result: ArrayList<SubEntryModel>) {
        Log.d(TAG,"onPostExecute: starts")
        listener.onSubDataListAvailable(result)
        Log.d(TAG,"onPostExecute: finished")
    }
}