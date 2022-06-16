package com.example.spotifiubyfy01

import android.content.ContentValues
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.spotifiubyfy01.databinding.ActivityLocationSelectionBinding
import java.util.HashMap

class LocationSelection : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationSelectionBinding
    private var currentLtd : Int = -34
    private var currentLng : Int = 151

    override fun onCreate(savedInstanceState: Bundle?) {

        val app = (this.application as Spotifiubify)


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
        val url = "https://spotifiubyfy-users.herokuapp.com/users/location/${this.currentLng}/${this.currentLtd}"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(applicationContext, "Location modified",
                Toast.LENGTH_SHORT).show()
                var nextPage = Intent(this, ProfileEditPage::class.java)
                if (intent.getStringExtra("nextPage") == "genres") {
                    nextPage = Intent(this, PreferencesSelection::class.java)

                }
                startActivity(nextPage)
            },
            { errorResponse -> val intent = Intent(this, PopUpWindow::class.java).apply {
                Log.d(ContentValues.TAG, "ERROR: $errorResponse")
                val error = errorResponse.networkResponse.data.decodeToString().split('"')[3]
                putExtra("popuptext", error)
                putExtra("tokenValidation", true) }
                startActivity(intent)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Authorization"] = "Bearer " + getSharedPreferences(getString(R.string.token_key), MODE_PRIVATE).getString(getString(R.string.token_key), null)
                return params
            }
        }
        MyRequestQueue.getInstance(this).addToRequestQueue(postRequest)

    }

    public fun skip(view : View) {
        val intent = Intent(this, ProfileEditPage::class.java)
        startActivity(intent)
    }
}