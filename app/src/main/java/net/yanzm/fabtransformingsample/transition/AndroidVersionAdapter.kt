package net.yanzm.fabtransformingsample.transition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AndroidVersionAdapter : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = VERSIONS[position % VERSIONS.size]
    }

    override fun getItemCount(): Int {
        return VERSIONS.size * 10
    }

    companion object {
        private val VERSIONS = arrayOf(
            "Cupcake",
            "Donuts",
            "Eclair",
            "Froyo",
            "Gingerbread",
            "Honeycomb",
            "IceCreamSandwich",
            "JellyBean",
            "KitKat",
            "Lollipop",
            "Marshmallow",
            "Nougat"
        )
    }
}

class ViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView as TextView

    companion object {
        private const val LAYOUT_ID = android.R.layout.simple_list_item_1

        fun create(inflater: LayoutInflater, parent: ViewGroup?): ViewHolder {
            return ViewHolder(inflater.inflate(LAYOUT_ID, parent, false))
        }
    }
}
