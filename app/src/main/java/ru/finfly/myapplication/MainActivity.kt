package ru.finfly.myapplication

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.widget.SearchView
import android.view.Menu
import android.view.MenuItem



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp)
        }
        findViewById<NavigationView>(R.id.navigation_view).setNavigationItemSelectedListener(
                NavigationView.OnNavigationItemSelectedListener {
                    when(it.itemId){
                        R.id.choose_category ->onChooseCategory()
                        R.id.choose_group ->onChooseGroup()
                        else -> false
                    }
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(
                    (getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(
                            ComponentName(applicationContext, SendHtmlRequest::class.java)
                    )
            )
            isSubmitButtonEnabled = true
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
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