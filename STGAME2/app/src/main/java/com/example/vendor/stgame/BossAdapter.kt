package com.example.vendor.stgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import java.util.*

/**
 * Created by ly on 2019/12/11 10:59
 *
 * Copyright is owned by chengdu haicheng technology
 * co., LTD. The code is only for learning and sharing.
 * It is forbidden to make profits by spreading the code.
 */
class BossAdapter(var mContext: Context, var mData: ArrayList<HashMap<String, String>>) : BaseAdapter() {

    companion object {
        val mCompare: Comparator<in HashMap<String, String>> = Comparator { o1, o2 ->
            if (o2[Constants.STATE] == o1[Constants.STATE]) {
                (Utils.dateToStamp(o2[Constants.BOSS_TIME]!!) + o1[Constants.BOSS_PERIOD]!!.toFloat() * 60 * 60 * 1000L).toInt() - (Utils.dateToStamp(
                    o1[Constants.BOSS_TIME]!!
                ) + o2[Constants.BOSS_PERIOD]!!.toFloat() * 60 * 60 * 1000L).toInt()
//                Utils.dateToStamp(o2[Constants.BOSS_TIME]!!).toInt()- Utils.dateToStamp(o1[Constants.BOSS_TIME]!!).toInt()
            } else {
                o2[Constants.STATE]!!.toInt() - o1[Constants.STATE]!!.toInt()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        var viewHolder: ViewHolder
        var view: View
        var time: Long = Utils.dateToStamp(mData[position][Constants.BOSS_TIME]!!) + (mData[position][Constants.BOSS_PERIOD]!!.toFloat() * 60 * 60 * 1000).toLong()
        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_layout, null);
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.tv_boss_space!!.text = mData[position][Constants.BOSS_SPACE]

        viewHolder.tv_boss_time!!.text =
            Utils.longToStringData(time)

        if ("1" == mData[position][Constants.STATE]) {
            viewHolder.tv_boss_space!!.setTextColor(mContext.getColor(R.color.BLACK))
            viewHolder.tv_boss_time!!.setTextColor(mContext.getColor(R.color.BLACK))
        } else if ("2" == mData[position][Constants.STATE]) {
            viewHolder.tv_boss_space!!.setTextColor(mContext.getColor(R.color.RED))
            viewHolder.tv_boss_time!!.setTextColor(mContext.getColor(R.color.RED))
        } else {
            viewHolder.tv_boss_space!!.setTextColor(mContext.getColor(R.color.GRAY))
            viewHolder.tv_boss_time!!.setTextColor(mContext.getColor(R.color.GRAY))
        }

        viewHolder.bt_update!!.setOnClickListener {
            if ("妖山一层".equals(mData[position][Constants.BOSS_SPACE])) {
                mData[position][Constants.BOSS_TIME] = Utils.longToStringData(Utils.getYaoOneBsTime())!!
            } else {
                mData[position][Constants.BOSS_TIME] = Utils.longToStringData(System.currentTimeMillis())!!

            }
            mData[position][Constants.STATE] = "1"
            notifyDataSetChanged()
        }
        viewHolder.bt_delete!!.setOnClickListener {
            mData.remove(mData[position])
            notifyDataSetChanged()
        }
        return view
    }

    override fun getItem(p0: Int): Any {
        return mData[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return mData.size
    }

    class ViewHolder(viewItem: View) {
        var tv_boss_space: TextView? = null
        var tv_boss_time: TextView? = null
        var bt_update: Button? = null
        var bt_delete: Button? = null

        init {
            tv_boss_space = viewItem.findViewById(R.id.tv_boss_space)
            tv_boss_time = viewItem.findViewById(R.id.tv_boss_time)
            bt_update = viewItem.findViewById(R.id.bt_update)
            bt_delete = viewItem.findViewById(R.id.bt_delete)
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        mData.sortWith(mCompare)
    }

}