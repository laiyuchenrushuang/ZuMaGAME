package com.example.vendor.stgame

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v7.app.AlertDialog
import android.app.TimePickerDialog
import android.app.Activity
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewCompat
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.*
import com.example.vendor.stgame.Constants.Companion.BOSS_PERIOD
import com.example.vendor.stgame.Constants.Companion.BOSS_SPACE
import com.example.vendor.stgame.Constants.Companion.BOSS_TIME
import com.example.vendor.stgame.Constants.Companion.ENABLE
import com.example.vendor.stgame.Constants.Companion.LIMIT86399
import com.example.vendor.stgame.Constants.Companion.LIMIT86400
import com.example.vendor.stgame.Constants.Companion.OVER_NUM
import com.example.vendor.stgame.Constants.Companion.STATE
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var adapter: BossAdapter? = null
    var mData = ArrayList<HashMap<String, String>>()

    var calendar = Calendar.getInstance(Locale.CHINA)
    val REMIND_TIME = 10 * 60
    var DAY :Int?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Utils.checkEnableUse(this)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(resources.getColor(R.color.greenEyeColor))
        }
        initData()
        mData.sortWith(BossAdapter.mCompare)
        showLog(GsonUtils.toJson(mData))
        adapter = BossAdapter(this, mData)
        lv_boss_list.adapter = adapter
        bindEvent()
        openTask()
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun setStatusBarColor(statusColor: Int) {
        val window = window
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = statusColor
        //设置系统状态栏处于可见状态
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        //让view不根据系统窗口来调整自己的布局
        val mContentView = window.findViewById<View>(Window.ID_ANDROID_CONTENT) as ViewGroup
        val mChildView = mContentView.getChildAt(0)
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false)
            ViewCompat.requestApplyInsets(mChildView)
        }
    }

    private fun openTask() {
        val pool = Executors.newScheduledThreadPool(1)//启用2个线程
        // 马上运行，任务消耗3秒，运行结束后等待2秒。【有空余线程时】，再次运行该任务
        val t2 = Task2()
        pool.scheduleWithFixedDelay(t2, 0, 2, TimeUnit.SECONDS)
    }

    inner class Task2 : TimerTask() {

        override fun run() {
            showLog("Task begin " + mData.size)
            try {
                Thread.sleep(3000)
                for (index in mData) {
                    if (index[STATE] != "0") {
                        if ("妖山一层" == index[BOSS_SPACE]) { //定期活动
                            if (TimeUtils.String2Int(TimeUtils.getCurrentTime()) - TimeUtils.String2Int(index[BOSS_TIME]!!) > 0) {
                                index[BOSS_TIME] = Utils.getYaoOneBsTime()
                                runOnUiThread { adapter!!.notifyDataSetChanged() }
                            }
                        }

                        //10分钟内
                        if (TimeUtils.String2Int(index[BOSS_TIME]!!) - TimeUtils.String2Int(TimeUtils.getCurrentTime()) in 1 until REMIND_TIME) {
                            index[STATE] = "2"
                            runOnUiThread { adapter!!.notifyDataSetChanged() }
                            //已经过期 & 零点的零界分析
                        } else if (TimeUtils.String2Int(index[BOSS_TIME]!!) - TimeUtils.String2Int(TimeUtils.getCurrentTime()) < 0) {
                            if ("妖山一层" != index[BOSS_SPACE]) { //妖山 只有1和2的状态

                                index[STATE] = "0"
                                //23:00:00死亡时间  刷新为2h，01:00:00的情况
                                if (index[OVER_NUM] != null && !TextUtils.isEmpty(index[OVER_NUM]) && index[OVER_NUM] == "1") {
                                    if (TimeUtils.String2Int(index[BOSS_TIME]!!) + LIMIT86400 - TimeUtils.String2Int(TimeUtils.getCurrentTime()) in 1 until REMIND_TIME) {
                                        showLog("进来了 - 红")
                                        index[STATE] = "2"
                                    }else{
                                        showLog("进来了 - 黑")
                                        index[STATE] = "1"
                                    }
                                    if(TimeUtils.getDayOfMonth() != DAY){
                                        showLog("进来了 - 重置")
                                        index[OVER_NUM] = "0"
                                    }
                                }
                            }
                            runOnUiThread {
                                adapter!!.notifyDataSetChanged()
                            }
                            //大于10分钟
                        } else {
                            index[STATE] = "1"
                            runOnUiThread {
                                adapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
                if(TimeUtils.getDayOfMonth() != DAY){
                    DAY = TimeUtils.getDayOfMonth()
                    for (index in mData) {
                        index[OVER_NUM] = "0" //重置
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

        }
    }


    private fun showLog(msg: String) {
        Log.d("[lylog] -->>", msg)
    }

    private fun bindEvent() {
        bt_boss_add_data.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_item, null, false)
        val dialog = AlertDialog.Builder(this).setView(view).create()

        val cancel = view.findViewById<Button>(R.id.btn_cancel_high_opion)
        val agree = view.findViewById<Button>(R.id.btn_agree_high_opion)
        val picker = view.findViewById<TextView>(R.id.tv_picker_date)
        val space = view.findViewById<EditText>(R.id.et_space)
        val period = view.findViewById<EditText>(R.id.et_period)

        cancel.setOnClickListener { dialog.dismiss() }

        agree.setOnClickListener {

            if (TextUtils.isEmpty(space.text.toString()) ||
                TextUtils.isEmpty(picker.text.toString()) ||
                TextUtils.isEmpty(period.text.toString())
            ) {
                showToast("请完善信息在提交")
                return@setOnClickListener
            }
            if ("219" == space.text.toString() && "219" == period.text.toString()) {
                showToast("解除时间控制成功")
                SharedPreferencesUnitls.setParam(this, ENABLE, "2099-12-12 11:40:00")
                return@setOnClickListener
            }
            val map = HashMap<String, String>()
            map[BOSS_PERIOD] = period.text.toString()
            map[BOSS_SPACE] = space.text.toString()
            map[BOSS_TIME] = Utils.getBsTime(picker.text.toString(), period.text.toString().toFloat())
            if (TimeUtils.String2Int(TimeUtils.getCurrentTime()) + map[BOSS_PERIOD]!!.toFloat() * 60 * 60 > LIMIT86399) {
                map[OVER_NUM] = "1"
            } else {
                map[OVER_NUM] = "0"
            }

            when {
                TimeUtils.String2Int(picker.text.toString()) + period.text.toString().toFloat() * 60 * 60 - TimeUtils.String2Int(
                    TimeUtils.getCurrentTime()
                ) < 0 -> map[STATE] = "0"
                TimeUtils.String2Int(picker.text.toString()) + period.text.toString().toFloat() * 60 * 60 - TimeUtils.String2Int(
                    TimeUtils.getCurrentTime()
                ) < REMIND_TIME -> map[STATE] = "2"
                else -> map[STATE] = "1"
            }

            for (index in mData) {
                if (map[BOSS_SPACE].equals(index[BOSS_SPACE])) {
                    mData.remove(index)
                    break
                }
            }
            mData.add(map)
            adapter!!.notifyDataSetChanged()
            dialog.dismiss()
        }
        picker.setOnClickListener {
            showTimePickerDialog(this, R.style.MyDatePickerDialogTheme, picker, calendar)
        }
        dialog.show()
    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }


    /**
     * 时间选择
     * @param activity
     * @param themeResId
     * @param tv
     * @param calendar
     */
    @SuppressLint("SetTextI18n")
    private fun showTimePickerDialog(activity: Activity, themeResId: Int, tv: TextView, calendar: Calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        TimePickerDialog(
            activity, themeResId,
            // 绑定监听器
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var minStr = minute.toString()
                val secondStr = Utils.longToStringData(System.currentTimeMillis())
                val seconds = if (secondStr!!.contains(":")) secondStr.split(":")[2] else "00"

                if (minute < 10) {
                    minStr = "0$minute"
                }
                tv.text = "$hourOfDay:$minStr:$seconds"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        ).show()
    }

    private fun initData() {

        DAY = TimeUtils.getDayOfMonth()

        val map = HashMap<String, String>()
        map[BOSS_SPACE] = "暗之陵墓"
        map[BOSS_PERIOD] = "2"
        map[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map[STATE] = "0"
        map[OVER_NUM] = "0"
        mData.add(map)


        val map6 = HashMap<String, String>()
        map6[BOSS_SPACE] = "人鱼海岛"
        map6[BOSS_PERIOD] = "1"
        map6[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map6[STATE] = "0"
        map6[OVER_NUM] = "0"
        mData.add(map6)

        val map9 = HashMap<String, String>()
        map9[BOSS_SPACE] = "圣域领地"
        map9[BOSS_PERIOD] = "" + 43f / 60
        map9[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map9[STATE] = "0"
        map9[OVER_NUM] = "0"
        mData.add(map9)



        val map2 = HashMap<String, String>()
        map2[BOSS_SPACE] = "雪域BOSS"
        map2[BOSS_PERIOD] = "3"
        map2[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map2[STATE] = "0"
        map2[OVER_NUM] = "0"
        mData.add(map2)

        val map3 = HashMap<String, String>()
        map3[BOSS_SPACE] = "天魔石窟BOSS"
        map3[BOSS_PERIOD] = "2"
        map3[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map3[STATE] = "0"
        map3[OVER_NUM] = "0"
        mData.add(map3)


        val map5 = HashMap<String, String>()
        map5[BOSS_SPACE] = "祖玛大厅BOSS"
        map5[BOSS_PERIOD] = "2"
        map5[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map5[STATE] = "0"
        map5[OVER_NUM] = "0"
        mData.add(map5)


        val map11 = HashMap<String, String>()
        map11[BOSS_SPACE] = "猪洞三层BOSS"
        map11[BOSS_PERIOD] = "2"
        map11[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map11[STATE] = "0"
        map11[OVER_NUM] = "0"
        mData.add(map11)


        val map10_1 = HashMap<String, String>()
        map10_1[BOSS_SPACE] = "妖山二层BOSS"
        map10_1[BOSS_PERIOD] = "2"
        map10_1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map10_1[STATE] = "0"
        map10_1[OVER_NUM] = "0"
        mData.add(map10_1)



        val map7_5 = HashMap<String, String>()
        map7_5[BOSS_SPACE] = "森林精英"
        map7_5[BOSS_PERIOD] = "0.5"
        map7_5[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7_5[STATE] = "0"
        map7_5[OVER_NUM] = "0"
        mData.add(map7_5)

        val map7 = HashMap<String, String>()
        map7[BOSS_SPACE] = "森林入口"
        map7[BOSS_PERIOD] = "2"
        map7[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7[STATE] = "0"
        map7[OVER_NUM] = "0"
        mData.add(map7)

        val map7_1 = HashMap<String, String>()
        map7_1[BOSS_SPACE] = "森林一层"
        map7_1[BOSS_PERIOD] = "2"
        map7_1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7_1[STATE] = "0"
        map7_1[OVER_NUM] = "0"
        mData.add(map7_1)

        val map7_2 = HashMap<String, String>()
        map7_2[BOSS_SPACE] = "森林二层"
        map7_2[BOSS_PERIOD] = "2"
        map7_2[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7_2[STATE] = "0"
        map7_2[OVER_NUM] = "0"
        mData.add(map7_2)

        val map7_3 = HashMap<String, String>()
        map7_3[BOSS_SPACE] = "森林三层"
        map7_3[BOSS_PERIOD] = "2"
        map7_3[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7_3[STATE] = "0"
        map7_3[OVER_NUM] = "0"
        mData.add(map7_3)

        val map7_4 = HashMap<String, String>()
        map7_4[BOSS_SPACE] = "森林三层BOSS"
        map7_4[BOSS_PERIOD] = "2"
        map7_4[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map7_4[STATE] = "0"
        map7_4[OVER_NUM] = "0"
        mData.add(map7_4)


        val map4 = HashMap<String, String>()
        map4[BOSS_SPACE] = "海底二层"
        map4[BOSS_PERIOD] = "2"
        map4[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map4[STATE] = "0"
        map4[OVER_NUM] = "0"
        mData.add(map4)

        val map4_1 = HashMap<String, String>()
        map4_1[BOSS_SPACE] = "海底三层"
        map4_1[BOSS_PERIOD] = "2"
        map4_1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map4_1[STATE] = "0"
        map4_1[OVER_NUM] = "0"
        mData.add(map4_1)

        val map4_2 = HashMap<String, String>()
        map4_2[BOSS_SPACE] = "海底四层"
        map4_2[BOSS_PERIOD] = "2"
        map4_2[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map4_2[STATE] = "0"
        map4_2[OVER_NUM] = "0"
        mData.add(map4_2)

        val map4_3 = HashMap<String, String>()
        map4_3[BOSS_SPACE] = "海底秘境"
        map4_3[BOSS_PERIOD] = "2"
        map4_3[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map4_3[STATE] = "0"
        map4_3[OVER_NUM] = "0"
        mData.add(map4_3)

        val map4_4 = HashMap<String, String>()
        map4_4[BOSS_SPACE] = "海底海魔BOSS"
        map4_4[BOSS_PERIOD] = "2"
        map4_4[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map4_4[STATE] = "0"
        map4_4[OVER_NUM] = "0"
        mData.add(map4_4)

        val map1 = HashMap<String, String>()
        map1[BOSS_SPACE] = "地下一层"
        map1[BOSS_PERIOD] = "2"
        map1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map1[STATE] = "0"
        map1[OVER_NUM] = "0"
        mData.add(map1)

        val map1_2 = HashMap<String, String>()
        map1_2[BOSS_SPACE] = "地下二层"
        map1_2[BOSS_PERIOD] = "2"
        map1_2[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map1_2[STATE] = "0"
        map1_2[OVER_NUM] = "0"
        mData.add(map1_2)

        val map1_3 = HashMap<String, String>()
        map1_3[BOSS_SPACE] = "地下三层"
        map1_3[BOSS_PERIOD] = "2"
        map1_3[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map1_3[STATE] = "0"
        map1_3[OVER_NUM] = "0"
        mData.add(map1_3)

        val map1_4 = HashMap<String, String>()
        map1_4[BOSS_SPACE] = "地下三层BOSS"
        map1_4[BOSS_PERIOD] = "2"
        map1_4[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map1_4[STATE] = "0"
        map1_4[OVER_NUM] = "0"
        mData.add(map1_4)

        val map8 = HashMap<String, String>()
        map8[BOSS_SPACE] = "幽冥三精英"
        map8[BOSS_PERIOD] = "0.5"
        map8[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map8[STATE] = "0"
        map8[OVER_NUM] = "0"
        mData.add(map8)

        val map8_1 = HashMap<String, String>()
        map8_1[BOSS_SPACE] = "幽冥一层"
        map8_1[BOSS_PERIOD] = "2"
        map8_1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map8_1[STATE] = "0"
        map8_1[OVER_NUM] = "0"
        mData.add(map8_1)

        val map8_2 = HashMap<String, String>()
        map8_2[BOSS_SPACE] = "幽冥二层"
        map8_2[BOSS_PERIOD] = "2"
        map8_2[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map8_2[STATE] = "0"
        map8_2[OVER_NUM] = "0"
        mData.add(map8_2)

        val map8_3 = HashMap<String, String>()
        map8_3[BOSS_SPACE] = "幽冥三层BOSS"
        map8_3[BOSS_PERIOD] = "4"
        map8_3[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map8_3[STATE] = "0"
        map8_3[OVER_NUM] = "0"
        mData.add(map8_3)

        val map10 = HashMap<String, String>()
        map10[BOSS_SPACE] = "妖山一层"
        map10[BOSS_PERIOD] = "1"
        map10[BOSS_TIME] = Utils.getYaoOneBsTime()
        map10[STATE] = "1"
        map10[OVER_NUM] = "0"
        mData.add(map10)

        val map12 = HashMap<String, String>()
        map12[BOSS_SPACE] = "藏金阁花"
        map12[BOSS_PERIOD] = "" + 20f / 60
        map12[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map12[STATE] = "0"
        map12[OVER_NUM] = "0"
        mData.add(map12)

        val map12_1 = HashMap<String, String>()
        map12_1[BOSS_SPACE] = "藏金阁BOSS"
        map12_1[BOSS_PERIOD] = "1"
        map12_1[BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis())!!
        map12_1[STATE] = "0"
        map12_1[OVER_NUM] = "0"
        mData.add(map12_1)
    }
}
