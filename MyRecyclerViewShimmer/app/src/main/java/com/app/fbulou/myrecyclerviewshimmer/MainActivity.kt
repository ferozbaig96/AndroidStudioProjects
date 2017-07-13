package com.app.fbulou.myrecyclerviewshimmer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.VolleyError
import com.app.fbulou.myrecyclerviewshimmer.adapters.ContentAdapter
import com.app.fbulou.myrecyclerviewshimmer.models.ContentResponse
import com.app.fbulou.volleysimple.ServerCallback
import com.app.fbulou.volleysimple.VolleySimple
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), ServerCallback<Any> {
    val TAG_CONTENT = "contentTag"
    val EMULATOR_LOCALHOST = "10.0.2.2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        init()
        VolleySimple.getInstance(this).placeJsonObjectRequest(
                TAG_CONTENT, "http://$EMULATOR_LOCALHOST:80/contentjson.json", Request.Method.GET, null, null, this)
    }

    private fun init() {
        val adapter: ContentAdapter = ContentAdapter()
        rvContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvContent.adapter = adapter
        rvContent.addItemDecoration(DividerItemDecoration(rvContent.context, LinearLayoutManager.VERTICAL))
    }

    override fun onAPIResponse(apiTag: String?, response: Any?) {
        when (apiTag) {
            TAG_CONTENT -> {
                val contentResponse: ContentResponse = Gson().fromJson(response.toString(), ContentResponse::class.java)
                (rvContent.adapter as ContentAdapter).populate(contentResponse.data)
            }
        }
    }

    override fun onErrorResponse(apiTag: String?, error: VolleyError?) {
        error?.printStackTrace()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
