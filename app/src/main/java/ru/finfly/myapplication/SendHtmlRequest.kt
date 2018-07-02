package ru.finfly.myapplication

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ProgressBar
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import android.widget.TextView
import com.android.volley.toolbox.JsonArrayRequest

import org.json.JSONArray
import org.json.JSONObject

class SendHtmlRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_html_request)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = Adapter()
        }
    }

    var data = JSONArray()

    private inner class Adapter:RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
        {
            private val availableStockView = view.findViewById<TextView>(R.id.availableStock)
            private val materialNameView = view.findViewById<TextView>(R.id.materialName)
            private val wholeStockView = view.findViewById<TextView>(R.id.wholeStock)
            private val reservedStockView = view.findViewById<TextView>(R.id.reservedStock)
            private val groupNameView = view.findViewById<TextView>(R.id.groupName)

            fun bind(obj: JSONObject)
            {
                availableStockView.text = obj.getString("available")
                wholeStockView.text = obj.getString("whole")
                reservedStockView.text = obj.getString("reserved")
                materialNameView.text = obj.getJSONObject("material").getString("name")
                groupNameView.text = obj.getJSONObject("group").getString("name")
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return ViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.stock_view_holder,
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

    override fun onStart()
    {
        super.onStart()
        if(Intent.ACTION_SEARCH.equals(intent.action))
        {
            Volley.newRequestQueue(this).add(
                    JsonArrayRequest(
                            com.android.volley.Request.Method.GET,
                            "http://95.165.143.36/finfly/lua.exe?stock.lua%20"+
                                    "\"" + intent.getStringExtra(SearchManager.QUERY) + "\"",
                            null,
                            Response.Listener<JSONArray>
                            {
                                data = it
                                findViewById<RecyclerView>(R.id.recyclerView).adapter.notifyDataSetChanged()
                                findViewById<ProgressBar>(R.id.progressBar).visibility = ProgressBar.INVISIBLE
                            },
                            Response.ErrorListener {
                                findViewById<ProgressBar>(R.id.progressBar).visibility = ProgressBar.INVISIBLE
                                AlertDialog.Builder(this).apply {
                                    setTitle(R.string.title_volley_error)
                                    setMessage(it.toString())
                                    setIcon(R.drawable.ic_error_outline_black_24dp)
                                    setCancelable(false)
                                    setPositiveButton(
                                            R.string.ok_button_label,
                                            {dialog, _ -> dialog.cancel()}
                                    )
                            }.show()
                        }
                )
            )
            findViewById<ProgressBar>(R.id.progressBar).visibility = ProgressBar.VISIBLE
        }
    }

}
