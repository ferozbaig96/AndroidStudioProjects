package com.app.fbulou.kotlinankosample

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.asReference
import org.jetbrains.anko.coroutines.experimental.bg

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val ref = asReference()

        btn.setOnClickListener {
            async(UI) {
                btn.isEnabled = false
                val result = bg {
                    // do something in background
                    loge("Before heavy bg task")
                    Thread.sleep(5000L)
                    loge("After heavy bg task")

                    "returned sample string"
                }

                loge("Inside UI Thread")
                // update UI
                val str: String = result.await()
                loge("Updating UI now")
                ref.invoke().renderData(str)
                loge("UI updated")

                btn.isEnabled = true
            }

            loge("Outside UI Thread")
        }
    }

    private fun renderData(str: String) {
        loge(str)
    }

}
