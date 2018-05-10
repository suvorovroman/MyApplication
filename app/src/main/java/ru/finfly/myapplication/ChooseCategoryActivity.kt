package ru.finfly.myapplication

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ChooseCategoryActivity:   ChooseHierarchyItemActivity(),
                                NodeSelectionFragment.OnNodeActionListener
{


    class Item(key: String, name: String, count: Int) {
        public val Key = key
        public val Name = name
        public val Count = count
    }

    val Path = mutableListOf<Item>()

    inner class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {
        init {
            retrieve()
        }

        private var data = JSONArray()

        fun retrieve() {
            val queue = Volley.newRequestQueue(this@ChooseCategoryActivity.applicationContext)
            val url = "http://95.165.143.36/finfly/lua.exe?category.lua%20" +
                    if (Path.isEmpty()) "0" else Path.last().Key
            val request = JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
                    Response.Listener<JSONArray> { response ->
                        data = response;
                        notifyDataSetChanged()
                    },
                    Response.ErrorListener { error ->
                        Log.d("Error", error.toString())
                    }
            )
            queue.add(request)

        }

        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            init {
                view.findViewById<ImageButton>(R.id.expand_item).setOnClickListener(
                        View.OnClickListener {
                            Path.add(Item(Category.getString("key"), Category.getString("name"), Category.getInt("count")))
                            retrieve()
                            notifyDataSetChanged()

                        }
                )
            }

            var Category: JSONObject = JSONObject()

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.category_view_holder, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = data.length()

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.Category = data.optJSONObject(position)
                holder.view.findViewById<TextView>(R.id.textView).text =
                        holder.Category.getString("name") + "(" +
                        holder.Category.getString("count") + ")"
            if (holder.Category.getInt("count") > 0)
                holder.view.findViewById<ImageButton>(R.id.expand_item).visibility = View.VISIBLE
            else
                holder.view.findViewById<ImageButton>(R.id.expand_item).visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        if (Path.size > 0) {
            val adapter = findViewById<RecyclerView>(R.id.recyclerView).adapter as Adapter
            Path.removeAt(Path.lastIndex)
            adapter.retrieve()
            adapter.notifyDataSetChanged()
        } else
            super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "category.lua%200")
                }

            })
            commit()
        }
    }

    override fun onExpandNode(key: String, name: String) {
        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "category.lua%20" + key)
                }
            })
            addToBackStack(null)
            commit()
        }
    }

    override fun onSelectNode(key: String, name: String) {
        Log.d("onSelectCategory", key + " " + name)
    }
}