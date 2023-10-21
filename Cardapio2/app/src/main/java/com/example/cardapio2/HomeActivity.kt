package com.example.cardapio2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast

class ComidaItem(val nome: String, val descricao: String, val valor: Double, val imageId: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nome)
        parcel.writeString(descricao)
        parcel.writeDouble(valor)
        parcel.writeInt(imageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComidaItem> {
        override fun createFromParcel(parcel: Parcel): ComidaItem {
            return ComidaItem(parcel)
        }

        override fun newArray(size: Int): Array<ComidaItem?> {
            return arrayOfNulls(size)
        }
    }
}
val carrinhoList = mutableListOf<ComidaItem>()

class ComidaAdapter(private val comidaList: List<ComidaItem>) :
    RecyclerView.Adapter<ComidaAdapter.ComidaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComidaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comida, parent, false)
        return ComidaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComidaViewHolder, position: Int) {
        val comida = comidaList[position]
        val context = holder.itemView.context
        val imageResource = context.resources.getIdentifier("food_image${comida.imageId}", "drawable", context.packageName)
        holder.imageView.setImageResource(imageResource)
        holder.nomeComidaView.text = comida.nome
        holder.descricaoView.text = comida.descricao
        holder.valorView.text = "R$ ${comida.valor}"
        holder.addCarButton.setOnClickListener {
            carrinhoList.add(comida)
            Toast.makeText(holder.itemView.context, "${comida.nome} adicionado ao carrinho", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return comidaList.size
    }

    inner class ComidaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nomeComidaView: TextView = itemView.findViewById(R.id.nomeComidaView)
        val descricaoView: TextView = itemView.findViewById(R.id.descricaoViewCarrinho)
        val valorView: TextView = itemView.findViewById(R.id.valorViewCarrinho)
        val addCarButton: Button = itemView.findViewById(R.id.removeCarButton)
    }
}
class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var comidaAdapter: ComidaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        val comidaList = mutableListOf(
            ComidaItem("Macarrão a Bolonhesa", "Descrição...", 33.00,1),
            ComidaItem("Panqueca de Carne", "Descrição...", 42.00,2),
            ComidaItem("Risotto", "Descrição...", 70.00,3),
            ComidaItem("Lasanha", "Descrição...", 89.00,4)
        )

        val nomeRecebido = intent.getStringExtra("nome")
        val nomeView = findViewById<TextView>(R.id.nomeView)
        nomeView.text = "Olá, $nomeRecebido"
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        comidaAdapter = ComidaAdapter(comidaList)
        recyclerView.adapter = comidaAdapter

        val carrinhoButton = findViewById<Button>(R.id.carrinhoButton)
        carrinhoButton.setOnClickListener {
            if(carrinhoList.size<1){
                Toast.makeText(this, "Carrinho vazio, adicione alguns itens", Toast.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, CarrinhoActivity::class.java)
                intent.putParcelableArrayListExtra("carrinhoList", ArrayList(carrinhoList))
                startActivityForResult(intent, 1)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val resultadoPedido = data?.getIntExtra("resultadoPedido", 0)
            if (resultadoPedido == 1) {
                Toast.makeText(this, "pedido realizado", Toast.LENGTH_SHORT).show()
                carrinhoList.clear()
            }
        }
    }

}