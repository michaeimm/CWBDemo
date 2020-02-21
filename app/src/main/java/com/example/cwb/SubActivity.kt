package com.example.cwb

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sub.*

class SubActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        intent.getParcelableExtra<CWBData>("data")?.run {
            tvMain.text = "$startTime\n$endTime\n${parameter?.parameterName?:""}${parameter?.parameterUnit?:""}"
        }

    }
}
