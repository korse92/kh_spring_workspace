package com.kh.java.lambda;

import java.util.Date;

/**
 * 함수형 프로그래밍
 * - 객체지향의 메소드
 *
 * - 함수 호출시 암시적인 입력값/결과값의 사용을 자제하고,
 * 	 명시적인 입력값/결과값을 사용하려는 프로그래밍
 * - 함수가 일급값 : 인자로 전달, 리턴값으로 함수를 처리
 *
 */
public class LambdaTest1 {

	private long num = new Date().getTime();

	public static void main(String[] args) {
		LambdaTest1 lt = new LambdaTest1();
		lt.test1();
		lt.test2();
		lt.test3();
	}


	/**
	 * 함수형 인터페이스 Koo를 생성
	 * - 추상메서드 : 정수2개를 입력받기.
	 * - 람다표현식생성 : 큰수를 리턴하도록 override하기
	 */
	private void test3() {
		Koo max = (a, b) -> (a > b) ? a : b;
		System.out.println(max.compare(2, 3));
		System.out.println(max.compare(20, 3));

		Koo min = (a, b) -> (a < b) ? a : b;
		System.out.println(min.compare(10, 3));
		System.out.println(min.compare(1, 3));
	}

	@FunctionalInterface
	interface Koo {
		int compare(int a, int b);
	}

	private void test2() {
//		Pita pita = (int a, int b) -> {
//			return Math.sqrt(a * a + b * b);
//		};
		Pita pita = (a, b) -> Math.sqrt(a * a + b * b);

		System.out.println(pita.calc(10, 5));
		System.out.println(pita.calc(30, 50));
	}



	/**
	 * a^2 + b^2 = c^2
	 */
	private void test1() {
		//익명클래스 선언 & 객체생성
		Pita pita = new Pita() {

			@Override
			public double calc(int a, int b) {
				return Math.sqrt(square(a) + square(b));
			}

		};

		System.out.println(pita.calc(10, 5));
		System.out.println(pita.calc(30, 50));
	}

}

/**
 * 함수형 인터페이스
 * @FunctionalInterface는 하나의 추상메소드만 허용
 *
 */
@FunctionalInterface
interface Pita{
	double calc(int a, int b);
//	double max(int a);

	default int square(int x) {
		return x * x;
	}
}
