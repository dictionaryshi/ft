package com.ft.br.study.source;

import java.util.Arrays;

/**
 * ArrayList
 *
 * @author shichunyang
 */
@SuppressWarnings("unchecked")
public class ArrayList<E> {

    /**
     * 数据结构改变的次数
     */
    protected transient int modCount = 0;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * 默认空数组对象
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    private Object[] elementData;

    private int size;

    public ArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 进行扩容, 最小长度是10
     *
     * @param minCapacity 要扩容的长度
     */
    private void ensureCapacityInternal(int minCapacity) {
        // 如果是空数组的话, 那么扩容量最低是10
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
    }

    /**
     * 对扩容的长度进行校验, 扩容长度必须大于元素个数
     *
     * @param minCapacity 要扩容的长度
     */
    private void ensureExplicitCapacity(int minCapacity) {
        modCount++;

        if (minCapacity - elementData.length > 0) {
            grow(minCapacity);
        }
    }

    /**
     * 对数组进行扩容
     *
     * @param minCapacity 要扩容的长度
     */
    private void grow(int minCapacity) {
        // 数组中元素的个数
        int oldCapacity = elementData.length;
        // 期望扩容长度(数组原始长度 + 数组原始长度 / 2)
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        // 期望扩容长度如果小于指定扩容长度, 那么数组扩容长度为指定扩容长度
        if (newCapacity - minCapacity < 0) {
            newCapacity = minCapacity;
        }
        // 期望扩容长度, 不能超越数组长度边界值(Integer.MAX_VALUE)
        if (newCapacity - MAX_ARRAY_SIZE > 0) {
            newCapacity = hugeCapacity(minCapacity);
        }
        // 对数组进行扩容
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    /**
     * 返回扩容长度的边界值, 扩容长度最大是Integer.MAX_VALUE。
     *
     * @param minCapacity 要扩容的长度
     * @return 边界值
     */
    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

    /**
     * 返回数组中元素的个数
     *
     * @return 元素个数
     */
    public int size() {
        return size;
    }

    /**
     * 判断元素个数是否为0
     *
     * @return true:集合为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 判断是否包含指定元素
     *
     * @param o 指定元素
     * @return true:集合包含指定元素
     */
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * 从左到右查找元素
     *
     * @param o 指定元素
     * @return 指定元素在数组中的索引
     */
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 得到数组
     *
     * @return 数组
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * 返回指定类型的数组
     *
     * @param a   指定的数组
     * @param <T> 类型参数
     * @return 指定类型的数组
     */
    @SuppressWarnings("all")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    /**
     * 根据索引获取元素
     *
     * @param index 数组的索引
     * @return 元素值
     */
    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    /**
     * 返回错误信息
     *
     * @param index 索引值
     * @return 错误信息
     */
    private String outOfBoundsMsg(int index) {
        return "index==>" + index + ", size==>" + size;
    }

    /**
     * 校验索引
     *
     * @param index 索引
     */
    private void rangeCheck(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    /**
     * 校验添加索引(允许最大索引超一位)
     *
     * @param index 索引
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    /**
     * 根据索引获取元素
     *
     * @param index 数组索引
     * @return 元素值
     */
    public E get(int index) {
        rangeCheck(index);

        return elementData(index);
    }

    /**
     * 修改指定索引的元素, 并将老元素返回
     *
     * @param index   索引
     * @param element 修改的新值
     * @return 老元素值
     */
    public E set(int index, E element) {
        rangeCheck(index);

        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    /**
     * 添加元素, 并对数组进行扩容
     *
     * @param e 新添加的元素
     * @return true:添加成功
     */
    public boolean add(E e) {
        // 对数组进行扩容
        ensureCapacityInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    /**
     * 在指定索引处添加元素
     *
     * @param index   索引
     * @param element 新元素
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        // 对数组进行扩容
        ensureCapacityInternal(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1,
                size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * 根据索引删除元素
     *
     * @param index 数组索引
     * @return 被删除的元素
     */
    public E remove(int index) {
        rangeCheck(index);

        modCount++;
        E oldValue = elementData(index);

        // 需要移动的元素个数
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index,
                    numMoved);
        }
        // 让GC回收最后一个位置的元素对象
        elementData[--size] = null;

        return oldValue;
    }

    /**
     * 清空数组, 并让GC回收数组元素对象
     */
    public void clear() {
        modCount++;

        // clear to let GC do its work
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }

        size = 0;
    }
}
