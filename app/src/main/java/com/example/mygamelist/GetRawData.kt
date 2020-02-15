package com.example.mygamelist

import android.os.AsyncTask
import android.util.Log
import android.view.View
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

interface OnDownloadCompleteRecepient{
    fun onDownloadComplete(data: String,status: DownloadStatus)
}

class GetRawData(private val listener: OnDownloadCompleteRecepient) : AsyncTask<String, Void, String>(){
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE
    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG,"doInBackground: called")
        if(params[0] == null){
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No url specified"
        }
        try{
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        }
        catch(e: Exception){
            val errorMessage = when(e){
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IOException reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Permission needed? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: unknown error ${e.message}"
                }
            }
            Log.e(TAG, errorMessage)
            return errorMessage
        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG,"onPostExecute: received $result")
        listener.onDownloadComplete(result,downloadStatus)
    }
}