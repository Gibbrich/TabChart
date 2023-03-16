package com.gibbrich.tabchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gibbrich.tabchart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.chart.data = listOf(1, 0, 5, 3)
    }
}