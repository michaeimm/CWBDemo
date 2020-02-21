package com.example.cwb

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.edit
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.brotli.BrotliInterceptor
import org.jetbrains.anko.dip
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.net.Proxy


class MainActivity : AppCompatActivity() {

    private var mainScope: CoroutineScope? = MainScope()

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(BrotliInterceptor)
            .connectionSpecs(listOf(ConnectionSpec.COMPATIBLE_TLS))
            .protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
            .proxy(Proxy.NO_PROXY)
            .build()
    }

    private val gson by lazy {
        GsonBuilder().apply {
            registerTypeAdapter(CWBDataTypeAdapter())
            registerTypeAdapter(CWBParameterTypeAdapter())
        }.create()
    }
    private val mainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvList.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = mainAdapter
        }

        val firstStart = getDefaultSharedPreferences(this).getBoolean(FIRST_START, true)
        if(!firstStart){
            toast("歡迎回來")
        } else {
            getDefaultSharedPreferences(this).edit{
                putBoolean(FIRST_START, false)
            }
        }

        mainScope?.launch {
            val data = getData()
            mainAdapter.setData(data)
        }
    }

    private suspend fun getData() = withContext(Dispatchers.IO){
        val request: Request = Request.Builder()
            .url("$API_URL?Authorization=$AUTHORIZATION&elementName=MinT&locationName=$LOCATION")
            .build()

        okHttpClient.newCall(request).execute().use { response -> response.body?.charStream()
            val res = JsonParser.parseReader(JsonReader(response.body?.charStream()))
            res.asJsonObject
                .get("records")
                .asJsonObject
                .get("location")
                .asJsonArray[0]
                .asJsonObject
                .get("weatherElement")
                .asJsonArray[0]
                .asJsonObject
                .get("time")
                .asJsonArray
                .map {
                    gson.fromJson<CWBData>(it)
                }
        }
    }

    override fun onDestroy() {
        mainScope = null
        super.onDestroy()
    }

    inner class MainAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var data : List<CWBData>? = null

        suspend fun setData(data: List<CWBData>) = withContext(Dispatchers.Main){
            this@MainAdapter.data = data
            notifyDataSetChanged()
        }

        override fun getItemViewType(position: Int): Int {
            return position % 2
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0){
                MainDataViewHolder(AppCompatTextView(this@MainActivity).apply{
                    gravity = Gravity.CENTER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dip(200)
                    )
                })
            } else {
                MainImageViewHolder(AppCompatImageView(this@MainActivity).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dip(200)
                    )
                })
            }
        }

        override fun getItemCount(): Int {
            val dataSize = data?.size ?: return 0
            return dataSize * 2 - 1
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (holder is MainDataViewHolder){
                holder.text.apply {
                    val mData = data?.get((position + 1) / 2)
                    text = mData?.run {
                        "$startTime\n$endTime\n${parameter?.parameterName?:""}${parameter?.parameterUnit?:""}"
                    }
                    onClick {
                        startActivity(intentFor<SubActivity>(
                            "data" to mData
                        ))
                    }
                }
            }
        }

    }

    inner class MainDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text = itemView as TextView
    }

    inner class MainImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView as ImageView

        init {
            Glide.with(this@MainActivity)
                .load(R.mipmap.ic_launcher_round)
                .centerInside()
                .into(image)
        }
    }

    companion object{
        private const val AUTHORIZATION = ""
        private const val API_URL = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001"
        private const val LOCATION = "臺北市"
        private const val FIRST_START = "firstStart"
    }
}
