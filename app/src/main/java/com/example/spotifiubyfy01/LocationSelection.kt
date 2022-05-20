package com.example.spotifiubyfy01

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.spotifiubyfy01.databinding.ActivityLocationSelectionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*


class LocationSelection : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        // Set text to button with current location
        val button = findViewById<Button>(R.id.nextButton)
        button.setText("Australia, Sydney")

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
            val countryName: String = addresses[0].getCountryName()
            val cityName: String = addresses[0].adminArea
            val button = findViewById<Button>(R.id.nextButton)
            button.setText(countryName + ", " + cityName)
        }

    }
    // Go to select Genres activity, passing location as
    public fun confirmLocation(view : View) {
        val intent = Intent(this, PreferencesSelection::class.java).apply{
            val button = findViewById<Button>(R.id.nextButton)
            val location = button.text.toString()
            putExtra("Location", location)
        }
        startActivity(intent)
    }

}