package com.ft.br.study.genericity;

import com.ft.br.model.mdo.Employee;
import com.ft.util.ClassUtil;

/**
 * BaseParent
 *
 * @author shichunyang
 */
public class BaseParent<T> {
    public BaseParent() {
        // 获取子类传递的参数化类型
        Class<Employee> beanClass = ClassUtil.resolveGenericType(this.getClass());
        System.out.println(beanClass.getName());
    }
}
