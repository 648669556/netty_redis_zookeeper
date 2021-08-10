package com.crazymakercircle.pool;

import com.crazymakercircle.util.Logger;

import java.util.concurrent.*;

/**
 * Created by 尼恩 at 疯狂创客圈
 */

public class JavaFuturePoolDemo {

    public static final int SLEEP_GAP = 500;


    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }

    static class HotWaterJob implements Callable<Boolean> //①
    {

        @Override
        public Boolean call() throws Exception //②
        {

            try {
                Logger.tcfo("洗好水壶");
                Logger.tcfo("灌上凉水");
                Logger.tcfo("放在火上");

                //线程睡眠一段时间，代表烧水中
                Thread.sleep(SLEEP_GAP);
                Logger.tcfo("水开了");

            } catch (InterruptedException e) {
                Logger.tcfo(" 发生异常被中断.");
                return false;
            }
            Logger.tcfo(" 运行结束.");

            return true;
        }
    }

    static class WashJob implements Callable<Boolean> {

        @Override
        public Boolean call() throws Exception {


            try {
                Logger.tcfo("洗茶壶");
                Logger.tcfo("洗茶杯");
                Logger.tcfo("拿茶叶");
                //线程睡眠一段时间，代表清洗中
                Thread.sleep(SLEEP_GAP);
                Logger.tcfo("洗完了");

            } catch (InterruptedException e) {
                Logger.tcfo(" 清洗工作 发生异常被中断.");
                return false;
            }
            Logger.tcfo(" 清洗工作  运行结束.");
            return true;
        }

    }


    public static void main(String args[]) {

        Callable<Boolean> hJob = new HotWaterJob();//异步逻辑

        Callable<Boolean> wJob = new WashJob();//异步逻辑

        ExecutorService pool =
                Executors.newFixedThreadPool(10);


//        new ThreadPoolExecutor(
//                corePoolSize, maximumPoolSize,keepAliveTime, milliseconds,runnableTaskQueue, threadFactory,serverHandler);

        Future<Boolean> hTask = pool.submit(hJob);
        Future<Boolean> wTask = pool.submit(wJob);


        try {

            boolean waterOk = hTask.get();
            boolean cupOk = wTask.get();

//            hThread.join();
//            wThread.join();


            Thread.currentThread().setName("主线程");
            if (waterOk && cupOk) {
                Logger.tcfo("泡茶喝");
            } else if (!waterOk) {
                Logger.tcfo("烧水失败，没有茶喝了");
            } else if (!cupOk) {
                Logger.tcfo("杯子洗不了，没有茶喝了");
            }

        } catch (InterruptedException e) {
            Logger.tcfo(getCurThreadName() + "发生异常被中断.");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Logger.tcfo(getCurThreadName() + " 运行结束.");

        pool.shutdown();
    }
}