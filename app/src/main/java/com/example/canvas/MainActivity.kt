package com.example.canvas

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class MainActivity : AppCompatActivity() {

    private lateinit var drawView: DrawView
    private lateinit var colorPickerView: ColorPickerView
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button
    private lateinit var redButton: Button
    private lateinit var greenButton: Button
    private lateinit var blackButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawView = findViewById(R.id.drawView)
        colorPickerView = findViewById(R.id.colorPickerView)
        saveButton = findViewById(R.id.saveButton)
        clearButton = findViewById(R.id.clearButton)
        redButton = findViewById(R.id.redButton)
        greenButton = findViewById(R.id.greenButton)
        blackButton = findViewById(R.id.blackButton)

        colorPickerView.setColorListener(
            ColorEnvelopeListener { envelope: ColorEnvelope, _ ->
                drawView.setColor(envelope.color)
            }
        )

        saveButton.setOnClickListener {
            checkPermissionAndSave()
        }

        clearButton.setOnClickListener {
            drawView.clearCanvas()
        }

        redButton.setOnClickListener {
            drawView.setColor(Color.RED)
        }

        greenButton.setOnClickListener {
            drawView.setColor(Color.GREEN)
        }

        blackButton.setOnClickListener {
            drawView.setColor(Color.BLACK)
        }
    }

    private fun checkPermissionAndSave() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        } else {
            saveCanvasToGallery()
        }
    }

    private fun saveCanvasToGallery() {
        val bitmap = drawView.getBitmap()
        val uri = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "Canvas_${System.currentTimeMillis()}",
            "Canvas drawing"
        )

        if (uri != null) {
            Toast.makeText(this, "Saved to Gallery!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Failed to Save!", Toast.LENGTH_SHORT).show()
        }
    }
}
