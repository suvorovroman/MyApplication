package ru.finfly.myapplication

import android.app.Activity
import android.app.AlertDialog
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

//
class NodeSelectionFragment:Fragment()
{

    interface OnNodeActionListener
    {
        fun onExpandNode(key: String, name: String)

        fun onSelectNode(key: String, name: String)
    }

    lateinit var onNodeActionListener: OnNodeActionListener;

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        private val nameView = view.findViewById<TextView>(R.id.textView)
        private val keyView = view.findViewById<TextView>(R.id.keyView)
        private val expandButton = view.findViewById<ImageButton>(R.id.expand_item)

        init
        {
            itemView.findViewById<ImageButton>(R.id.expand_item).setOnClickListener {
                onNodeActionListener.onExpandNode(
                        keyView.text as String,
                        nameView.text as String
                )
            }
        }

        fun bind(obj: JSONObject)
        {
            nameView.text = obj.getString("name")
            keyView.text  = obj.getString("key")
            expandButton.visibility =   if(obj.getInt("count") > 0)
                                            ImageButton.VISIBLE
                                        else
                                            ImageButton.INVISIBLE
        }
    }

    var data: JSONArray = JSONArray()

    inner class Adapter:RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                        LayoutInflater.from(parent.context).inflate(
                            R.layout.category_view_holder,
                            parent,
                            false
                        )
                    )
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ViewHolder).bind(data.optJSONObject(position))
        }

        override fun getItemCount() = data.length()
    }

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar

    inner class Request(url:String):JsonArrayRequest(com.android.volley.Request.Method.GET, url, null,
            Response.Listener<JSONArray>
            {
                progressBar.visibility = ProgressBar.INVISIBLE
                data = it
                recyclerView.adapter.notifyDataSetChanged()
            },
            Response.ErrorListener
            {
                progressBar.visibility = ProgressBar.INVISIBLE
                AlertDialog.Builder(activity).apply {
                    setTitle(R.string.title_volley_error)
                    setMessage(it.toString())
                    setIcon(R.drawable.ic_error_outline_black_24dp)
                    setCancelable(false)
                    setPositiveButton(R.string.ok_button_label, {dialog, _ -> dialog.cancel()})
                }.show()
            })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.hierarchy_level, container, false).apply {
            recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
                layoutManager = LinearLayoutManager(context)
                adapter = Adapter()
            }
            progressBar = findViewById<ProgressBar>(R.id.progressBar)
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        Volley.newRequestQueue(activity).add(
                Request(
                        "http://95.165.143.36/finfly/lua.exe?" +
                         arguments.getString("request")
                )
        )
        progressBar.visibility = ProgressBar.VISIBLE
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        onNodeActionListener = activity as OnNodeActionListener
    }

}