package ru.finfly.myapplication

import android.os.Bundle
import android.util.Log

class ChooseGroupActivity:  ChooseHierarchyItemActivity(),
                            NodeSelectionFragment.OnNodeActionListener
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "group.lua%200")
                }
            })
            commit()
        }
    }

    override fun onExpandNode(key: String, name: String) {
        fragmentManager.beginTransaction().apply {
            add(R.id.frame, NodeSelectionFragment().apply {
                arguments = Bundle().apply {
                    putString("request", "group.lua%20" + key)
                }
            })
            addToBackStack(null)
            commit()
        }
    }

    override fun onSelectNode(key: String, name: String) {
        Log.d("onExpandGroup", key + " " + name)
    }

}