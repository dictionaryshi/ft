package com.ft.study.source;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * HashMap
 *
 * @author shichunyang
 */
public class HashMap<K, V> {
	/**
	 * 默认初始容量(必须是2的N次幂), 16
	 */
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

	/**
	 * 最大容量
	 */
	static final int MAXIMUM_CAPACITY = 1 << 30;

	/**
	 * 默认负载因子
	 */
	static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * 未扩容的空表实例
	 */
	static final Entry<?, ?>[] EMPTY_TABLE = {};

	/**
	 * 链表(长度是2的N次幂)
	 */
	transient Entry<K, V>[] table = (Entry<K, V>[]) EMPTY_TABLE;

	/**
	 * 键值对数量
	 */
	transient int size;

	/**
	 * 达到该阙值时发生扩容动作(容量 * 负载因子)
	 */
	int threshold;

	/**
	 * hash表负载因子
	 */
	final float loadFactor;

	transient int modCount;

	public HashMap(int initialCapacity, float loadFactor) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " +
					initialCapacity);
		}
		if (initialCapacity > MAXIMUM_CAPACITY) {
			initialCapacity = MAXIMUM_CAPACITY;
		}
		if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
			throw new IllegalArgumentException("Illegal load factor: " +
					loadFactor);
		}
		this.loadFactor = loadFactor;
		threshold = initialCapacity;
	}

	public HashMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	public HashMap() {
		this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	static class Entry<K, V> implements Map.Entry<K, V> {
		final K key;
		V value;
		Entry<K, V> next;
		int hash;

		/**
		 * Creates new entry.
		 */
		Entry(int h, K k, V v, Entry<K, V> n) {
			value = v;
			next = n;
			key = k;
			hash = h;
		}

		@Override
		public final K getKey() {
			return key;
		}

		@Override
		public final V getValue() {
			return value;
		}

		@Override
		public final V setValue(V newValue) {
			V oldValue = value;
			value = newValue;
			return oldValue;
		}

		@Override
		public final boolean equals(Object o) {
			if (!(o instanceof Map.Entry)) {
				return false;
			}
			Map.Entry e = (Map.Entry) o;
			Object k1 = getKey();
			Object k2 = e.getKey();
			if (k1 == k2 || (k1 != null && k1.equals(k2))) {
				Object v1 = getValue();
				Object v2 = e.getValue();
				if (v1 == v2 || (v1 != null && v1.equals(v2))) {
					return true;
				}
			}
			return false;
		}

		@Override
		public final int hashCode() {
			return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
		}

		@Override
		public final String toString() {
			return getKey() + "=" + getValue();
		}

		void recordAccess(HashMap<K, V> m) {
		}

		void recordRemoval(HashMap<K, V> m) {
		}
	}

	private void inflateTable(int toSize) {
		// 要扩容的大小
		int capacity = roundUpToPowerOf2(toSize);

		// 下次要扩容的阙值
		threshold = (int) Math.min(capacity * loadFactor, MAXIMUM_CAPACITY + 1);
		table = new Entry[capacity];
	}

	/**
	 * 计算要扩容的大小
	 */
	private static int roundUpToPowerOf2(int number) {
		return number >= MAXIMUM_CAPACITY
				? MAXIMUM_CAPACITY
				: (number > 1) ? Integer.highestOneBit((number - 1) << 1) : 1;
	}

	final int hash(Object k) {
		int h = 0;

		h ^= k.hashCode();

		h ^= (h >>> 20) ^ (h >>> 12);

		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) {
		return h & (length - 1);
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public V get(Object key) {
		if (key == null) {
			return getForNullKey();
		}
		Entry<K, V> entry = getEntry(key);

		return null == entry ? null : entry.getValue();
	}

	private V getForNullKey() {
		if (size == 0) {
			return null;
		}
		for (Entry<K, V> e = table[0]; e != null; e = e.next) {
			if (e.key == null) {
				return e.value;
			}
		}
		return null;
	}

	public boolean containsKey(Object key) {
		return getEntry(key) != null;
	}

	final Entry<K, V> getEntry(Object key) {
		if (size == 0) {
			return null;
		}

		int hash = (key == null) ? 0 : hash(key);
		for (Entry<K, V> e = table[indexFor(hash, table.length)];
			 e != null;
			 e = e.next) {
			Object k;
			if (e.hash == hash &&
					((k = e.key) == key || (key != null && key.equals(k)))) {
				return e;
			}
		}
		return null;
	}

	public V put(K key, V value) {
		if (table == EMPTY_TABLE) {
			// 初始化hash表(计算扩容阙值)
			inflateTable(threshold);
		}
		if (key == null) {
			return putForNullKey(value);
		}
		int hash = hash(key);
		int i = indexFor(hash, table.length);
		for (Entry<K, V> e = table[i]; e != null; e = e.next) {
			Object k;
			if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
				V oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}

		modCount++;
		addEntry(hash, key, value, i);
		return null;
	}

	private V putForNullKey(V value) {
		for (Entry<K, V> e = table[0]; e != null; e = e.next) {
			if (e.key == null) {
				V oldValue = e.value;
				e.value = value;
				e.recordAccess(this);
				return oldValue;
			}
		}
		modCount++;
		addEntry(0, null, value, 0);
		return null;
	}

	void addEntry(int hash, K key, V value, int bucketIndex) {
		if ((size >= threshold) && (null != table[bucketIndex])) {
			resize(2 * table.length);
			hash = (null != key) ? hash(key) : 0;
			bucketIndex = indexFor(hash, table.length);
		}

		createEntry(hash, key, value, bucketIndex);
	}

	void createEntry(int hash, K key, V value, int bucketIndex) {
		Entry<K, V> e = table[bucketIndex];
		table[bucketIndex] = new Entry<>(hash, key, value, e);
		size++;
	}

	void resize(int newCapacity) {
		Entry[] oldTable = table;
		int oldCapacity = oldTable.length;
		if (oldCapacity == MAXIMUM_CAPACITY) {
			threshold = Integer.MAX_VALUE;
			return;
		}

		Entry[] newTable = new Entry[newCapacity];
		transfer(newTable);
		table = newTable;
		threshold = (int) Math.min(newCapacity * loadFactor, MAXIMUM_CAPACITY + 1);
	}

	void transfer(Entry[] newTable) {
		int newCapacity = newTable.length;
		for (Entry<K, V> e : table) {
			while (null != e) {
				Entry<K, V> next = e.next;
				int i = indexFor(e.hash, newCapacity);
				e.next = newTable[i];
				newTable[i] = e;
				e = next;
			}
		}
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		int numKeysToBeAdded = m.size();
		if (numKeysToBeAdded == 0) {
			return;
		}

		if (table == EMPTY_TABLE) {
			inflateTable((int) Math.max(numKeysToBeAdded * loadFactor, threshold));
		}

		if (numKeysToBeAdded > threshold) {
			int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
			if (targetCapacity > MAXIMUM_CAPACITY) {
				targetCapacity = MAXIMUM_CAPACITY;
			}
			int newCapacity = table.length;
			while (newCapacity < targetCapacity) {
				newCapacity <<= 1;
			}
			if (newCapacity > table.length) {
				resize(newCapacity);
			}
		}

		for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	public V remove(Object key) {
		Entry<K, V> e = removeEntryForKey(key);
		return (e == null ? null : e.value);
	}

	final Entry<K, V> removeEntryForKey(Object key) {
		if (size == 0) {
			return null;
		}
		int hash = (key == null) ? 0 : hash(key);
		int i = indexFor(hash, table.length);
		Entry<K, V> prev = table[i];
		Entry<K, V> e = prev;

		while (e != null) {
			Entry<K, V> next = e.next;
			Object k;
			if (e.hash == hash &&
					((k = e.key) == key || (key != null && key.equals(k)))) {
				modCount++;
				size--;
				if (prev == e) {
					table[i] = next;
				} else {
					prev.next = next;
				}
				e.recordRemoval(this);
				return e;
			}
			prev = e;
			e = next;
		}

		return e;
	}

	public void clear() {
		modCount++;
		Arrays.fill(table, null);
		size = 0;
	}

	public boolean containsValue(Object value) {
		if (value == null) {
			return containsNullValue();
		}

		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++) {
			for (Entry e = tab[i]; e != null; e = e.next) {
				if (value.equals(e.value)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean containsNullValue() {
		Entry[] tab = table;
		for (int i = 0; i < tab.length; i++) {
			for (Entry e = tab[i]; e != null; e = e.next) {
				if (e.value == null) {
					return true;
				}
			}
		}
		return false;
	}
}
