package com.ft.br;

import com.ft.br.constant.PropertiesConstant;
import com.ft.br.service.GoodsService;
import com.ft.util.*;
import com.ft.util.exception.FtException;
import com.ft.web.cloud.hystrix.ThreadLocalHystrixConcurrencyStrategy;
import com.ft.web.plugin.MailUtil;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
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
@EnableTransactionManagement(proxyTargetClass = true)
@RestController
@Slf4j
@EnableScheduling
@EnableRetry
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.ft")
@EnableHystrix
public class FtApplication {
	@Bean
	public IRule iRule() {
		return new RoundRobinRule();
	}

	private final PropertiesConstant propertiesConstants;

	public FtApplication(
			PropertiesConstant propertiesConstants
	) {
		this.propertiesConstants = propertiesConstants;
	}

	@Autowired
	private GoodsService goodsService;

	@Value("${spring.cloud.client.ip-address}")
	private String ip;

	@Value("${server.port}")
	private Integer port;

	@GetMapping("/")
	public String helloWorld() {
		com.sun.management.OperatingSystemMXBean osmb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		return JsonUtil.object2Json(
				new HashMap<String, Object>(16) {
					{
						put("env", propertiesConstants.getConstant());
						put("ip", ip);
						put("port", port);
						put("cpu", osmb.getAvailableProcessors());
						put("os", osmb.getName());
						put("totalCache", osmb.getTotalPhysicalMemorySize() / 1024 / 1024);
						put("freeCache", osmb.getFreePhysicalMemorySize() / 1024 / 1024);
					}
				}
		);
	}

	@GetMapping("/db")
	public String db() {
		return JsonUtil.object2Json(goodsService.get(1L));
	}

	@Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000L, multiplier = 2, maxDelay = 86400000L))
	@GetMapping("/rpc")
	public String rpc() {
		log.info("rpc retry test");
		throw new FtException(500, "重试机制测试");
	}

	@Autowired
	private MailUtil mailUtil;

	@GetMapping("/mail")
	public String mail(HttpServletRequest request) throws Exception {
		String cid = "monster";

		String sheetTitle = "人员信息";
		String nameKey = "姓名";
		String ageKey = "年龄";
		String dateKey = "日期";
		List<String> columnChs = Arrays.asList(nameKey, ageKey, dateKey);
		List<Map<String, Object>> dataList = new ArrayList<>();

		int target = 70;
		int limit = 17;
		for (int i = 0; i < target; i++) {
			Map<String, Object> scy = new HashMap<>(16);
			scy.put(nameKey, "史春阳");
			scy.put(ageKey, i + 1);
			scy.put(dateKey, new Date());
			dataList.add(scy);
		}

		try (
				ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
				InputStream jpegIn = new FileInputStream("/Users/shichunyang/Downloads/monster.jpeg");
				ByteArrayOutputStream jpegOut = new ByteArrayOutputStream()
		) {
			ExcelUtil.createExcel(excelOut, sheetTitle, columnChs, dataList, limit);

			IOUtil.copyLarge(jpegIn, jpegOut, new byte[1024 * 4]);

			Map<String, Object[]> inLines = new HashMap<>(16);
			inLines.put(cid, new Object[]{jpegOut, request.getServletContext().getMimeType("*.jpeg")});

			Map<String, Object[]> attachments = new HashMap<>(16);
			attachments.put("人员信息.xls", new Object[]{excelOut, request.getServletContext().getMimeType("*.xls")});
			mailUtil.send(
					new String[]{"903031015@qq.com"},
					"903031015@qq.com",
					"一封测试邮件",
					"<h1>Hello World</h1> <img src='cid:" + cid + "' />",
					inLines,
					attachments
			);
		} catch (Exception e) {
			log.error("mail exception==>{}", FtException.getExceptionStack(e));
		}

		return "success";
	}

	@Autowired
	private DiscoveryClient discoveryClient;

	@GetMapping("/discovery")
	public String discovery() {
		List<String> services = discoveryClient.getServices();
		System.out.println(JsonUtil.object2Json(services));
		List<ServiceInstance> serviceInstances = discoveryClient.getInstances("business");
		return JsonUtil.object2Json(serviceInstances);
	}

	/**
	 * mvn clean package -Pstag -DskipTests=true
	 * nohup java -jar ft-0.0.1-SNAPSHOT.jar > /root/temp.out 2>&1 &
	 */
	public static void main(String[] args) {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");

		MDC.put(LogHolder.REQUEST_ID, "application start");

		// 托管hystrix线程池
		HystrixPlugins.getInstance().registerConcurrencyStrategy(new ThreadLocalHystrixConcurrencyStrategy());

		ApplicationContext applicationContext = SpringApplication.run(FtApplication.class, args);
		SpringContextUtil.setApplicationContext(applicationContext);
	}
}
