package cn.ly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 简要描述
 * admin主启动类
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/14 10:51
 */
@SpringBootApplication
@MapperScan("cn.ly.*.mapper")
@EnableTransactionManagement
public class LyAdminApplication {
    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        SpringApplication.run(LyAdminApplication.class,args);
    }
}
