package com.ft.br.study.dom4j;

import com.ft.util.Dom4jUtil;
import org.dom4j.Document;
import org.dom4j.Element;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Dom4j
 *
 * @author shichunyang
 */
public class Dom4jTest {
    public static final String CHINA_FILE_PATH = "src/main/resources/china.xml";
    public static final String DATABASE_FILE_PATH = "src/main/resources/database.xml";

    public void read() throws Exception {
        Document doc = Dom4jUtil.getDocument(new FileInputStream(CHINA_FILE_PATH));
        if (doc == null) {
            return;
        }

        // 获取根节点
        Element root = doc.getRootElement();

        // 获取所有province元素节点
        @SuppressWarnings("unchecked")
        List<Element> provinceElementList = root.elements("province");

        for (Element provinceElement : provinceElementList) {

            System.out.println("省份==>" + provinceElement.attributeValue("name"));

            // 遍历所有city
            @SuppressWarnings("unchecked")
            List<Element> cityElementList = provinceElement.elements("city");

            for (Element cityElement : cityElementList) {
                String city = cityElement.getText();
                System.out.println(city);
            }
            System.out.println("---");
        }

        // 查询单节点
        Element firstProvince = root.element("province");
        Element firstCity = firstProvince.element("city");
        System.out.println("第一个省份/城市==>" + firstProvince.attributeValue("name") + "／" + firstCity.getText());
    }

    public void write() throws Exception {
        Document doc = Dom4jUtil.getDocument(new FileInputStream(DATABASE_FILE_PATH));
        if (doc == null) {
            return;
        }

        Element root = doc.getRootElement();

        Element userElement = root.addElement("user");

        userElement.setText("数据库配置");
        userElement.addAttribute("username", "root");
        userElement.addAttribute("password", "flzx@3qc");

        Dom4jUtil.xmlWrite(new FileOutputStream(DATABASE_FILE_PATH), doc);
    }
}
