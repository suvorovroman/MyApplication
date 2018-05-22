package ru.finfly.myapplication

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.SearchView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.widget.TextView
import com.android.volley.toolbox.JsonArrayRequest

import kotlinx.android.synthetic.main.activity_send_html_request.*
import org.json.JSONArray

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
            holder.itemView.findViewById<TextView>(R.id.textView).text = data.optString(position, "available")
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
