package kr.co.wanted.gongzone.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.wanted.gongzone.databinding.FilterItemBinding

class FilterViewAdapter: RecyclerView.Adapter<FilterViewAdapter.Holder>() {
    var listData = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val filterName = listData[position]

        if (position == listData.size-1) {
            holder.setFilterName(filterName, true)
        } else {
            holder.setFilterName(filterName, false)
        }
    }

    class Holder(val binding: FilterItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun setFilterName(filterName: String, isLastPos: Boolean) {
            binding.filterName.text = filterName
            if (isLastPos) binding.bar.visibility = GONE
        }
    }
}