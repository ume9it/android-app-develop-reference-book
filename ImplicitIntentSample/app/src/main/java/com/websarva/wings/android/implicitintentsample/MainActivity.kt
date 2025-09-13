package com.websarva.wings.android.implicitintentsample

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.EditText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.net.URLEncoder
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {

    private var _latitude = 0.0
    private var _longitude = 0.0
    private lateinit var _fusedLocationClient: FusedLocationProviderClient
    private lateinit var _locationRequest: LocationRequest
    //位置情報が変更されたときの処理を行うコールバックオブジェクトプロパティ
    private lateinit var _onUpdateLocation: OnUpdateLocation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
        val builder = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        _locationRequest = builder.build()
        _onUpdateLocation = OnUpdateLocation()
    }

    override fun onResume() {
        super.onResume()

        //位置情報の追跡を開始

        //ACCESS_FINE_LOCATIONとACCESS_COARSE_LOCATIONの許可がおりていないなら・・・
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 許可をACCESS_FINE_LOCATIONとACCESS_COARSE_LOCATIONに設定
            val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                //許可を求めるダイアログを表示。その際、リクエストコードを1000に設定
            ActivityCompat.requestPermissions(this@MainActivity, permissions, 1000)
            return
        }
        _fusedLocationClient.requestLocationUpdates(_locationRequest, _onUpdateLocation, mainLooper)
    }

    override fun onPause() {
        super.onPause()

        //位置情報の追跡を停止
        _fusedLocationClient.removeLocationUpdates(_onUpdateLocation)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //位置情報のパーミッションダイアログでカツ許可を選択したなら
        if (requestCode == 1000 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED &&
            grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        // 位置情報の追跡を開始
        _fusedLocationClient.requestLocationUpdates(_locationRequest, _onUpdateLocation, mainLooper)
    }

    fun onMapSearchButtonClick(view: View) {
        val etSearchWord = findViewById<EditText>(R.id.etSearchWord)
        var searchWord = etSearchWord.text.toString()
        searchWord = URLEncoder.encode(searchWord, "UTF-8")
        val uriStr = "geo:0,0?q=${searchWord}"
        val uri = Uri.parse(uriStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    fun onMapCurrentButtonClick(view: View) {
        val uriStr = "geo:${_latitude},${_longitude}"
        val uri = Uri.parse(uriStr)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private inner class OnUpdateLocation: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location = locationResult.lastLocation
            location?.let {
                _latitude = it.latitude
                _longitude = it.longitude
                val tvLatitude = findViewById<TextView>(R.id.tvLatitude)
                val tvLongitude = findViewById<TextView>(R.id.tvLongitude)
                tvLatitude.text = _latitude.toString()
                tvLongitude.text = _longitude.toString()
            }
        }
    }
}