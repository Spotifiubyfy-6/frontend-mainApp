package com.example.spotifiubyfy01

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.spotifiubyfy01.databinding.ActivityLocationSelectionBinding
import org.json.JSONObject

class LocationSelection : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationSelectionBinding
    private var currentLtd : Int = -34
    private var currentLng : Int = 151

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Set text to button with current location
        val textView = findViewById<TextView>(R.id.textView)
        textView.setText("Australia, Sydney")

        // onClick, clears map and adds a marker on click location. Updates camera position
        mMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener {
            override fun onMapClick(latlng :LatLng) {
                mMap.clear()
                setCountry(latlng)
                val location = LatLng(latlng.latitude,latlng.longitude)
                mMap.addMarker(MarkerOptions().position(location))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location))

            }
        })
    }
    // receives lat and lng, returns country and city name
    fun setCountry(latlng : LatLng) {
        val gcd = Geocoder(this)
        val addresses: List<Address> = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
        if (addresses.isNotEmpty()) {
            this.currentLng = latlng.longitude.toInt()
            this.currentLtd = latlng.latitude.toInt()
            val countryName: String = addresses[0].getCountryName()
            val textView = findViewById<TextView>(R.id.textView)
            textView.setText(countryName)
        }

    }

    // Go to select Genres activity, passing location as
    public fun confirmLocation(view : View) {
        val url = "https://spotifiubyfy-users.herokuapp.com/users/location/${this.currentLtd}/${this.currentLng}"

        val getRequest = JsonArrayRequest(
            Request.Method.POST,
            url,
            null,
            { response ->
                val intent = Intent(this, PreferencesSelection::class.java).apply{
                    val textView = findViewById<TextView>(R.id.textView)
                    val location = textView.text.toString()
                    putExtra("Location", location)
                }
                startActivity(intent)
            },
            { errorResponse ->
                print(errorResponse)
            })
        MyRequestQueue.getInstance(this).addToRequestQueue(getRequest)

    }
}