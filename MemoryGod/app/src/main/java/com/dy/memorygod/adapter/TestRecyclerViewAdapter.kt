package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent
import kotlinx.android.synthetic.main.item_test.view.*

class TestRecyclerViewAdapter(
    private val context: Context,
    private val onEventListener: TestRecyclerViewEventListener
) :
    RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tracker: SelectionTracker<Long>
    lateinit var dataList: MutableList<MainDataContent>

    init {
        setHasStableIds(true)
    }

    fun init(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        setTracker()
    }

    private fun setTracker() {
        tracker = SelectionTracker.Builder(
            javaClass.name,
            recyclerView,
            MyItemKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()

                    val selection = tracker.selection
                    val size = selection.size()
                    onEventListener.onItemSelected(size)
                }
            })
    }

    inner class MyItemKeyProvider(private val recyclerView: RecyclerView) :
        ItemKeyProvider<Long>(SCOPE_MAPPED) {

        override fun getKey(position: Int): Long? {
            return recyclerView.adapter?.getItemId(position)
        }

        override fun getPosition(key: Long): Int {
            val viewHolder = recyclerView.findViewHolderForItemId(key)
            return viewHolder?.layoutPosition ?: RecyclerView.NO_POSITION
        }
    }

    inner class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Long>() {
        override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as TestRecyclerViewAdapter.ViewHolder)
                    .getItemDetails()
            }

            return null
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.textView_test_item_title

        fun bind(data: MainDataContent, isActivated: Boolean) {
            titleTextView.text = data.problem
            itemView.isActivated = isActivated

            refreshVisibility(itemView)
            refreshBgColor(itemView)
            refreshTestCheckColor(itemView, data)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
            object : ItemDetailsLookup.ItemDetails<Long>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Long? = itemId
            }
    }

    private fun refreshVisibility(itemView: View) {
        val reorderImageView: ImageView =
            itemView.imageView_test_item_reorder

        reorderImageView.visibility = View.GONE
    }

    private fun refreshBgColor(itemView: View) {
        val cardView: View =
            itemView.cardView_test_recyclerView

        when (itemView.isActivated) {
            true -> cardView.setBackgroundResource(R.color.color_item_bg_selection)
            false -> cardView.setBackgroundResource(R.color.color_item_bg_normal)
        }
    }

    private fun refreshTestCheckColor(itemView: View, data: MainDataContent) {
        val testCheckView: View =
            itemView.view_test_item_test_check

        val color = data.testCheck.color
        testCheckView.setBackgroundResource(color)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val data = dataList[position]

        val selectedPosition = position.toLong()
        val isSelected = tracker.isSelected(selectedPosition)

        holder.bind(data, isSelected)

        itemView.setOnClickListener {
            onEventListener.onItemClicked(position)
        }
    }

    fun refresh(dataList: MutableList<MainDataContent>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun addItemAtFirst(data: MainDataContent) {
        dataList.add(0, data)
        refreshSelection(data)
    }

    fun addItemAtLast(data: MainDataContent) {
        dataList.add(data)
        refreshSelection(data)
    }

    private fun refreshSelection(data: MainDataContent) {
        clearSelection()
        select(data)
    }

    fun getSelectionSize(): Int {
        return tracker.selection.size()
    }

    fun getSelectedList(): List<MainDataContent> {
        return tracker.selection.map { dataList[it.toInt()] }
    }

    fun select(data: MainDataContent) {
        val idx = dataList.indexOf(data)
        val key = idx.toLong()

        if (idx == -1) {
            return
        }

        tracker.select(key)
        scrollToPosition(idx)
    }

    fun scrollToPosition(idx: Int) {
        if (idx == -1 || idx >= itemCount) {
            return
        }

        recyclerView.scrollToPosition(idx)
    }

    fun selectAll() {
        val keys = (0 until dataList.size).map { it.toLong() }
        tracker.setItemsSelected(keys, true)
    }

    fun clearSelection() {
        tracker.clearSelection()
    }

}