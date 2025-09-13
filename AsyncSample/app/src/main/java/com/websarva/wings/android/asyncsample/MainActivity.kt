package com.websarva.wings.android.asyncsample

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.SocketTimeoutException
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import javax.net.ssl.HttpsURLConnection
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    companion object {
        private const val DEBUG_TAG = "AsyncSample"
        private const val WEATHER_INFO_URL = "https://weather.tsukumijima.net/api/forecast/city/"
        //OpenWeatherMapではAPIキーが必要だがtsukumijimaでは不要なのでコメントアウト
        //private const val APPID=""
    }
    private var _list: MutableList<MutableMap<String, String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _list = createList()
        val lvCityList = findViewById<ListView>(R.id.lvCityList)
        val from = arrayOf("name")
        val to = intArrayOf(android.R.id.text1)
        val adapter =
            SimpleAdapter(this@MainActivity, _list, android.R.layout.simple_list_item_1, from, to)
        lvCityList.adapter = adapter
        lvCityList.onItemClickListener = ListItemClickListener()
    }

    private fun createList(): MutableList<MutableMap<String, String>> {
        var list: MutableList<MutableMap<String, String>> = mutableListOf()
        var city = mutableMapOf("name" to "さいたま", "q" to "110010")
        list.add(city)
        city = mutableMapOf("name" to "千葉", "q" to "120010")
        list.add(city)
        city = mutableMapOf("name" to "東京", "q" to "130010")
        list.add(city)
        city = mutableMapOf("name" to "神奈川", "q" to "140010")
        list.add(city)

        return list
    }

    @UiThread
    private fun receiveWeatherInfo(urlFull: String) {
        //ここに非同期で天気情報を取得する処理を記述する。
        val backgroundReceiver = WeatherInfoBackgroundReceiver(urlFull)
        val executeService = Executors.newSingleThreadExecutor()
        val future = executeService.submit(backgroundReceiver)
        val result = future.get()
        showWeatherInfo(result)
    }

    @UiThread
    private fun showWeatherInfo(result: String) {
        val rootJSON = JSONObject(result)
        val location = rootJSON.getJSONObject("location")
        val cityName = location.getString("city")
        val forecasts = rootJSON.getJSONArray("forecasts")
        val todaysForecast = forecasts.getJSONObject(0)
        val weather = todaysForecast.getString("telop")

        val description = rootJSON.getJSONObject("description")
        val bodyText = description.getString("bodyText")
        val telop = "${cityName}の天気"
        val desc = "現在は${weather}です。\n${bodyText}"

        val tvWeatherTelop = findViewById<TextView>(R.id.tvWeatherTelop)
        val tvWeatherDesc = findViewById<TextView>(R.id.tvWeatherDesc)
        tvWeatherTelop.text = telop
        tvWeatherDesc.text = desc
    }

    private inner class WeatherInfoBackgroundReceiver(url: String): Callable<String> {
        private val _url = url

        @WorkerThread
        override fun call(): String {
            var result = ""
            val url = URL(_url)
            val con = url.openConnection() as HttpsURLConnection
            con.connectTimeout = 1000
            con.readTimeout = 1000
            con.requestMethod = "GET"
            try {
                con.connect()
                //HttpURLConnectionオブジェクトからレスポンスデータを取得
                val stream = con.inputStream
                //レスポンスデータであるInputStreamオブジェクトを文字列に変換
                result = is2String(stream)
                stream.close()
            }
            catch (ex: SocketTimeoutException) {
                Log.w(DEBUG_TAG, "通信タイムアウト", ex)
            }
            con.disconnect()
            return result
        }

        private fun is2String(stream: InputStream): String {
            val sb = StringBuilder()
            val reader = BufferedReader(
                InputStreamReader(stream, StandardCharsets.UTF_8))
            var line = reader.readLine()
            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }
            reader.close()
            return sb.toString()
        }
    }

    private inner class ListItemClickListener: AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = _list.get(position)
            val q = item.get("q")
            q?.let {
                val urlFull = "$WEATHER_INFO_URL$q"
                receiveWeatherInfo(urlFull)
            }
        }
    }
}