package com.ft.br;

import com.ft.br.service.GoodsService;
import com.ft.redis.annotation.RedisLimit;
import com.ft.util.*;
import com.ft.util.exception.FtException;
import com.ft.util.thread.ThreadPoolUtil;
import com.ft.web.model.MailBO;
import com.ft.web.plugin.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * SpringBoot启动类
 *
 * @author shichunyang
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@EnableTransactionManagement(proxyTargetClass = true)
@RestController
@Slf4j
@EnableScheduling
@EnableRetry
public class FtApplication {

    @Autowired
    private GoodsService goodsService;

    @Value("${server.port}")
    private Integer port;

    @Value("${com.ft.profile}")
    private String profile;

    @GetMapping("/")
    public String helloWorld() {
        com.sun.management.OperatingSystemMXBean osmb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        return JsonUtil.object2Json(
                new HashMap<String, Object>(16) {
                    {
                        put("profile", profile);
                        put("ip", IpUtil.getIp());
                        put("port", port);
                        put("cpu", osmb.getAvailableProcessors());
                        put("os", osmb.getName());
                        put("totalCache", osmb.getTotalPhysicalMemorySize() / 1024 / 1024);
                        put("freeCache", osmb.getFreePhysicalMemorySize() / 1024 / 1024);
                    }
                }
        );
    }

    @RedisLimit(key = "db_master_slave_key", timeout = 5_000L, size = 5, useParam = false)
    @GetMapping("/db")
    public String db() {
        return JsonUtil.object2Json(goodsService.get(1));
    }

    @Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000L, multiplier = 2, maxDelay = 86400000L))
    @GetMapping("/rpc")
    public String rpc() {
        log.info("rpc retry test");
        FtException.throwException("重试机制测试");
        return null;
    }

    @Autowired
    private MailUtil mailUtil;

    @RedisLimit(key = "send_email_key", timeout = 120_000L, size = 1, useParam = false)
    @GetMapping("/mail")
    public String mail(HttpServletRequest request) throws Exception {
        List<List<Object>> rows = new ArrayList<>();

        List<String> columnCHs = new ArrayList<>();
        columnCHs.add("姓名");
        columnCHs.add("年龄");
        columnCHs.add("日期");

        int target = 70;
        for (int i = 1; i <= target; i++) {
            List<Object> columns = new ArrayList<>(16);
            columns.add("史春阳" + i);
            columns.add(i + "");
            columns.add(DateUtil.date2Str(new Date(), DateUtil.DEFAULT_DATE_FORMAT));
            rows.add(columns);
        }

        try (
                ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
                InputStream jpegIn = new FileInputStream("/Users/shichunyang/Downloads/monster.jpeg");
                ByteArrayOutputStream jpegOut = new ByteArrayOutputStream()
        ) {
            String sheetTitle = "人员信息";
            int limit = 17;
            ExcelUtil.createExcel(excelOut, sheetTitle, columnCHs, rows, limit);

            IOUtil.copyLarge(jpegIn, jpegOut, new byte[1024 * 4]);

            MailBO mailBO;
            String cid = "monster";

            Map<String, MailBO> inLines = new HashMap<>(16);
            mailBO = new MailBO();
            mailBO.setContentType(request.getServletContext().getMimeType("*.jpeg"));
            mailBO.setFileOutputStream(jpegOut);
            inLines.put(cid, mailBO);

            Map<String, MailBO> attachments = new HashMap<>(16);
            mailBO = new MailBO();
            mailBO.setContentType(request.getServletContext().getMimeType("*.xls"));
            mailBO.setFileOutputStream(excelOut);
            attachments.put("人员信息.xls", mailBO);

            mailUtil.send(
                    new String[]{"903031015@qq.com"},
                    "903031015@qq.com",
                    "一封测试邮件",
                    "<h1>Hello World</h1> <img src='cid:" + cid + "' />",
                    inLines,
                    attachments
            );
        }

        return "success";
    }

    /**
     * mvn clean package -Pstag -DskipTests=true
     * nohup java -jar ft-0.0.1-SNAPSHOT.jar > /root/temp.out 2>&1 &
     */
    public static void main(String[] args) {
        MDC.put(ThreadLocalMap.REQUEST_ID, "application start");

        // 设置forkJoin线程池大小
        CommonUtil.setForkJoinPoolSize(ThreadPoolUtil.getCpuNumber() * 5);

        ApplicationContext applicationContext = SpringApplication.run(FtApplication.class, args);
        SpringContextUtil.setApplicationContext(applicationContext);

        MDC.remove(ThreadLocalMap.REQUEST_ID);
    }
}
