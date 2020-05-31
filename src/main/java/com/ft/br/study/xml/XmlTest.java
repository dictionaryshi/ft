package com.ft.br.study.xml;

import com.ft.br.model.mdo.City;
import com.ft.br.model.mdo.Province;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

/**
 * XmlTest
 *
 * @author shichunyang
 */
public class XmlTest {
    public static void main(String[] args) {
        Province ln = new Province(1, "辽宁省");
        ln.addCity(new City(30, "沈阳", "shenyang"));
        ln.addCity(new City(31, "大连", "dalian"));

        Province hlj = new Province(2, "黑龙江省");
        hlj.addCity(new City(32, "伊春", "森林公园"));
        hlj.addCity(new City(33, "鹤岗", "煤矿"));

        List<Province> provinces = new ArrayList<>();
        provinces.add(ln);
        provinces.add(hlj);

        XStream xStream = new XStream();

        // 指定标签的别名
        xStream.alias("china", List.class);
        xStream.alias("province", Province.class);
        xStream.alias("city", City.class);

        // 将Province的name属性变成province标签的属性
        xStream.useAttributeFor(Province.class, "name");
        // 隐藏cities标签节点(隐藏不需要的节点)
        xStream.addImplicitCollection(Province.class, "cities");
        // 让City类的description属性不生成在XML中
        xStream.omitField(City.class, "description");

        String xml = xStream.toXML(provinces);

        System.out.println(xml);
    }
}
