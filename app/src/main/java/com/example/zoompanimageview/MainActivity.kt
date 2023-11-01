package com.example.zoompanimageview

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.zoompanimageview.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videos = listOf(
            R.raw.cat1,
            R.raw.cat2,
            R.raw.cat3,
            R.raw.cat4,
        )


        binding.btn.setOnClickListener {
            val uri = Uri.parse("android.resource://" + packageName + "/" + videos.random())
            binding.zoomPanView.setVideo(uri, true)
        }
    }
}