package com.websarva.wings.android.fragmentsample

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import android.widget.ListView

class MenuListFragment : Fragment(R.layout.fragment_menu_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lvMenu = view.findViewById<ListView>(R.id.lvMenu)
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
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(menu)
        menuList.add(mutableMapOf("name" to "焼肉定食", "price" to "800円"))
        menuList.add(menu)

        val from = arrayOf("name", "price")
        val to = intArrayOf(android.R.id.text1, android.R.id.text2)
        val adapter =
            SimpleAdapter(activity, menuList,
                android.R.layout.simple_list_item_2, from, to)
        lvMenu.adapter = adapter
        lvMenu.onItemClickListener = ListItemClickListener()
    }

    private inner class ListItemClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val item = parent.getItemAtPosition(position) as MutableMap<String, String>
            val menuName = item["name"]
            val menuPrice = item["price"]

            val bundle = Bundle()
            bundle.putString("menuName", menuName)
            bundle.putString("menuPrice", menuPrice)

            activity?.let {
                val transaction = parentFragmentManager.beginTransaction()
                transaction.setReorderingAllowed(true)
                val fragmentMainContainer = it.findViewById<View>(R.id.fragmentMainContainer)
                if (fragmentMainContainer != null) {
                    transaction.addToBackStack("Only List")
                    transaction.replace(R.id.fragmentMainContainer,
                        MenuThanksFragment::class.java, bundle)
                } else {
                    transaction.replace(R.id.fragmentThanksContainer, MenuThanksFragment::class.java, bundle)
                }
                transaction.commit()
            }
        }
    }
}