package com.example.appinterface

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import kotlin.jvm.java

class PanelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel)


        val btnClientes = findViewById<Button>(R.id.btnClientes)
        val btnProductos = findViewById<Button>(R.id.btnProductos)


        btnClientes.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        btnProductos.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }
    }
}
