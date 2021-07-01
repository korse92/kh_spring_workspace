package com.kh.java.lambda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * BiConsumer<P, P>
 * BiFunction<P, P, R>
 * BiPrecate<P, P>:boolean
 *
 * @author korse
 *
 */
public class LambdaTest2 {

	public static void main(String[] args) {
		LambdaTest2 lt = new LambdaTest2();
//		lt.test1();
//		lt.test2();
//		lt.test3();
//		lt.test4();
//		lt.test5();
		lt.test6();

	}

	/**
	 * @실습문제
	 */
	private void test6() {
		//현재시간을 11:16:24 표시하는 람다식
		Function<Date, String> nowFormatStr = now -> {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String s = sdf.format(now);
			return s;
		};
		System.out.println(nowFormatStr.apply(new Date()));

		//로또(Set<Integer>)를 생성하는 람다식
		Supplier<Set<Integer>> lotto = () -> {
			Set<Integer> set = new HashSet<>();
			for(int i = 0; i < 6; i++) {
				boolean dup = false;
				while(!dup) {
					dup = set.add(new Random().nextInt(45) + 1);
				}
			}

			return set;
		};
		System.out.println(lotto.get());


		//환율계산기 : 3000원 -> ?달러 (1100원이라고 가정)
		Function<Integer, Double> exchange = won -> {
			double rate = 1100;
			return won / rate;
		};
		Double dollar = exchange.apply(3000);
		System.out.println("$" + dollar);

	}

	/**
	 * Runnable : 매개변수 0, 리턴값 0
	 * void run();
	 * 가즈아
	 */
	private void test1() {
		Runnable r = () -> System.out.println("가즈아!");
		r.run();
	}

	/**
	 * Consumer<P> : 매개변수 1, 리턴값 0
	 * void accept(P p);
	 */
	private void test2() {
		//매개인자 하나일 경우, 괄호생략 가능
		Consumer<String> c = name -> System.out.println("이름 : " + name);
		c.accept("홍길동");
	}

	/**
	 * Supplier<R> : 매개변수 0, 리턴값 1
	 * R get();
	 */
	private void test3() {
		Supplier<Integer> rnd1to100 = () -> new Random().nextInt(100) + 1;
		System.out.println(rnd1to100.get());
	}

	/**
	 * Function<P, R> : 매개변수 1, 리턴값 1
	 * R apply(P P);
	 */
	private void test4() {
		Function<String, String> f = name -> name.toUpperCase();
		System.out.println(f.apply("James"));
		System.out.println(f.apply("Bread"));
	}

	/**
	 * Predicate<P> : 매개변수 1, 리턴 boolean
	 * boolean test(P p);
	 *
	 */
	private void test5() {
		Predicate<Integer> isEven = n -> (n % 2 == 0);
		System.out.println(isEven.test(3));
		System.out.println(isEven.test(8));
	}
}
