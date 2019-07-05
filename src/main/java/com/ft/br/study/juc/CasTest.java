package com.ft.br.study.juc;

import com.ft.util.exception.FtException;
import com.ft.web.model.UserDO;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS(Compare and Swap):比较并替换。
 *
 * @author shichunyang
 */
@Slf4j
public class CasTest {
	public static final Unsafe UNSAFE;
	private static final long USER_NAME_OFFSET;

	static {
		try {
			Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);

			USER_NAME_OFFSET = UNSAFE.objectFieldOffset
					(UserDO.class.getDeclaredField("username"));
		} catch (Exception e) {
			log.error("cas init exception==>{}", FtException.getExceptionStack(e));
			FtException.throwException(e.getMessage());
			throw new RuntimeException("系统异常");
		}
	}

	private static boolean compareAndSetUsername(UserDO userDO, String expect, String update) {
		return UNSAFE.compareAndSwapObject(userDO, USER_NAME_OFFSET, expect, update);
	}

	/**
	 * CAS缺点:
	 * 1.CPU消耗大。
	 * 2.只能操作一个变量。
	 * 3.ABA问题。
	 */
	public static void main(String[] args) {
		AtomicLong atomicLong = new AtomicLong(100L);
		boolean flag1 = atomicLong.compareAndSet(100L, 500L);
		boolean flag2 = atomicLong.compareAndSet(300L, 700L);

		UserDO userDO = new UserDO();
		userDO.setUsername("春阳");
		boolean flag3 = compareAndSetUsername(userDO, "春阳", "xt");

		UserDO z3 = new UserDO();
		z3.setUsername("xt");

		UserDO l4 = new UserDO();
		l4.setUsername("cy");

		AtomicReference<UserDO> atomicReference = new AtomicReference<>(z3);
		boolean flag4 = atomicReference.compareAndSet(z3, l4);

		AtomicStampedReference<String> atomicStampedReference = new AtomicStampedReference<>("cy", 1);
		int oldStamp = atomicStampedReference.getStamp();
		boolean flag5 = atomicStampedReference.compareAndSet("cy", "xt", oldStamp, oldStamp + 1);

		System.out.println();
	}
}
