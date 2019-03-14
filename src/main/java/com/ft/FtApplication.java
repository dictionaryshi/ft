package com.ft;

import com.ft.config.study.ProFile;
import com.ft.constant.PropertiesConstant;
import com.ft.service.GoodsService;
import com.ft.util.ExcelUtil;
import com.ft.util.JsonUtil;
import com.ft.util.LogHolder;
import com.ft.util.SpringContextUtil;
import com.ft.web.cloud.hystrix.ThreadLocalHystrixConcurrencyStrategy;
import com.ft.web.exception.FtException;
import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.internet.MimeMessage;
import javax.servlet.MultipartConfigElement;
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
@EnableFeignClients
@EnableHystrix
public class FtApplication {
	@Bean
	public IRule iRule() {
		return new RoundRobinRule();
	}

	private final PropertiesConstant propertiesConstants;
	private final ProFile proFile;

	@Autowired
	public FtApplication(
			PropertiesConstant propertiesConstants,
			ProFile proFile
	) {
		this.propertiesConstants = propertiesConstants;
		this.proFile = proFile;
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
						put("pro_file", proFile.proFile());
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

	private ThreadLocal<Long> threadTime = new ThreadLocal<>();

	@Retryable(value = {Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 5000L, multiplier = 2, maxDelay = 86400000L))
	@GetMapping("/rpc")
	public String rpc() {
		if (threadTime.get() != null) {
			log.info("rpc test, time==>{}", System.currentTimeMillis() - threadTime.get());
		} else {
			threadTime.set(System.currentTimeMillis());
		}

		throw new FtException(500, "重试机制测试");
	}

	//@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/mail")
	public String mail(HttpServletRequest request) throws Exception {
		MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMailMessage, true);

		mimeMessageHelper.setTo("shichunyang@ft.com");
		mimeMessageHelper.setFrom("903031015@qq.com");
		mimeMessageHelper.setSubject("一封测试邮件");

		String cid = "monster";
		mimeMessageHelper.setText("<h1>Hello World</h1> <img src='cid:" + cid + "' />", true);

		String sheetTitle = "人员信息";

		String nameKey = "姓名";
		String ageKey = "年龄";
		String dateKey = "日期";
		List<String> columnChs = Arrays.asList(nameKey, ageKey, dateKey);

		List<Map<String, Object>> dataList = new ArrayList<>();

		int target = 100;
		int limit = 10;
		for (int i = 0; i < target; i++) {
			Map<String, Object> scy = new HashMap<>(16);
			scy.put(nameKey, "史春阳");
			scy.put(ageKey, i + 1);
			scy.put(dateKey, new Date());
			dataList.add(scy);
		}

		ByteArrayOutputStream excelOut = new ByteArrayOutputStream();
		ExcelUtil.createExcel(excelOut, sheetTitle, columnChs, dataList, limit);

		ByteArrayOutputStream jpegOut = new ByteArrayOutputStream();
		InputStream jpegIn = new FileInputStream("/Users/shichunyang/Downloads/monster.jpeg");
		IOUtils.copy(jpegIn, jpegOut);
		jpegIn.close();

		mimeMessageHelper.addInline(cid, new ByteArrayResource(jpegOut.toByteArray()), request.getServletContext().getMimeType("*.jpeg"));
		mimeMessageHelper.addAttachment("人员信息.xls", new ByteArrayResource(excelOut.toByteArray()), request.getServletContext().getMimeType("*.xls"));

		jpegOut.close();
		excelOut.close();

		javaMailSender.send(mimeMailMessage);
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
	 * 文件上传配置
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.parse("5MB"));
		/// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("20MB"));
		return factory.createMultipartConfig();
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
