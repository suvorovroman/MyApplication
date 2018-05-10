package ru.finfly.myapplication

import android.app.AlertDialog
import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.android.volley.Response
import org.json.JSONArray
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest

open class ChooseHierarchyItemActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_hierarchy_item)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

}