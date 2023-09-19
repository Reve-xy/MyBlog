package cn.ly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 简要描述
 *
 * @author : Rêve
 * @version : 1.0
 * @date : 2023/4/5 15:55
 */
@SpringBootApplication
@MapperScan("cn.ly.*.mapper")
@EnableConfigurationProperties
@EnableScheduling
@EnableTransactionManagement
public class LyBlogApplication {
    public static void main(String[] args) {
        System.setProperty("rocketmq.client.logUseSlf4j", "true");
        SpringApplication.run(LyBlogApplication.class,args);
    }
}
