package kr.co.wanted.gongzone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.wanted.gongzone.databinding.SearchStoreViewBinding
import kr.co.wanted.gongzone.databinding.StoreViewBinding
import kr.co.wanted.gongzone.model.space.SpaceItem

class SpaceAdapter : RecyclerView.Adapter<SpaceAdapter.Holder>() {
    var listData = ArrayList<SpaceItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = SearchStoreViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val space = listData[position]

        holder.setSpace(space, position)
    }

    interface OnItemClickListener {
        fun onItemClick(v: View, pos: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class Holder(val binding: SearchStoreViewBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.storeBtn.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(it, pos)
                }
            }
        }

        fun setSpace(space: SpaceItem, pos: Int) {
            binding.storeIntroTxt.text = space.introduce
            "${space.spaceName}·${space.spaceType}".also { binding.storeNameAndKindTxt.text = it }
            Glide.with(binding.root).load(space.imagePath).into(binding.storeImg)

            when (pos) {
                (listData.size - 1) -> binding.line.visibility = GONE
            }
        }
    }
}