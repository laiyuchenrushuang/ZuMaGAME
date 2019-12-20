package com.example.vendor.stgame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import java.util.*

/**
 */
class BossAdapter(private var mContext: Context, private var mData: ArrayList<HashMap<String, String>>) : BaseAdapter() {

    companion object {
        val mCompare: Comparator<in HashMap<String, String>> = Comparator { o1, o2 ->
            if (o2[Constants.STATE] == o1[Constants.STATE]) {
                TimeUtils.String2Int(o1[Constants.BOSS_TIME]!!)  - TimeUtils.String2Int(o2[Constants.BOSS_TIME]!!)
            } else {
                o2[Constants.STATE]!!.toInt() - o1[Constants.STATE]!!.toInt()
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, convertView: View?, p2: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            view = View.inflate(mContext, R.layout.item_layout, null)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.tvBossSpace!!.text = mData[position][Constants.BOSS_SPACE]


        viewHolder.tvBossTime!!.text =  mData[position][Constants.BOSS_TIME]!!

        when {
            "1" == mData[position][Constants.STATE] -> {
                viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.BLACK))
                viewHolder.tvBossTime!!.setTextColor(mContext.getColor(R.color.BLACK))
            }
            "2" == mData[position][Constants.STATE] -> {
                viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.RED))
                viewHolder.tvBossTime!!.setTextColor(mContext.getColor(R.color.RED))
            }
            else -> {
                viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.GRAY))
                viewHolder.tvBossTime!!.setTextColor(mContext.getColor(R.color.GRAY))
            }
        }

        if( viewHolder.tvBossSpace!!.text.toString().contains("森林")){
            viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.green))
        }

        if( viewHolder.tvBossSpace!!.text.toString().contains("海底")){
            viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.blue))
        }

        if( viewHolder.tvBossSpace!!.text.toString().contains("地下")){
            viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.purple))
        }

        if( viewHolder.tvBossSpace!!.text.toString().contains("BOSS")){
            viewHolder.tvBossSpace!!.setTextColor(mContext.getColor(R.color.yellow))
        }

        viewHolder.btUpdate!!.setOnClickListener {
            if ("妖山一层" == mData[position][Constants.BOSS_SPACE]) {
                mData[position][Constants.BOSS_TIME] =Utils.getYaoOneBsTime()
            } else {
                mData[position][Constants.BOSS_TIME] = Utils.getBsTime(TimeUtils.getCurrentTime(),mData[position][Constants.BOSS_PERIOD]!!.toFloat())
                if(TimeUtils.String2Int(TimeUtils.getCurrentTime()) + mData[position][Constants.BOSS_PERIOD]!!.toFloat() * 60 * 60 > Constants.LIMIT86399){
                    mData[position][Constants.OVER_NUM] = "1"
                }else{
                    mData[position][Constants.OVER_NUM] = "0"
                }

            }
            mData[position][Constants.STATE] = "1"
            notifyDataSetChanged()
        }
        viewHolder.btDelete!!.setOnClickListener {
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
        var tvBossSpace: TextView? = null
        var tvBossTime: TextView? = null
        var btUpdate: Button? = null
        var btDelete: Button? = null

        init {
            tvBossSpace = viewItem.findViewById(R.id.tv_boss_space)
            tvBossTime = viewItem.findViewById(R.id.tv_boss_time)
            btUpdate = viewItem.findViewById(R.id.bt_update)
            btDelete = viewItem.findViewById(R.id.bt_delete)
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        mData.sortWith(mCompare)
    }

}