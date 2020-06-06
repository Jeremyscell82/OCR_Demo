package com.jl_demo.ocr_demo

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    var mSelectedImage: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        find_text.isEnabled = false
        take_pic.setOnClickListener {
            dispatchTakePictureIntent()
        }

        find_text.setOnClickListener {
            runTextRecognition()
        }

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun runTextRecognition() {
        if (mSelectedImage != null){
            val image = InputImage.fromBitmap(mSelectedImage!!, 0)
            val recognizer = TextRecognition.getClient()
            find_text.isEnabled = false
            recognizer.process(image)
                .addOnSuccessListener { texts ->
                    find_text.isEnabled = true
                    results.text = texts.text
                }
                .addOnFailureListener { e -> // Task failed with an exception
                    find_text.isEnabled = true
                    e.printStackTrace()
                }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            find_text.isEnabled = true
            mSelectedImage = data?.extras?.get("data") as Bitmap
            image_view.setImageBitmap(mSelectedImage)
        }
    }
}