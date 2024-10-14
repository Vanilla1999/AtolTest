package Dialog.adapters



import android.annotation.SuppressLint

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.atoltest.GenericDiffUtil
import com.example.atoltest.GenericItemDiff
import com.example.atoltest.databinding.ItemRecyclerViewForEmptyBinding
import com.example.atoltest.databinding.ListItemBinding
import com.example.atoltest.databinding.ListItemNotEnableBinding


class BluetoothNotBoundAdapter(
    private val onBTCallback: (BTDevice) -> Unit,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val Enable = 1
        private const val NotEnable = 2
        private const val Empty = 3
    }

    var btListForUpdate = mutableListOf<BTDevice>()
    var btList = listOf<BTDevice>(BTDevice())
    private lateinit var diff: GenericItemDiff<BTDevice>


    //  @LayoutRes private val layoutRes: Int,
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Enable -> {
                val binding = ListItemBinding.inflate(inflater, parent, false)
                ViewHolderEnable(
                    binding, itemClick
                )
            }
            NotEnable -> {
                val binding = ListItemNotEnableBinding.inflate(inflater, parent, false)
                ViewHolderNotEnable(
                    binding
                )
            }
            Empty -> {
                val binding = ItemRecyclerViewForEmptyBinding.inflate(inflater, parent, false)
                ViewHolderNull(
                    binding
                )
            }
            else -> throw IllegalStateException("Incorrect view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (btList[position].deviceName.isNullOrEmpty() && btList[position].macAdress.isNullOrEmpty())
            (holder as ViewHolderNull)
        else if (btList[position].enabled)
            (holder as ViewHolderEnable).bindView(btList[position]) else (holder as ViewHolderNotEnable).bindView(
            btList[position])
    }

    // чтоб использовать разные ViewHolder.
    override fun getItemViewType(position: Int): Int {
        return if (btList[position].deviceName.isNullOrEmpty() && btList[position].macAdress.isNullOrEmpty()) Empty else
            if (btList[position].enabled)
                Enable else {
                NotEnable
            }
    }

    fun setDiff(diff: GenericItemDiff<BTDevice>) {
        this.diff = diff
    }

    fun update(items: List<BTDevice>) {
        val diffCallback = GenericDiffUtil(btList, items, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            btList = items
            diffResult.dispatchUpdatesTo(this)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    fun updateOneItem(item: BTDevice) {
        btListForUpdate.add(item)
        btList = btListForUpdate
        notifyDataSetChanged()
    }

    private val itemClick: (Int) -> Unit =
        { position: Int -> onBTCallback(btList[position]) }

    inner class ViewHolderEnable(
        private val item: ListItemBinding,
        private val itemClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                // itemClick(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(model: BTDevice) {
            item.mainTitle.text = model.deviceName
            item.subTitle.text = model.macAdress
            item.pairButton.setOnClickListener {
                itemClick(adapterPosition)
            }
        }
    }

    inner class ViewHolderNotEnable(
        private val item: ListItemNotEnableBinding,
    ) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                // itemClick(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(model: BTDevice) {
        }
    }

    inner class ViewHolderNull(
        private val item: ItemRecyclerViewForEmptyBinding,
    ) : RecyclerView.ViewHolder(item.root)


    override fun getItemCount(): Int {
        return btList.size
    }

}
