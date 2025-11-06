package com.example.appinterface

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.ProductoAdapter
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.producto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_productos)
    }


    fun volverpag(v: View) {
        onBackPressed()
    }


    fun crearProducto(v: View) {
        val nombre = findViewById<EditText>(R.id.nombreProducto)
        val descripcion = findViewById<EditText>(R.id.descripcionProducto)
        val cantidad = findViewById<EditText>(R.id.cantidadProducto)
        val imagen = findViewById<EditText>(R.id.imagenProducto)
        val idVendedor = findViewById<EditText>(R.id.idVendedor)
        val estado = findViewById<EditText>(R.id.estadoProducto)

        // Validar campos vacíos
        if (nombre.text.isEmpty() || descripcion.text.isEmpty() ||
            cantidad.text.isEmpty() || imagen.text.isEmpty() ||
            idVendedor.text.isEmpty() || estado.text.isEmpty()) {

            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }


        val nuevoProducto = producto(
            id_producto = 0,
            nombre = nombre.text.toString(),
            descripcion = descripcion.text.toString(),
            cantidad = cantidad.text.toString().toInt(),
            imagen = imagen.text.toString(),
            id_vendedor = idVendedor.text.toString().toInt(),
            estado = estado.text.toString()
        )


        RetrofitInstance.api2kotlin.crearProducto(nuevoProducto)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val mensaje = response.body()?.string() ?: "Producto creado correctamente"
                        Toast.makeText(this@ProductosActivity, mensaje, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@ProductosActivity, "Error al crear producto (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@ProductosActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    @SuppressLint("MissingInflatedId")
    fun mostrarProductos(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getProductos().enqueue(object : Callback<List<producto>> {
            override fun onResponse(call: Call<List<producto>>, response: Response<List<producto>>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.isNotEmpty()) {
                        val listaProductos = data.map { p ->
                            """
                            ID: ${p.id_producto}
                            Nombre: ${p.nombre}
                            Descripción: ${p.descripcion}
                            Cantidad: ${p.cantidad}
                            Imagen: ${p.imagen}
                            ID vendedor: ${p.id_vendedor}
                            Estado: ${p.estado}
                            """.trimIndent()
                        }
                        val adapter = ProductoAdapter(listaProductos)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@ProductosActivity, "No hay productos disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ProductosActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<producto>>, t: Throwable) {
                Toast.makeText(this@ProductosActivity, "Error en la conexión con la API: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
