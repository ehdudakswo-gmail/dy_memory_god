package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.ActivityMode
import kotlinx.android.synthetic.main.item_test_recycler_view.view.*

class TestRecyclerViewAdapter(
    private val context: Context,
    private val onEventListener: TestRecyclerViewEventListener
) :
    RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>(),
    TestRecyclerViewTouchHelperListener {

    private var activityMode: ActivityMode = ActivityMode.TEST_NORMAL
    lateinit var dataList: MutableList<MainDataContent>
    var trashList: MutableList<MainDataContent> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_test_recycler_view, parent, false)

        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: View =
            itemView.cardView_test_recyclerView
        private val titleTextView: TextView =
            itemView.textView_test_recyclerView_title


        fun bind(data: MainDataContent) {
            titleTextView.text = data.problem
            cardView.setBackgroundResource(R.color.color_test_recyclerView_item_bg_normal)
            refreshTestCheck(itemView, data)
            refreshVisibility(itemView)
        }
    }

    private fun refreshTestCheck(itemView: View, data: MainDataContent) {
        val testCheckView: View =
            itemView.view_test_recyclerView_item_test_check

        val color = data.testCheck.color
        testCheckView.setBackgroundResource(color)
    }

    private fun refreshVisibility(itemView: View) {
        val moveImageView: ImageView =
            itemView.imageView_test_recyclerView_item_move
        val deleteImageView: ImageView =
            itemView.imageView_test_recyclerView_item_delete

        when (activityMode) {
            ActivityMode.TEST_NORMAL -> {
                moveImageView.visibility = View.GONE
                deleteImageView.visibility = View.GONE
            }
            ActivityMode.TEST_EDIT -> {
                moveImageView.visibility = View.VISIBLE
                deleteImageView.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val data = dataList[position]

        holder.bind(data)

        itemView.setOnClickListener {
            onEventListener.onItemClicked(position)
        }

        itemView.imageView_test_recyclerView_item_delete.setOnClickListener {
            onEventListener.onItemDeleted(position)
        }

        itemView.imageView_test_recyclerView_item_delete.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val view = itemView.cardView_test_recyclerView
                    view.setBackgroundResource(R.color.color_test_recyclerView_item_bg_delete)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    val view = itemView.cardView_test_recyclerView
                    view.setBackgroundResource(R.color.color_test_recyclerView_item_bg_normal)
                }
            }

            false
        }

        itemView.imageView_test_recyclerView_item_move.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onEventListener.onDragStarted(holder)
                    val view = itemView.cardView_test_recyclerView
                    view.setBackgroundResource(R.color.color_test_recyclerView_item_bg_move)
                }
            }

            false
        }
    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        val fromItem = dataList[from]
        dataList.removeAt(from)
        dataList.add(to, fromItem)

        notifyItemMoved(from, to)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                notifyDataSetChanged()
            }
        }
    }

    override fun onItemSwipe(position: Int) {
        deleteItem(position)
    }

    fun refresh(activityMode: ActivityMode, dataList: MutableList<MainDataContent>) {
        this.activityMode = activityMode
        this.dataList = dataList

        notifyDataSetChanged()
    }

    fun addItem(data: MainDataContent) {
        dataList.add(data)

        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val trashItem = dataList.removeAt(position)
        trashList.add(0, trashItem)

        notifyDataSetChanged()
    }

    fun restoreItem(data: MainDataContent) {
        trashList.remove(data)
        dataList.add(0, data)

        notifyDataSetChanged()
    }

}