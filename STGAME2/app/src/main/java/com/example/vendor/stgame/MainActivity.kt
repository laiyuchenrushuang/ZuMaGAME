package com.example.vendor.stgame

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
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import javax.xml.datatype.DatatypeConstants.SECONDS


class MainActivity : AppCompatActivity() {

    var adapter: BossAdapter? = null
    var mData = ArrayList<HashMap<String, String>>()

    var calendar = Calendar.getInstance(Locale.CHINA)
    val REMIND_TIME = 10 * 60 * 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            setStatusBarColor(resources.getColor(R.color.greenEyeColor))
        }
        initData()
        mData.sortWith(BossAdapter.mCompare)
        showLog(GsonUtils.toJson(mData))
        adapter = BossAdapter(this, mData)
        lv_boss_list.adapter = adapter
        bindEvent()
        OpenTask()
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

    private fun OpenTask() {
        val pool = Executors.newScheduledThreadPool(2)//启用2个线程
        val t1 = Task1()
        // 马上运行，任务消耗3秒。运行结束后等待2秒。【有空余线程时】，再次运行该任务
        pool.scheduleWithFixedDelay(t1, 0, 2, TimeUnit.SECONDS)

        // 马上运行，任务消耗5秒，运行结束后等待2秒。【有空余线程时】，再次运行该任务
        val t2 = Task2()
        pool.scheduleWithFixedDelay(t2, 0, 2, TimeUnit.SECONDS)
    }

    inner class Task2 : TimerTask() {

        override fun run() {
            System.out.println("----task2 start--------" + Date().toLocaleString())
            try {
                Thread.sleep(5000)
                val currentTime = System.currentTimeMillis()
                for (index in mData) {
                    if (Utils.dateToStamp(index[Constants.BOSS_TIME]!!) - currentTime < REMIND_TIME) {
                        index[Constants.STATE] = "2"
                        adapter!!.notifyDataSetChanged()
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            System.out.println("----5s later, task2 end--------" + Date().toLocaleString())
        }
    }

    inner class Task1 : TimerTask() {

        override fun run() {
            System.out.println("----task2 start--------" + Date().toLocaleString())
            try {
                Thread.sleep(3000)
                val currentTime = System.currentTimeMillis()
                for (index in mData) {
                    if (Utils.dateToStamp(index[Constants.BOSS_TIME]!!) - currentTime < REMIND_TIME) {
                        index[Constants.STATE] = "2"
                        adapter!!.notifyDataSetChanged()
                    }
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            System.out.println("----5s later, task2 end--------" + Date().toLocaleString())
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
            val map = HashMap<String, String>()
            map[Constants.BOSS_PERIOD] = period.text.toString()
            map[Constants.BOSS_SPACE] = space.text.toString()
            map[Constants.BOSS_TIME] = picker.text.toString()
            map[Constants.STATE] = "1"
            for (index in mData) {
                if (map[Constants.BOSS_SPACE].equals(index[Constants.BOSS_SPACE])) {
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
    private fun showTimePickerDialog(activity: Activity, themeResId: Int, tv: TextView, calendar: Calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        TimePickerDialog(
            activity, themeResId,
            // 绑定监听器
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                var minStr = minute.toString()
                if (minute < 10) {
                    minStr = "0$minute"
                }
                tv.text = "$hourOfDay:$minStr:00"
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
        )// 设置初始时间
            // true表示采用24制
            .show()
    }

    private fun initData() {

        val map = HashMap<String, String>()
        map[Constants.BOSS_SPACE] = "暗之"
        map[Constants.BOSS_PERIOD] = "2"
        map[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map[Constants.STATE] = "0"
        mData.add(map)

        val map1 = HashMap<String, String>()
        map1[Constants.BOSS_SPACE] = "地下"
        map1[Constants.BOSS_PERIOD] = "2"
        map1[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map1[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map1[Constants.STATE] = "0"
        mData.add(map1)

        val map2 = HashMap<String, String>()
        map2[Constants.BOSS_SPACE] = "雪域"
        map2[Constants.BOSS_PERIOD] = "2"
        map2[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map2[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map2[Constants.STATE] = "0"
        mData.add(map2)

        val map3 = HashMap<String, String>()
        map3[Constants.BOSS_SPACE] = "魔窟"
        map3[Constants.BOSS_PERIOD] = "2"
        map3[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map3[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map3[Constants.STATE] = "0"
        mData.add(map3)

        val map4 = HashMap<String, String>()
        map4[Constants.BOSS_SPACE] = "海底"
        map4[Constants.BOSS_PERIOD] = "2"
        map4[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map4[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map4[Constants.STATE] = "0"
        mData.add(map4)

        val map5 = HashMap<String, String>()
        map5[Constants.BOSS_SPACE] = "祖玛"
        map5[Constants.BOSS_PERIOD] = "2"
        map5[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map5[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map5[Constants.STATE] = "0"
        mData.add(map5)

        val map6 = HashMap<String, String>()
        map6[Constants.BOSS_SPACE] = "人鱼"
        map6[Constants.BOSS_PERIOD] = "1"
        map6[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map6[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map6[Constants.STATE] = "0"
        mData.add(map6)

        val map7 = HashMap<String, String>()
        map7[Constants.BOSS_SPACE] = "森林"
        map7[Constants.BOSS_PERIOD] = "2"
        map7[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - map7[Constants.BOSS_PERIOD]!!.toLong() * 60 * 60 * 1000)!!
        map7[Constants.STATE] = "0"
        mData.add(map7)

        val map8 = HashMap<String, String>()
        map8[Constants.BOSS_SPACE] = "幽冥"
        map8[Constants.BOSS_PERIOD] = "0.5"
        map8[Constants.BOSS_TIME] =
            Utils.longToStringData(System.currentTimeMillis() - (map8[Constants.BOSS_PERIOD]!!.toFloat() * 60 * 60 * 1000).toLong())!!
        map8[Constants.STATE] = "0"
        mData.add(map8)
    }
}
