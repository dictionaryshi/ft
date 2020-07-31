package com.ft.br.java8;

import com.ft.br.model.mdo.Employee;
import com.ft.dao.stock.model.UserDO;
import com.ft.util.JsonUtil;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamTest {

    /**
     * 根据数组创建Stream对象
     */
    @Test
    public void createByArray() {
        Employee[] employees = Employee.LIST.toArray(new Employee[]{});
        Stream<Employee> employeeStream = Arrays.stream(employees);
        employeeStream.forEach(System.out::println);
    }

    /**
     * 使用对象创建Stream对象
     */
    @Test
    public void createByStream() {
        Stream<Employee> employeeStream = Stream.of(new Employee("张三", 30), new Employee("李四", 20));
        employeeStream.forEach(System.out::println);
    }

    @Test
    public void createByList() {
        Stream<Employee> employeeStream = Employee.LIST.stream();

        // skip 表示跳过多少个元素。distinct 根据元素的hashCode和equals方法来去重
        Stream<Employee> employeeNewStream = employeeStream.filter((employee) -> employee.getAge() > 0).limit(5).skip(0).distinct();
        employeeNewStream.forEach(System.out::println);
    }

    /**
     * 映射
     */
    @Test
    public void map() {
        Employee.LIST.stream().map(employee -> employee.getAge() * 10).forEach(System.out::println);

        System.out.println("---");

        String[] strArr = {"1a2", "3b4", "5c6", "7d8"};
        // 将流映射成另一个流
        Stream<Integer> characterStream = Arrays.stream(strArr).flatMap(str -> {
            List<Integer> numberList = new ArrayList<>();
            for (Character ch : str.toCharArray()) {
                if (Character.isDigit(ch)) {
                    numberList.add(Integer.parseInt(ch + ""));
                }
            }
            return numberList.stream();
        });

        characterStream.forEach(System.out::println);
    }

    /**
     * 排序
     */
    @Test
    public void sorted() {
        Employee.LIST.stream().sorted(Comparator.comparing(Employee::getAge).reversed()).forEach(System.out::println);
    }

    @Test
    public void streamApi() {
        // 是否全部匹配
        boolean isAllMatch = Employee.LIST.stream().allMatch(employee -> employee.getAge() > 5);
        System.out.println(isAllMatch);

        // 至少匹配一个元素
        boolean isAnyMatch = Employee.LIST.stream().anyMatch(employee -> employee.getAge() > 40);
        System.out.println(isAnyMatch);

        // 最大元素
        Optional<Employee> maxEmployee = Employee.LIST.stream().max(Comparator.comparing(Employee::getAge));
        // 最小元素
        Optional<Employee> minEmployee = Employee.LIST.stream().min(Comparator.comparing(Employee::getAge));

        System.out.println(maxEmployee.orElse(null));
        System.out.println(minEmployee.orElse(null));
    }

    @Test
    public void reduce() {
        Optional<Integer> optionalInteger = Employee.LIST.stream().map(Employee::getAge).reduce((e1, e2) -> e1 + e2);
        System.out.println(optionalInteger.orElse(null));
    }

    @Test
    public void collect() {
        List<String> names = Employee.LIST.stream().map(Employee::getName).collect(Collectors.toCollection(LinkedList::new));
        names.forEach(System.out::println);
    }

    @Test
    public void mapCollect() {
        int sumAge = Employee.LIST.stream().mapToInt(Employee::getAge).sum();
        System.out.println(sumAge);

        Double averageAge = Employee.LIST.stream().collect(Collectors.averagingDouble(Employee::getAge));
        System.out.println(averageAge);
    }

    @Test
    public void groupingBy() {
        List<Employee> list = Employee.LIST;

        Map<Integer, List<Employee>> listMap = list.stream().collect(Collectors.groupingBy(Employee::getAge));
        System.out.println(JsonUtil.object2Json(listMap));

        Map<Integer, Set<String>> nameMap = list.stream().collect(Collectors.groupingBy(Employee::getAge, Collectors.mapping(Employee::getName, Collectors.toSet())));
        System.out.println(JsonUtil.object2Json(nameMap));

        Map<Integer, Map<String, List<Employee>>> groupMap = list.stream().collect(Collectors.groupingBy(Employee::getAge, Collectors.groupingBy(Employee::getName)));
        System.out.println(JsonUtil.object2Json(groupMap));
    }

    /**
     * 分区
     */
    @Test
    public void partitioningBy() {
        Map<Boolean, List<Employee>> map = Employee.LIST.stream()
                .collect(Collectors.partitioningBy(employee -> employee.getAge() > 30));
        System.out.println(JsonUtil.object2Json(map));
    }

    /**
     * 聚合操作
     */
    @Test
    public void summarizingInt() {
        IntSummaryStatistics intSummaryStatistics = Employee.LIST.stream().collect(Collectors.summarizingInt(Employee::getAge));
        System.out.println(intSummaryStatistics.getAverage());
        System.out.println(intSummaryStatistics.getCount());
        System.out.println(intSummaryStatistics.getMax());
        System.out.println(intSummaryStatistics.getMin());
        System.out.println(intSummaryStatistics.getSum());
    }

    @Test
    public void joining() {
        String str = Employee.LIST.stream().map(Employee::getName)
                .collect(Collectors.joining(",", "start", "end"));
        System.out.println(str);
    }

    @Test
    public void orElse() {
        Integer[] arr = {1, 2, 3};

        Integer result = Stream.of(arr)
                .filter(number -> number == 2)
                .findFirst()
                .orElse(null);

        System.out.println(result);
    }

    @Test
    public void initCollection() {
        Set<String> urls = new HashSet<String>() {
            {
                add("/health-check");
                add("/error");
                add("/disable");
            }
        };
        System.out.println(urls);

        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("username", "scy");
                put("age", 27);
            }
        };
        System.out.println(map);
    }

    @Test
    public void merge() {
        Map<String, Integer> counter = new HashMap<String, Integer>() {
            {
                put("A", 100);
                put("B", 200);
                put("C", 300);
            }
        };
        counter.merge("A", 400, (oldValue, newValue) -> oldValue + newValue);

        counter.putIfAbsent("A", 0);

        counter.forEach((key, value) -> System.out.println(key + "_" + value));

        System.out.println(counter.getOrDefault("D", 666));
    }

    @Test
    public void toMap() {
        UserDO u1 = new UserDO();
        u1.setId(1);
        u1.setUsername("小一");

        UserDO u2 = new UserDO();
        u2.setId(2);
        u2.setUsername("小二");

        UserDO u3 = new UserDO();
        u3.setId(3);
        u3.setUsername("小三");

        UserDO u4 = new UserDO();
        u4.setId(2);
        u4.setUsername("小四");

        Map<Integer, UserDO> userMap = Stream.of(u1, u2, u3, u4).collect(Collectors.toMap(UserDO::getId, Function.identity(), (oldValue, newValue) -> newValue));
        System.out.println(JsonUtil.object2Json(userMap));
    }

    @Test
    public void range() {
        IntStream.range(0, 10).forEach(System.out::println);
    }
}
