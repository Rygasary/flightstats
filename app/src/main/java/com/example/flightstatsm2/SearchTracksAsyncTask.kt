package com.example.flightstatsm2

import android.os.AsyncTask
import android.util.Log

class SearchTracksAsyncTask(requestListener: RequestsManager.RequestListener) :
    AsyncTask<SearchTrackDataModel, Void, String>() {

    var mRequestListener: RequestsManager.RequestListener? = null

    init {
        mRequestListener = requestListener
    }

    override fun doInBackground(vararg searchTrackModel: SearchTrackDataModel?): String? {
        val data = searchTrackModel[0]
        val baseUrl: String = "https://opensky-network.org/api/tracks/all"

        val result: String? =
            RequestsManager.get(baseUrl, getRequestParams(searchTrackModel = searchTrackModel[0]))




        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        if (result == null) {
            mRequestListener?.onRequestFailed()
        } else {
            Log.v("RESULT", result)
            mRequestListener?.onRequestSuccess(result)
        }

    }

    private fun getRequestParams(searchTrackModel: SearchTrackDataModel?): Map<String, String>? {
        val params = HashMap<String, String>()
        if (searchTrackModel != null) {
            params["icao24"] = searchTrackModel.icao
            params["time"] = searchTrackModel.time.toString()
        }
        return params
    }
}