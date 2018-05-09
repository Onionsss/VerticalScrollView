package com.onion.verticalscrollview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = mutableListOf("梦幻西游今天全新开始了,希望大家来玩!","今天是个好日子!","呵呵","驱动大师大所大所大所多奥术大师大所大所大所大所多奥术大师大所大所大所多奥术大师大所多")

        main_vertical.setData(data)
        main_vertical.start()
    }
}
