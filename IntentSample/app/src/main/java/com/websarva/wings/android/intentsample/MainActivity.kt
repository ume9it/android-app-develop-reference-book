package com.websarva.wings.android.intentsample

import android.os.Bundle
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.widget.SimpleAdapter
import android.content.Intent
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvMenu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        // SimpleAdapterで使用するmutableListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, String>> = mutableListOf()
        var menu = mutableMapOf("name" to "から揚げ定食", "price" to "850円")
        menuList.add(menu)
        menuList.add(mutableMapOf("name" to "生姜焼き定食", "price" to "800円"))
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        val from = arrayOf("name", "price")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter = SimpleAdapter(this@MainActivity, menuList, android.R.layout.simple_list_item_2, from, to)
        lvMenu.adapter = adapter

        //リストタップのリスナクラス登録
        lvMenu.onItemClickListener = ListItemClickListener()
    }
    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //タップされた行のデータを取得
            //SimpleAdapterでは1行分のデータはMutableMap型
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            //定食名と金額を取得
            val menuName = item["name"]
            val menuPrice = item["price"]
            //インテントオブジェクトを生成
            val intent2MenuThanks = Intent(this@MainActivity, MenuThanksActivity::class.java)
//            //第二画面に送るデータを格納
//            intent2MenuThanks.putExtra("menuName", menuName)
//            intent2MenuThanks.putExtra("menuPrice", menuPrice)
//            //第2画面の起動
//            startActivity(intent2MenuThanks)
            val bundle = Bundle()
            bundle.putString("menuName", menuName)
            bundle.putString("menuPrice", menuPrice)
            intent2MenuThanks.putExtras(bundle)
            startActivity(intent2MenuThanks)
        }
    }
}