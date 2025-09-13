package com.websarva.wings.android.cameraintentsample

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ImageView
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    // カメラアクティビティを起動するためのランチャーオブジェクト
    private val _cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallbackFromCamera())

    private var _imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ivCamera)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun onCameraImageClick(view: View) {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val now = Date()
        val nowStr = dateFormat.format(now)
        val fileName = "CameraIntentSamplePhoto_${nowStr}.jpg"

        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, fileName)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        _imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri)
        _cameraLauncher.launch(intent)
    }

    private inner class ActivityResultCallbackFromCamera : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult) {
            if (result.resultCode == RESULT_OK) {
//                val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                    result.data?.getParcelableExtra("data", Bitmap::class.java)
//                } else {
//                    result.data?.getParcelableExtra<Bitmap>("data")
//                }
//                val ivCamera = findViewById<ImageView>(R.id.ivCamera)
//                ivCamera.setImageBitmap(bitmap)
                val ivCamera = findViewById<ImageView>(R.id.ivCamera)
                ivCamera.setImageURI(_imageUri)
            }
        }
    }
}