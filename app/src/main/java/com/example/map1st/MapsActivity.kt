package com.example.map1st

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.Tag
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, AdapterView.OnItemSelectedListener {


    open lateinit var mMap: GoogleMap
    private val TAG = "MapsActivity"
    private val FILENAME = "UserMap.data"

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1
    var selectedPosition = 0

    //    private var getLocation = GetLocation("", LatLng(0.0, 0.0))
    //private lateinit var listMarkerInfo: MutableList<GetLocation> //
    var listMarkerInfo = mutableListOf<GetLocation>()


    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

        } else ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST)
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
                    return
                }
                mMap.isMyLocationEnabled = true

            } else {
                Toast.makeText(this,
                    "User has not granted location access permission",
                    Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps) // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this) // get referance to my ad crime button
        // In kotlin not good practice to use underscores in variable names
        // it does need in java but not kotlin val addCrime = findViewById(R.id.add_crime) as Button In kotlin can immediately call by Id like below


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
            return
        }
        addCrime.setOnClickListener { view ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> // Got last known location. In some rare situations this can be null.
                location?.let { onAddCrime(view, it) }
            }
        }

        val spinner: Spinner =
            crime_spinner // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this,
            R.array.crime_array,
            android.R.layout.simple_spinner_item).also { adapter -> // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) // Apply the adapter to the spinner
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?,
                                            view: View?,
                                            position: Int,
                                            id: Long) { // An item was selected. You can retrieve the selected item using
                    // parent.getItemAtPosition(pos)
                    selectedPosition = position

                    // Getting the selected crime
                    val crimeName = parent?.getItemAtPosition(position).toString()

                    // Showing selected item
                    Toast.makeText(parent?.context,
                        "Selected Crime: $crimeName",
                        Toast.LENGTH_SHORT).show()
                    val spinner: Spinner = crime_spinner
                    spinner.onItemSelectedListener = this
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(parent?.context,
                        "Must Select A Crime To Add",
                        Toast.LENGTH_LONG).show()
                }

            }
        }

        val userMarkersFromFile = deserializeUserMarkers(this)
        listMarkerInfo.addAll(userMarkersFromFile)

        if (listMarkerInfo != null) {
            for (marker in listMarkerInfo) {
                var markerColor = marker.colour
                mMap.addMarker(MarkerOptions().position(marker.coordinates).title(marker.title).draggable(
                    false).icon(BitmapDescriptorFactory.defaultMarker(markerColor)))
            }
        }

        val model = ViewModelProviders.of(this)[MainActivityViewModel::class.java]
        model.getMarkers().observe(this, Observer { markersSnapshot ->
            Log.i(TAG, "Received markers from view model")
            listMarkerInfo.clear()
            listMarkerInfo.addAll(markersSnapshot)

        })


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationAccess() //        val userPos = getLocationAccess() Need to get users position for camera to pan to
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { // TODO: Consider calling
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? -> // Got last known location. In some rare situations this can be null.
            location?.let {
                val mCurrentPlace = LatLng(location.latitude, location.longitude)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))

                mMap.setInfoWindowAdapter(CustomMarkerInfoWindowAdapter(this))
            }

        }


    }


    fun onAddCrime(view: View, lastLocation: Location) {
        val closeToJozi = LatLng(55.0, 1.0)
        val mCurrentPlace = LatLng(lastLocation.latitude, lastLocation.longitude)


        when (selectedPosition) {
            0 -> {
                Toast.makeText(this,
                    "You Must Select A Valid Crime To Add!",
                    Toast.LENGTH_LONG).show()
                confirmCrime.setOnClickListener {
                    Toast.makeText(this,
                        "Can't confirm before you select a crime.",
                        Toast.LENGTH_LONG).show()
                }
            }

            1 -> {
                val marker =
                    mMap.addMarker(MarkerOptions().position(mCurrentPlace).title("Drag Murder Marker to Crime Destination!").draggable(
                        true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))
                val markerobj = GetLocation("Murder Crime Destination",
                    marker.position,
                    BitmapDescriptorFactory.HUE_RED)
                Toast.makeText(this,
                    "Drag Murder Marker to Crime Destination!",
                    Toast.LENGTH_LONG).show()
                confirmCrime.setOnClickListener {
                    marker.setDraggable(false)
                    marker.title = "Murder Crime Destination"
                    markerobj.title = marker.title
                    markerobj.coordinates = marker.position
                    listMarkerInfo.add(markerobj)
                    serializeUserMarkers(this, listMarkerInfo)

                    //                    serializeUserMarkers(this, listMarkerInfo)
                    val intent = Intent(this, NewDatePicker1::class.java)
                    startActivity(intent)

                }
            }
            2 -> {
                val marker =
                    mMap.addMarker(MarkerOptions().position(mCurrentPlace).title("Drag Robbery Marker to Crime Destination!").draggable(
                        true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))
                Toast.makeText(this,
                    "Drag Robbery Marker to Crime Destination!",
                    Toast.LENGTH_LONG).show() //                confirmCrime.setOnClickListener {
                //                    marker.setDraggable(false)
                //                    marker.setTitle("Robbery Crime Destination")
                //                    getLocation.title = marker.title
                //                    getLocation.coordinates = marker.position
                //                    listMarkerInfo.add(getLocation)
                //                    Log.e("DebugTag", "New content: " + listMarkerInfo.toString())
                //                    val intent = Intent(this, NewDatePicker1::class.java)
                //                    startActivity(intent)
                //                }
            }
            3 -> {
                val marker =
                    mMap.addMarker(MarkerOptions().position(mCurrentPlace).title("Drag Hijacking Marker to Crime Destination!").draggable(
                        true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))
                Toast.makeText(this,
                    "Drag Hijacking Marker to Crime Destination!",
                    Toast.LENGTH_LONG).show() //                confirmCrime.setOnClickListener {
                //                    marker.setDraggable(false)
                //                    marker.setTitle("Hijacking Crime Destination")
                ////                    getLocation.title = marker.title
                ////                    getLocation.coordinates = marker.position
                ////                    listMarkerInfo.add(getLocation)
                ////                    Log.e("DebugTag", "New content: " + listMarkerInfo.toString())
                ////                    val intent = Intent(this, NewDatePicker1::class.java)
                ////                    startActivity(intent)
                ////                }
            }
            4 -> {
                val marker =
                    mMap.addMarker(MarkerOptions().position(mCurrentPlace).title("Drag Trespassing Marker to Crime Destination!").draggable(
                        true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))
                Toast.makeText(this,
                    "Drag Trespassing Marker to Crime Destination!",
                    Toast.LENGTH_LONG).show() //                confirmCrime.setOnClickListener {
                //                    marker.setDraggable(false)
                //                    marker.setTitle("Trespassing Crime Destination")
                //                    getLocation.title = marker.title
                //                    getLocation.coordinates = marker.position
                //                    listMarkerInfo.add(getLocation)
                //                    Log.e("DebugTag", "New content: " + listMarkerInfo.toString())
                //                    val intent = Intent(this, NewDatePicker1::class.java)
                //                    startActivity(intent)
                //                }
            }
            5 -> {
                val marker =
                    mMap.addMarker(MarkerOptions().position(mCurrentPlace).title("Drag Literring Marker to Crime Destination!").draggable(
                        true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPlace, 15f))
                Toast.makeText(this,
                    "Drag Literring Marker to Crime Destination!",
                    Toast.LENGTH_LONG).show() //                confirmCrime.setOnClickListener {
                //                    marker.setDraggable(false)
                //                    marker.setTitle("Literring Crime Destination")
                //                    getLocation.title = marker.title
                //                    getLocation.coordinates = marker.position
                //                    listMarkerInfo.add(getLocation)
                //                    Log.e("DebugTag", "New content: " + listMarkerInfo.toString())
                //                    val intent = Intent(this, NewDatePicker1::class.java)
                //                    startActivity(intent)
                //                }

            }
        }


    }


    private fun serializeUserMarkers(context: Context, userMarkers: List<GetLocation>) {
        Log.i(TAG, "SerializeUserMapWithMarkers")
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use {
            it.writeObject(userMarkers)

        }
    }

    private fun deserializeUserMarkers(context: Context): List<GetLocation> {
        Log.i(TAG, "DeSerializeMapWithMarkers")
        val dataFile = getDataFile(context)
        if (!dataFile.exists()) {
            Log.i(TAG, "Data File Does not exist yet")
            return emptyList()
        }

        ObjectInputStream(FileInputStream(dataFile)).use { return it.readObject() as List<GetLocation> }

    }

    private fun getDataFile(context: Context): File {
        Log.i(TAG, "Getting file from directory ${context.filesDir}")
        return File(context.filesDir, FILENAME)
    }


    override fun onItemSelected(parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long) { // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        selectedPosition = position // Getting the selected crime
        val crimeName = parent?.getItemAtPosition(position).toString()

        // Showing selected item
        Toast.makeText(parent?.context, "Selected Crime: $crimeName", Toast.LENGTH_SHORT).show()
        val spinner: Spinner = crime_spinner
        spinner.onItemSelectedListener = this


    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(parent?.context, "Must Select A Crime To Add", Toast.LENGTH_LONG).show()
    }


}

class GetLocation(var title: String, var coordinates: LatLng, var colour: Float) : Serializable {}

