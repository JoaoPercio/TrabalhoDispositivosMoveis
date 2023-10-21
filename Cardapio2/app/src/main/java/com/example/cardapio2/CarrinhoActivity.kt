package com.example.cardapio2

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import android.content.Intent

class CarrinhoAdapter(private val carrinhoList: MutableList<ComidaItem>, private val activity: CarrinhoActivity) :
    RecyclerView.Adapter<CarrinhoAdapter.CarrinhoViewHolder>() {

    // ViewHolder e métodos onBindViewHolder aqui para exibir os itens do carrinho

    inner class CarrinhoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewCarrinho)
        val nomeComidaView: TextView = itemView.findViewById(R.id.nomeComidaViewCarrinho)
        val descricaoView: TextView = itemView.findViewById(R.id.descricaoViewCarrinho)
        val valorView: TextView = itemView.findViewById(R.id.valorViewCarrinho)
        val removeCarButton: Button = itemView.findViewById(R.id.removeCarButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarrinhoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_carrinho, parent, false)
        return CarrinhoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CarrinhoViewHolder, position: Int) {
        val comida = carrinhoList[position]
        val context = holder.itemView.context
        val imageResource = context.resources.getIdentifier("food_image${comida.imageId}", "drawable", context.packageName)
        holder.imageView.setImageResource(imageResource)
        holder.nomeComidaView.text = comida.nome
        holder.descricaoView.text = comida.descricao
        holder.valorView.text = "R$ ${comida.valor}"
        holder.removeCarButton.setOnClickListener {
            carrinhoList.removeAt(position)
            // Notificar o RecyclerView sobre a remoção para atualizar a exibição

            notifyDataSetChanged()
            val novoValorTotal = activity.calcularValorTotal(carrinhoList)
            activity.atualizarValorTotal(novoValorTotal)
        }
    }

    override fun getItemCount(): Int {
        if (carrinhoList != null) {
            return carrinhoList.size
        }
        return 0
    }

}
class CarrinhoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var carrinhoAdapter: CarrinhoAdapter
    private lateinit var valorTotalView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carrinho)
        supportActionBar?.hide()
        val carrinhoList = intent.getParcelableArrayListExtra<ComidaItem>("carrinhoList")?.toMutableList()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        carrinhoAdapter = carrinhoList?.let { CarrinhoAdapter(it,this) }!!
        recyclerView.adapter = carrinhoAdapter
        valorTotalView= findViewById(R.id.valorTotalView)

        // Calcular o valor total e configurar o TextView
        val valorTotal = calcularValorTotal(carrinhoList)
        valorTotalView.text = "R$ $valorTotal"
        val pedidoButton: Button = findViewById(R.id.pedidoButton)
        pedidoButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("resultadoPedido", 1)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
        val voltarButton: Button = findViewById(R.id.voltarButton)
        voltarButton.setOnClickListener {
            val intent = Intent()
            intent.putExtra("resultadoPedido", 2)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }

    }
    fun calcularValorTotal(carrinhoList: MutableList<ComidaItem>?): Double {
        if (carrinhoList != null) {
            var total = 0.0
            for (comida in carrinhoList) {
                total += comida.valor
            }
            return total
        }
        return 0.0
    }
    fun atualizarValorTotal(valorTotal: Double) {
        valorTotalView.text = "R$ $valorTotal"
    }
}