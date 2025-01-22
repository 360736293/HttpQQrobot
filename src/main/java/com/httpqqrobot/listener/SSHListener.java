package com.httpqqrobot.listener;

import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * SSH端口转发监听器，用于内网穿透，暂时无用
 */
@Slf4j
@Component
public class SSHListener implements ServletContextListener {

    Session session;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
//            log.info("SSH开始建立连接");
//            session = JschUtil.getSession("远程服务器地址", 22, "root", "服务器密码");
//            //mysql
//            JschUtil.bindPort(session, "127.0.0.1", 3306, 3306);
//            //nacos
//            JschUtil.bindPort(session, "127.0.0.1", 8848, 8848);
//            JschUtil.bindPort(session, "127.0.0.1", 9848, 9848);
//            JschUtil.bindPort(session, "127.0.0.1", 9849, 9849);
//            JschUtil.bindPort(session, "127.0.0.1", 7848, 7848);
//            log.info("SSH连接成功");
        } catch (Throwable e) {
            log.error("SSH建立连接失败: ", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        try {
//            log.info("SSH开始断开连接");
//            JschUtil.close(session);
//            log.info("SSH断开连接成功");
        } catch (Throwable e) {
            log.error("SSH断开连接失败: ", e);
        }
    }
}
