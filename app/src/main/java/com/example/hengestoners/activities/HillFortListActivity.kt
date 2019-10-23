package com.example.hengestoners.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hengestoners.R
import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.adapters.HillFortListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult

class HillFortListActivity : AppCompatActivity(), HillFortListener {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        toolbar.title = title
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //recyclerView.adapter = HillFortAdapter(app.hillForts.findAll(), this)
        loadHillForts()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add -> startActivityForResult<HillFortActivity>(0)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onHillFortClick(hillFort: HillFortModel) {
        startActivityForResult(intentFor<HillFortActivity>().putExtra("hillFort_edit", hillFort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //recyclerView.adapter?.notifyDataSetChanged()
        loadHillForts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillForts() {
        showHillForts(app.hillForts.findAll())
    }

    fun showHillForts(hillForts: List<HillFortModel>){
        recyclerView.adapter = HillFortAdapter(hillForts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}



