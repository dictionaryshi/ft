package com.ft;

import com.ft.util.JsonUtil;
import com.ft.model.mdo.UserDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringTest {

	@Autowired
	private AbstractApplicationContext applicationContext;

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
		// 获取系统环境
		ConfigurableEnvironment configurableEnvironment = applicationContext.getEnvironment();
		String osName = configurableEnvironment.getProperty("os.name");
		System.out.println(osName);

		// 获取配置文件中的内容
		String cookieDomain = configurableEnvironment.getProperty("cookieDomain");
		System.out.println(cookieDomain);
	}

	@Test
	public void isSingleton() {
		UserDO user1 = applicationContext.getBean("scy", UserDO.class);
		UserDO user2 = applicationContext.getBean("scy", UserDO.class);
		System.out.println(user1 == user2);

		boolean isSingleton = applicationContext.isSingleton("scy");
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
