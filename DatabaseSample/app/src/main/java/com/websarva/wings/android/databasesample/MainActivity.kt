package com.websarva.wings.android.databasesample

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.ListView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.AdapterView

class MainActivity : AppCompatActivity() {
    private var _cocktailId = -1
    private var _cocktailName = ""
    // データベースヘルパーオブジェクト
    private val _helper = DatabaseHelper(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lvCocktail = findViewById<ListView>(R.id.lvCocktail)
        lvCocktail.onItemClickListener = ListItemClickListener()

        val btnSave = findViewById<View>(R.id.btnSave)
    }

    override fun onDestroy() {
        _helper.close()
        super.onDestroy()
    }

    fun onSaveButtonClick(view: View) {
        val etNote = findViewById<EditText>(R.id.etNote)
        //入力された感想を取得
        val note = etNote.text.toString()

        //データベースヘルパーオブジェクトからデータベース接続オブジェクトを取得
        val db = _helper.writableDatabase

        //まず、リストで選択されたカクテルの目もデータを削除。その後インサートを行う
        val sqlDelete = "DELETE FROM cocktailmemos WHERE _id = ?"
        var stmt = db.compileStatement(sqlDelete)
        stmt.bindLong(1, _cocktailId.toLong())
        stmt.executeUpdateDelete()

        val sqlInsert = "INSERT INTO cocktailmemos (_id, name, note) VALUES (?, ?, ?)"
        stmt = db.compileStatement(sqlInsert)
        stmt.bindLong(1, _cocktailId.toLong())
        stmt.bindString(2, _cocktailName)
        stmt.bindString(3, note)
        stmt.executeInsert()

        etNote.setText("")
        val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
        tvCocktailName.text = getString(R.string.tv_name)
        val btnSave = findViewById<View>(R.id.btnSave)
        btnSave.isEnabled = false
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            //タップされた行番号をプロパティの主キーIDに代入
            _cocktailId = position
            //タップされた行のデータを取得。これがカクテル名となるので、プロパティに代入
            _cocktailName = parent.getItemAtPosition(position) as String
            //カクテル名を表示するTextViewを取得
            val tvCocktailName = findViewById<TextView>(R.id.tvCocktailName)
            //カクテル名を表示するTextViewに表示カクテル名を設定。
            tvCocktailName.text = _cocktailName
            //保存ボタンを取得
            val btnSave = findViewById<View>(R.id.btnSave)
            //保存ボタンをタップできるように設定
            btnSave.isEnabled = true

            val db = _helper.writableDatabase
            val sql = "SELECT * FROM cocktailmemos WHERE _id = ${_cocktailId}"
            val cursor = db.rawQuery(sql, null)
            var note = ""
            while (cursor.moveToNext()) {
                val idxNote = cursor.getColumnIndex("note")
                note = cursor.getString(idxNote)
            }
            val etNote = findViewById<EditText>(R.id.etNote)
            etNote.setText(note)
        }
    }
}