package com.example.appinterface
import android.widget.TextView
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appinterface.Adapter.PersonaAdapter
import com.example.appinterface.Api.DataResponse
import com.example.appinterface.Api.RetrofitInstance
import com.example.appinterface.model.cliente
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class MainActivity : AppCompatActivity() {
    private lateinit var persona: Persona

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonGoToSecondActivity: Button = findViewById(R.id.buttonSegundaActividad)
        buttonGoToSecondActivity.setOnClickListener {
            val intent = Intent(this, ProductosActivity::class.java)
            startActivity(intent)
        }
    }

    fun crearpersona(v: View) {
        var nombre = findViewById<EditText>(R.id.nombre)
        var apellido = findViewById<EditText>(R.id.apellido)
        var contrasena = findViewById<EditText>(R.id.contrasena)
        var direccion = findViewById<EditText>(R.id.direccion)
        var telefono = findViewById<EditText>(R.id.telefono)
        var correo = findViewById<EditText>(R.id.correo)

        if (nombre.text.isEmpty() || apellido.text.isEmpty() || contrasena.text.isEmpty() ||
            direccion.text.isEmpty() || telefono.text.isEmpty() || correo.text.isEmpty()) {

            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoCliente = cliente(

            nombre = nombre.text.toString(),
            apellido = apellido.text.toString(),
            contrasena = contrasena.text.toString(),
            direccion = direccion.text.toString(),
            telefono = telefono.text.toString(),
            correo_electronico = correo.text.toString()
        )

        RetrofitInstance.api2kotlin.crearCliente(nuevoCliente)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val mensaje = response.body()?.string() ?: "Cliente creado correctamente"
                        Toast.makeText(this@MainActivity, mensaje, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al crear cliente (${response.code()})", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }


    private fun DataPersona(pCliente: TextView, cCliente: cliente) {
        val description = """
        Nombre: ${cCliente.nombre}
        Apellido: ${cCliente.apellido}
        Dirección: ${cCliente.direccion}
        Teléfono: ${cCliente.telefono}
        Correo: ${cCliente.correo_electronico}
    """.trimIndent()

        pCliente.text = description
    }

    fun crearmostrarpersonas(v: View) {
        val recyclerView = findViewById<RecyclerView>(R.id.RecyPersonas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.api2kotlin.getClientes().enqueue(object : Callback<List<cliente>> {
            override fun onResponse(call: Call<List<cliente>>, response: Response<List<cliente>>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if (data != null && data.isNotEmpty()) {
                        // Mapear los datos a texto legible
                        val listaClientes = data.map { c ->
                            """
                          
                            Nombre: ${c.nombre}
                            Apellido: ${c.apellido}
                            Contraseña: ${c.contrasena}
                            Dirección: ${c.direccion}
                            Teléfono: ${c.telefono}
                            Correo: ${c.correo_electronico}
                            """.trimIndent()
                        }

                        val adapter = PersonaAdapter(listaClientes)
                        recyclerView.adapter = adapter
                    } else {
                        Toast.makeText(this@MainActivity, "No hay clientes disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<cliente>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error en la conexión con la API: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
