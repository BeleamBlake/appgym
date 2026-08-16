package com.example.appgym
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class EntradasAdapter(private val lista: List<RegistroProgreso>) :
    RecyclerView.Adapter<EntradasAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val lblFecha: TextView = view.findViewById(R.id.lblFecha)
        val lblPeso: TextView = view.findViewById(R.id.lblPeso)
        val lblEstatura: TextView = view.findViewById(R.id.lblEstatura)
        val lblGrasa: TextView = view.findViewById(R.id.lblGrasa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrada, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]

        val sdf = SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault())
        val fechaTexto = item.fecha?.toDate()?.let { sdf.format(it) } ?: "Sin fecha"

        holder.lblFecha.text = fechaTexto
        holder.lblPeso.text = "${item.peso} kg"
        holder.lblEstatura.text = "${item.estatura} m"
        holder.lblGrasa.text = "${item.grasaCorporal}%"
    }

    override fun getItemCount(): Int = lista.size
}