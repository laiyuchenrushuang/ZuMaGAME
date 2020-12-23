# ZuMaGAME

祖玛神途游戏，BOSS刷新时间助手，哈哈哈。。。。

定时器每5秒发任务，不一定一直有效，家里的那个华为荣耀8，跑一会，定时器不监听了，很是蛋疼，然后 我用了一个while去循环捕捉数据更新状态，代码没提交上来的原因是 这个线程池用着规范一些，巩固一下知识。


软件使用 控制 



        private void getNetTime1() {
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            //url = new URL("http://www.bjtime.cn");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            final String format = formatter.format(calendar.getTime());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   String time = "2020-12-25 09:56:08";
                   long one = StringUtils.date2Stamp(time);

                   long two = StringUtils.date2Stamp(format);

                   Log.d("lylog","  one = "+one);
                   Log.d("lylog","  two = "+two);

                   if(two >= one){
                       Toast.makeText(MainActivity.this, "当前软件试用期已经到"  , Toast.LENGTH_SHORT).show();
                       finish();
                   }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "当前手机网络有故障"  , Toast.LENGTH_SHORT).show();

            finish();
        }
    }
    
    
    如果 报错 ---->     Cleartext HTTP traffic to www.baidu.com not permitted
    
     <application
     ....
        android:usesCleartextTraffic="true"...>
        ....
        </application>
        
