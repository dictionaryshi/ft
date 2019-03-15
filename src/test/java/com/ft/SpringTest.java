package com.ft;

import com.ft.model.mdo.UserDO;
import com.ft.util.JsonUtil;
import com.ft.util.SpringContextUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void count() {

		// 遍历容器中所有bean的名称
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		for (String beanName : allBeanNames) {
			System.out.println(beanName);
		}

		// 容器中bean的数量
		int beanCount = applicationContext.getBeanDefinitionCount();
		System.out.println(beanCount);
	}

	@Test
	public void getEnvironment() {
		// 获取配置文件中的内容
		String cookieDomain = SpringContextUtil.getProperty(applicationContext.getEnvironment(), "cookieDomain");
		System.out.println(cookieDomain);
	}

	@Test
	public void isSingleton() {
		SpringContextUtil.setApplicationContext(applicationContext);
		boolean isSingleton = SpringContextUtil.isSingleton("scy");
		System.out.println(isSingleton);
	}

	@Test
	public void bean() {
		// 查看指定bean在容器中的名字
		String[] userNames = applicationContext.getBeanNamesForType(UserDO.class);
		for (String userName : userNames) {
			System.out.println(userName);
		}

		System.out.println("-----");

		Map<String, UserDO> userMap = applicationContext.getBeansOfType(UserDO.class);
		System.out.println(JsonUtil.object2Json(userMap));
	}
}
