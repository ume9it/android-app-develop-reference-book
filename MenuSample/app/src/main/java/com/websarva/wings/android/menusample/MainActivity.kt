package com.websarva.wings.android.menusample

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    //リストビューに表示するデータ
    private var _menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
    //SimpleAdapterの第四引数fromに使用するプロパティ
    private val _from = arrayOf("name", "price")
    //SimpleAdapterの第五引数toに使用するプロパティ
    private val _to = intArrayOf(R.id.tvMenuNameRow, R.id.tvMenuPriceRow)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.lvMenu)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //定食メニューListオブジェクトをprivateメソッドを利用して用意し、プロパティに格納。
        _menuList = createTeishokuList()
        //画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)
        //SimpleAdapterを生成
        val adapter = SimpleAdapter(
            this@MainActivity,
            _menuList,
            R.layout.row,
            _from,
            _to
        )
        //アダプタの登録
        lvMenu.adapter = adapter
        //リストタップのリスナクラス登録。
        lvMenu.onItemClickListener = ListItemClickListener()

        registerForContextMenu(lvMenu)
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

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //タップされた行のデータを取得
            //SimpleAdapterでは1行分のデータはMutableMap型
            val item = parent.getItemAtPosition(position) as MutableMap<String, Any>
            //注文処理
            order(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // オプションメニュー用xmlファイルをインフレート
        menuInflater.inflate(R.menu.menu_options_menu_list, menu)
        return true
    }

    private fun createCurryList(): MutableList<MutableMap<String,Any>> {
        // カレーメニューリスト用のListオブジェクトを用意
        val menuList: MutableList<MutableMap<String, Any>> = mutableListOf()
        //「ビーフカレー」のデータを格納するMapオブジェクトの用意とMenuListへのデータ登録。
        var menu = mutableMapOf<String, Any>("name" to "ビーフカレー", "price" to 520, "desc" to "特選スパイスを聞かせ他国産ポーク100％のカレーです。")
        menuList.add(menu)
        menu = mutableMapOf<String, Any>("name" to "ポークカレー", "price" to 520, "desc" to "特選スパイスを聞かせ他国産ポーク100％のカレーです。")
        menuList.add(menu)
        menu = mutableMapOf<String, Any>("name" to "チキンカレー", "price" to 520, "desc" to "特選スパイスを聞かせ他国産ポーク100％のカレーです。")
        menuList.add(menu)
        menu = mutableMapOf<String, Any>("name" to "ドライカレー", "price" to 520, "desc" to "特選スパイスを聞かせ他国産ポーク100％のカレーです。")
        menuList.add(menu)
        menu = mutableMapOf<String, Any>("name" to "グリーンカレー", "price" to 520, "desc" to "特選スパイスを聞かせ他国産ポーク100％のカレーです。")
        menuList.add(menu)
        return menuList
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //戻り値用の変数を初期値trueで用意。
        var returnVal = true
        // 選択されたメニューのIDのR値による処理の分岐
        when(item.itemId) {
            // 定食メニューが選択された場合
            R.id.menuListOptionTeishoku ->
                _menuList = createTeishokuList()
            // カレーメニューが選択された場合
            R.id.menuListOptionCurry ->
                _menuList = createCurryList()
            else ->
                returnVal = super.onOptionsItemSelected(item)
        }
        //画面部品ListViewを取得
        val lvMenu = findViewById<ListView>(R.id.lvMenu)

        //SimpleAdapterを選択されたメニューデータで生成
        val adapter = SimpleAdapter(
            this@MainActivity,
            _menuList,
            R.layout.row,
            _from,
            _to
        )
        //アダプタの登録
        lvMenu.adapter = adapter
        return returnVal
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo
    ) {
        //親クラスの同名メソッドの呼び出し
        super.onCreateContextMenu(menu, v, menuInfo)
        //コンテkストメニュー用xmlファイルをインフレート
        menuInflater.inflate(R.menu.menu_context_menu_list, menu)
        //メニュー表示
        menu.setHeaderTitle(R.string.menu_list_options_header)
    }

    private fun order(menu: MutableMap<String, Any>) {
        //定食名と金額を取得。Mapの値部分がAny型なのでキャストが必要。
        val menuName = menu["name"] as String
        val menuPrice = menu["price"] as Int

        //インテントオブジェクトを生成
        val intent2MenuThanks = Intent(this@MainActivity, MenuThanksActivity::class.java)
        //第二画面に送るデータを格納。
        intent2MenuThanks.putExtra("menuName", menuName)
        //MenuThanksActivityでのデータ受け取りと合わせるために、金額にここで「円」を追加する。
        intent2MenuThanks.putExtra("menuPrice", "${menuPrice}円")
        //第二画面の起動
        startActivity(intent2MenuThanks)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var returnVal = true
        //長押しされたビューに関する情報が格納されたオブジェクトを取得
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        //長押しされたリストのポジションを取得
        val listPosition = info.position
        //ポジションから長押しされたメニュー情報Mapオブジェクトを取得。
        val menu = _menuList[listPosition]

        //選択されたメニューのIDのR値による処理の分岐
        when(item.itemId) {
            R.id.menuListContextDesc ->{
                //メニューの説明文字列を取得
                val desc = menu["desc"] as String
                //トーストで表示
                Toast.makeText(this@MainActivity, desc, Toast.LENGTH_LONG).show()
            }
            R.id.menuListContextOrder -> {
                order(menu)
            }
            else -> {
                returnVal = super.onContextItemSelected(item)
            }
        }
        return returnVal
    }
}