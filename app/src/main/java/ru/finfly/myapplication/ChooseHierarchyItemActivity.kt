package ru.finfly.myapplication

import android.content.Context
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

abstract class ChooseHierarchyItemActivity(private val section: String):
                AppCompatActivity(),
                NodeSelectionFragment.OnNodeActionListener
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_hierarchy_item)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        restoreSelection()
        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "$section.lua%200")
                }

            })
            commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onExpandNode(key: String, name: String) {
        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "$section.lua%20$key")
                }
            })
            addToBackStack(null)
            commit()
        }
    }

    private val selection = mutableMapOf<String, String>()

    override fun onSelectNode(key: String, name: String, select: Boolean) {
        if(selection.contains(key))
            selection.remove(key)
        else
            selection[key] = name
    }

    override fun onCheckSelection(key: String): Boolean {
        return selection.contains(key)
    }

    private fun saveSelection(){
        getPreferences(Context.MODE_PRIVATE).edit().apply{
            val set = mutableSetOf<String>()
            for ((key, name) in selection) {
                set.add(key)
                putString("$section/$key", name)
            }
            putStringSet(section, set)
            commit()
        }
    }

    private fun restoreSelection(){
        with(getPreferences(Context.MODE_PRIVATE)){
            val set = getStringSet(section, setOf<String>())
            for(key in set)
                selection[key] = getString("$section/$key", "")

        }
    }

    override fun onPause() {
        super.onPause()
        saveSelection()
    }
}