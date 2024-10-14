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
import com.example.atoltest.R
import com.example.atoltest.databinding.ListItem2Binding


class BluetoothBoundAdapter(
    private val onBTCallback: (BTDevice) -> Unit,
    private val onTouch: (BTDevice) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val Enable = 1
        private const val NotEnable = 2
        private const val Empty = 3
    }

    var btList = listOf<BTDevice>(BTDevice())
    var btListForUpdate = mutableListOf<BTDevice>()
    private lateinit var diff: GenericItemDiff<BTDevice>


    //  @LayoutRes private val layoutRes: Int,
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            Enable -> {
                val binding = ListItem2Binding.inflate(inflater, parent, false)
                ViewHolderEnable(
                    binding, itemClick,itemTouch
                )
            }
            NotEnable -> {
                val binding = inflater.inflate(R.layout.list_item_not_enable, parent, false)
                ViewHolderNotEnable(
                    binding
                )
            }
            Empty -> {
                val binding = inflater.inflate(R.layout.item_recycler_view_for_empty, parent, false)
                ViewHolderNull(
                    binding
                )
            }
            else -> throw IllegalStateException("Incorrect view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (btList[position].deviceName.isNullOrEmpty() && btList[position].macAdress.isNullOrEmpty())
            (holder as ViewHolderNull) else if (btList[position].enabled)
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
        btListForUpdate = items.toMutableList()
        val diffCallback = GenericDiffUtil(btList, btListForUpdate, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            btList = items
            diffResult.dispatchUpdatesTo(this)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    fun updateOneItemDiff(item: BTDevice) {
        btListForUpdate.add(item)
        val diffCallback = GenericDiffUtil(btList, btListForUpdate, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            btList = btListForUpdate
            diffResult.dispatchUpdatesTo(this)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    fun updateOneItem(item: BTDevice) {
        Log.i("Log", "updateOneItem")
        btListForUpdate.add(item)
        btList = btListForUpdate
        notifyDataSetChanged()
    }


    fun deleteOneItem(item: BTDevice) {
        btListForUpdate.remove(item)
        val diffCallback = GenericDiffUtil(btList, btListForUpdate, diff)
        val diffResult = DiffUtil.calculateDiff(diffCallback, true)
        try {
            btList = btListForUpdate
            diffResult.dispatchUpdatesTo(this)
        } catch (e: Exception) {
            Log.d("TAG", e.message.toString())
        }
    }

    private val itemClick: (Int) -> Unit =
        { position: Int -> onBTCallback(btList[position]) }

    private val itemTouch: (Int) -> Unit =
        { position: Int -> onTouch(btList[position]) }

    inner class ViewHolderEnable(
        private val item: ListItem2Binding,
        private val itemClick: (Int) -> Unit,
        private val itemTouch: (Int) -> Unit
    ) : RecyclerView.ViewHolder(item.root) {

        init {
            item.root.setOnClickListener {
                itemTouch(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(model: BTDevice) {
            item.mainTitle.text= model.deviceName
            item.subTitle.text= model.macAdress
            item.removeButton.setOnClickListener {
                itemClick(adapterPosition)
            }
        }
    }

    inner class ViewHolderNotEnable(
        private val item: View
    ) : RecyclerView.ViewHolder(item) {

        init {
            item.setOnClickListener {
                // itemClick(adapterPosition)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindView(model: BTDevice) {
        }
    }

    inner class ViewHolderNull(
        private val item: View
    ) : RecyclerView.ViewHolder(item)


    override fun getItemCount(): Int {
        return btList.size
    }

}
