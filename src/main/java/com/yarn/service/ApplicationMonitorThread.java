package com.yarn.service;

import com.yarn.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.yarn.api.records.ApplicationReport;
import org.apache.hadoop.yarn.api.records.YarnApplicationState;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.exceptions.YarnException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangdi on 2017/02/23.
 */
public class ApplicationMonitorThread implements Runnable {

    @Override
    public void run() {
        while (true) {
            long sleepTime = doMain();
            try {
                TimeUnit.MILLISECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                Log4jFactory.logger_system.error("error", e);
            }
        }
    }


    public long doMain() {
        long sleeptime = 1000 * 60 * 5;
        EmailUtil emailUtil = new EmailUtil();
        List<String> errorMessage = appStatus();

        if (!errorMessage.isEmpty()) {
            errorMessage.add("服务器地址:[" + SystemConfig.server_host + "]");

            StringBuffer sb = new StringBuffer();
            for (String strMessage : errorMessage) {
                sb.append(strMessage + "\n");
            }

            String[] astrCCRecipient = {"123@qq.com"};//抄送人
            String[] astrRecipient = {"234@qzt360.com"};//收件人

            emailUtil.sendSimpleTextEmail(astrRecipient, astrCCRecipient, "警告!Yarn程序异常退出", sb.toString());
            Log4jFactory.logger_system.info("send message! " + sb.toString());
            sleeptime = 1000 * 60 * 60;
        }
        return sleeptime;
    }


    /***
     * 检查Yarn中app的运行状态
     */
    private List<String> appStatus() {

        String[] listMonitor = SystemConfig.monitor_app_names.split(",");

        List<String> rep = new ArrayList<String>();

        YarnClient client = YarnClient.createYarnClient();
        Configuration conf = new Configuration();
        client.init(conf);
        client.start();

        EnumSet<YarnApplicationState> appStates = EnumSet.noneOf(YarnApplicationState.class);
        if (appStates.isEmpty()) {
            appStates.add(YarnApplicationState.RUNNING);
            appStates.add(YarnApplicationState.ACCEPTED);
            appStates.add(YarnApplicationState.SUBMITTED);
        }
        try {
            List<ApplicationReport> appsReport = client.getApplications(appStates);
            Map<String, ApplicationReport> monitorApp = new HashMap<String, ApplicationReport>();

            for (ApplicationReport appReport : appsReport) {
                monitorApp.put(appReport.getName().toLowerCase(), appReport);
                Log4jFactory.logger_system.info("Yarn application = [" + appReport.getName() + "], application id = [" + appReport.getApplicationId() + "]");
            }

            //循环需要监控的列表
            for (String appname : listMonitor) {
                if (monitorApp.containsKey(appname.toLowerCase())) {
                    PubObject.appMap.put(appname.toLowerCase(), monitorApp.get(appname.toLowerCase()).getApplicationId().toString());
                } else {
                    if (PubObject.appMap.containsKey(appname.toLowerCase())) {
                        String strMessage = "应用程序 [" + appname + "] 异常退出，异常程序编号:[" + PubObject.appMap.get(appname.toLowerCase()) + "]，请检查！时间[" + DateUtil.getNowDateFormat("yyyy-MM-dd HH:mm:ss") + "]";
                        rep.add(strMessage);
                        Log4jFactory.logger_system.info(strMessage);
                    } else {
                        String strMessage = "应用程序 [" + appname + "] 没有启动，请尽快启动！时间[" + DateUtil.getNowDateFormat("yyyy-MM-dd HH:mm:ss") + "]";
                        rep.add(strMessage);
                        Log4jFactory.logger_system.info(strMessage);
                    }
                }
            }
        } catch (YarnException e) {
            Log4jFactory.logger_system.error("error:", e);
        } catch (IOException e) {
            Log4jFactory.logger_system.error("error:", e);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                Log4jFactory.logger_system.error("error:", e);
            }
        }
        return rep;
    }
}
