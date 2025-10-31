package com.example.appinterface.Api

import com.example.appinterface.model.cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import okhttp3.ResponseBody


interface ApiServicesKotlin {
    @GET("cliente")
    fun getClientes(): Call<List<cliente>>

    @POST("cliente")
    fun crearCliente(@Body cliente: cliente): Call<ResponseBody>


}