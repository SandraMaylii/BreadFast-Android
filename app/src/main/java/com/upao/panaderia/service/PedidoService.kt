package com.upao.panaderia.service

import android.content.Context
import com.upao.panaderia.models.requestModel.PedidosRequest
import com.upao.panaderia.models.responseModel.PedidoResponse
import com.upao.panaderia.repositories.PedidoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PedidoService(context: Context) {

    private val pedidoRepository = PedidoRepository(context)

    fun createPedido(context: Context, pedido: PedidosRequest, onResult: (String) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val idPedido = pedidoRepository.createPedido(context, pedido)
            withContext(Dispatchers.Main) {
                onResult(idPedido)
            }
        }
    }

    fun getPedido(context: Context, id: Int, onResult: (PedidoResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val pedido = pedidoRepository.getPedido(context, id)
            withContext(Dispatchers.Main) {
                onResult(pedido)
            }
        }
    }
}