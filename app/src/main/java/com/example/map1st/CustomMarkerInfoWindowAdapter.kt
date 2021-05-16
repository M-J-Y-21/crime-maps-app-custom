package com.example.map1st

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


/**
 * Created by User on 10/2/2017.
 */
class CustomMarkerInfoWindowAdapter(private val mContext: Context) : InfoWindowAdapter {
    private val mWindow: View
    private fun rendowWindowText(marker: Marker, view: View) {
        val title = marker.title
        val tvTitle = view.findViewById<View>(R.id.title) as TextView
        if (title != "") {
            tvTitle.text = title
        }
        val details = marker.snippet
        val tvDetails = view.findViewById<View>(R.id.details) as TextView
        if (details != "") {
            tvDetails.text = details
        }
    }

    private fun rendowWindowPicture(marker: Marker, view: View) {
        val crimePicture = view.findViewById<View>(R.id.crimePicture) as ImageView
        if (marker.snippet != null) {
            marker.snippet += crimePicture
        }
    }

    override fun getInfoWindow(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    init {
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.marker_info_window, null)
    }
}