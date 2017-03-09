package com.yarn.service;

import com.yarn.util.Log4jFactory;

/**
 * Created by zhangdi on 2017/02/23.
 */


public class ApplicationMonitor {


    public static void main(String[] args) {
        ApplicationMonitor applicationMonitor = new ApplicationMonitor();
        applicationMonitor.init();
        new Thread(new ApplicationMonitorThread()).start();
    }



    private void init() {
        String logQueryLog4J_Properties = "/var/lib/hadoop-hdfs/zhangd/YarnApplicationMonitor/log4j.properties";// LogQuery.jar使用的log4j配置文件
        // 初始化log4j
        Log4jFactory.initLog4J(logQueryLog4J_Properties);
    }



}
