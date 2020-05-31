package com.ft.br.java8;

import com.ft.br.model.mdo.Employee;
import com.ft.util.JsonUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.*;

public class LambdaTest {
    private void forEachAccept(Employee employee) {
        System.out.println(JsonUtil.object2Json(employee));
    }

    /**
     * Lambda表达式只能作用于函数式接口的方法体。(只有一个抽象方法的接口就是函数式接口)
     *
     * @FunctionalInterface 用于校验函数式接口
     */
    @Test
    public void sortList() {
        Employee.LIST.sort(Comparator.comparing(Employee::getAge));
        Employee.LIST.forEach(employee -> System.out.println(JsonUtil.object2Json(employee)));

        System.out.println("---");

        Employee.LIST.sort(Comparator.comparing(Employee::getAge).reversed());
        // 方法引用(Lambda的另一种表现形式)
        Employee.LIST.forEach(this::forEachAccept);
    }

    @Test
    public void quote() {
        Comparator<String> comparator = String::compareTo;
        System.out.println(comparator);

        Supplier<Employee> supplier = Employee::new;
        System.out.println(supplier);
    }

    /**
     * 消费型接口
     */
    private <T> void consumer(T data, Consumer<T> consumer) {
        consumer.accept(data);
    }

    @Test
    public void testConsumer() {
        this.consumer(Employee.LIST.get(0), employee -> System.out.println(employee.getName()));
    }

    /**
     * 生产型接口
     */
    private <T> void supplier(List<T> list, Supplier<T> supplier) {
        list.add(supplier.get());
    }

    @Test
    public void testSupplier() {
        List<Employee> employees = new ArrayList<>();

        supplier(employees, () -> new Employee("春阳", 28));
        supplier(employees, () -> new Employee("徐婷", 25));

        System.out.println(JsonUtil.object2Json(employees));
    }

    /**
     * 函数型接口
     */
    private <T, R> R function(T param, Function<T, R> function) {
        return function.apply(param);
    }

    /**
     * Lambda表达式方法体只有一行时, 可以省略return
     */
    @Test
    public void testFunction() {
        int hashCode = this.function(Employee.LIST.get(0), employee -> employee.hashCode() * 10);
        System.out.println(hashCode);
    }

    /**
     * 断言型接口
     */
    private <T> List<T> predicate(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }

        return result;
    }

    @Test
    public void testPredicate() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(this.predicate(list, number -> number > 5));
    }
}
