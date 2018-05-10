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
import android.util.Log
import android.view.*
import android.widget.SearchView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_send_html_request.*

class SendHtmlRequest : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_html_request)
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        
        findViewById<NavigationView>(R.id.navigation_view).setNavigationItemSelectedListener(
                NavigationView.OnNavigationItemSelectedListener { item ->
                    when(item.itemId) {
                        R.id.choose_category -> onChooseCategory()
                        R.id.choose_group -> onChooseGroup()
                        else -> return@OnNavigationItemSelectedListener false
                    }
                }
        )
    }

    override fun onStart()
    {
        super.onStart()

        val text_view = findViewById<TextView>(R.id.editText)

        val queue = Volley.newRequestQueue(this)
        val url = "http://95.165.143.36/finfly/lua.exe?hello.lua"

        val request = StringRequest(Request.Method.GET, url,
                Response.Listener<String> { response -> Log.d("Response", response); text_view.text = response },
                Response.ErrorListener { Log.d("Error", "Error") })

        queue.add(request)

        if(Intent.ACTION_SEARCH.equals(intent.action))
        {
            var query = intent.getStringExtra(SearchManager.QUERY)
            Log.d("Query:", query)
        }
  }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.options_menu, menu)

        var searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView = menu!!.findItem(R.id.search).getActionView() as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()))
        searchView.isSubmitButtonEnabled = true

        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> openNavigationDrawer()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openNavigationDrawer():Boolean
    {
        findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        return true
    }

    private fun onChooseCategory():Boolean
    {
        startActivity(Intent(this, ChooseCategoryActivity::class.java))
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }

    private fun onChooseGroup():Boolean
    {
        startActivity(Intent(this, ChooseGroupActivity::class.java))
        findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        return true
    }
}
