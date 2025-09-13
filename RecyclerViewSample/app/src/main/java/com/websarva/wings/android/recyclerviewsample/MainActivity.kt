package com.websarva.wings.android.recyclerviewsample

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlin.collections.get
import kotlin.text.get

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setLogo(R.mipmap.ic_launcher)
        setSupportActionBar(toolbar)
        val toolbarLayout = findViewById<CollapsingToolbarLayout>(R.id.toolbarLayout)
        toolbarLayout.title = getString(R.string.toolbar_title)
        //通常サイズ時の文字色を指定
        toolbarLayout.setExpandedTitleColor(Color.WHITE)
        //縮小サイズ時の文字色を指定
        toolbarLayout.setCollapsedTitleTextColor(Color.LTGRAY)

        val rvMenu = findViewById<RecyclerView>(R.id.rvMenu)
        val layout = LinearLayoutManager(this@MainActivity)
        rvMenu.layoutManager = layout
        val menuList = createTeishokuList()
        val adapter = RecyclerListAdapter(menuList)
        rvMenu.adapter = adapter

        //区切り線専用のオブジェクトを生成
        val decorator = DividerItemDecoration(this@MainActivity, layout.orientation)
        //RecyclerViewに区切り線用のオブジェクトを設定
        rvMenu.addItemDecoration(decorator)
    }

    private fun createTeishokuList(): MutableList<MutableMap<String, Any>> {
        //定食メニューリスト用のListオブジェクトを用意。
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        //「唐揚げ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        var menu = mutableMapOf<String, Any>(
            "name" to "唐揚げ定食",
            "price" to 800,
            "desc" to "若鶏の唐揚げにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        //「ハンバーグ定食」のデータを格納するMapオブジェクトの用意とmenuListへのデータ登録。
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        menu = mutableMapOf(
            "name" to "ハンバーグ定食",
            "price" to 850,
            "desc" to "手ごねハンバーグにサラダ、ご飯とお味噌汁が付きます。"
        )
        menuList.add(menu)
        return menuList
    }

    private inner class RecyclerListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //リスト1行分中でメニュー名を表示する画面部品
        var _tvMenuNameRow: TextView
        //リスト1行分中でメニュー金額を表示する画面部品
        var _tvMenuPriceRow: TextView
        init {
            //引数で渡されたリスト1行分の画面部品中から表示に使われるTextViewを取得
            _tvMenuNameRow = itemView.findViewById(R.id.tvMenuNameRow)
            _tvMenuPriceRow = itemView.findViewById(R.id.tvMenuPriceRow)
        }
    }

    private inner class RecyclerListAdapter(private val _listData: MutableList<MutableMap<String, Any>>) : RecyclerView.Adapter<RecyclerListViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerListViewHolder {
            //レイアウトインフレータを取得
            val inflater = LayoutInflater.from(this@MainActivity)
            //row.xmlをインフレ―トし、1行分の画面部品とする
            val view = inflater.inflate(R.layout.row, parent, false)
            view.setOnClickListener(ItemClickListener())
            //ビューホルダーオブジェクトを作成
            val holder = RecyclerListViewHolder(view)
            //生成したビューホルダーをリターン
            return holder
        }

        override fun onBindViewHolder(holder: RecyclerListViewHolder, position: Int) {
            val item = _listData[position]
            val menuName = item["name"] as String
            val menuPrice = item["price"] as Int
            val menuPriceStr = menuPrice.toString()
            holder._tvMenuNameRow.text = menuName
            holder._tvMenuPriceRow.text = menuPriceStr
        }

        override fun getItemCount(): Int {
            //リストデータ中の件数をリターン
            return _listData.size
        }
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            val tvMenuName = view.findViewById<TextView>(R.id.tvMenuNameRow)
            val menuName = tvMenuName.text.toString()
            val msg = getString(R.string.msg_header) + menuName
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
        }
    }
}