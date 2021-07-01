package com.kh.java.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class LambdaTest3 {

	public static void main(String[] args) {
		LambdaTest3 lt = new LambdaTest3();
//		lt.test1();
//		lt.test2();

		lt.test3();
	}

	private void test3() {
		List<Integer> list = Arrays.asList(1,2,3,4,5);
		list.forEach(System.out::println);
	}

	private void test2() {
		//Mask객체를 생성하는 람다식을 작성하세요.
		//name:String
		Function<String, Mask> maskConstr1 = Mask::new;
		//color:String, kf:int
		BiFunction<String, Integer, Mask> maskConstr2 = Mask::new;

		Mask mask1 = maskConstr1.apply("조은마스크");
		Mask mask2 = maskConstr2.apply("White", 94);
		Mask mask3 = maskConstr2.apply("Black", 94);
		Mask mask4 = maskConstr2.apply("White", 94);

		System.out.println(mask1);
		System.out.println(mask2);
		System.out.println(mask3);
		System.out.println(mask4);

		//equals
		//2개의 인자를 받는 equals람다식
		BiPredicate<Mask, Mask> maskEquals1 = Mask::equals;
		//특정mask객체 기준으로 1개의 인자를 받는 equals람다식
		Predicate<Mask> maskEquals2 = mask2::equals;
		System.out.println(maskEquals1.test(mask2, mask4)); // t
		System.out.println(maskEquals1.test(mask2, mask3)); //f
		System.out.println(maskEquals2.test(mask4)); // t
		System.out.println(maskEquals2.test(mask1)); // f
	}

	class Mask {
		private String name;
		private String color;
		private int kf; // 94 80

		public Mask(String name) {
			super();
			this.name = name;
		}

		public Mask(String color, int kf) {
			super();
			this.color = color;
			this.kf = kf;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getEnclosingInstance().hashCode();
			result = prime * result + ((color == null) ? 0 : color.hashCode());
			result = prime * result + kf;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Mask other = (Mask) obj;
			if (!getEnclosingInstance().equals(other.getEnclosingInstance()))
				return false;
			if (color == null) {
				if (other.color != null)
					return false;
			} else if (!color.equals(other.color))
				return false;
			if (kf != other.kf)
				return false;
			return true;
		}

		private LambdaTest3 getEnclosingInstance() {
			return LambdaTest3.this;
		}

		@Override
		public String toString() {
			return "Mask [name=" + name + ", color=" + color + ", kf=" + kf + "]";
		}

	}

	/**
	 * 메소드 참조 Method Reference
	 * 기존 메소드를 람다식으로 사용가능
	 */
	private void test1() {
		//문자열을 정수로 변환하는 람다식
//		Function<String, Integer> toInt = s -> Integer.parseInt(s);
		Function<String, Integer> toInt = Integer::parseInt;
		System.out.println(toInt.apply("123"));
		System.out.println(toInt.apply("45"));
		System.out.println(toInt.apply("6"));

//		Consumer<Object> printer = o -> System.out.println(o);
		Consumer<Object> printer = System.out::println;
		printer.accept("안녕");

		//문자열의 길이를 리턴
		String name = "James";
		int len = name.length();
		Function<String, Integer> leng = String::length;
		System.out.println(leng.apply("안녕하세요"));

		//BiPredicate<P1, P2>:boolean
//		BiPredicate<String, String> strEquals = (s1, s2) -> s1.equals(s2);
		BiPredicate<String, String> strEquals = String::equals;
		System.out.println(strEquals.test("foo", "foo"));
		System.out.println(strEquals.test("foo", "koo"));

		String foo = "foo";
//		Predicate<String> equalsToFoo = s -> foo.equals(s);
		Predicate<String> equalsToFoo = foo::equals;
		System.out.println(equalsToFoo.test("foo"));
		System.out.println(equalsToFoo.test("koo"));

		//생성자 참조
		//name:String -> Product객체
//		Function<String, Product> productFactory = n -> new Product(n);
		Function<String, Product> productFactory = Product::new;

//		BiFunction<String, Integer, Product> prodConstructor =
//				(n, p) -> new Product(n, p);
		BiFunction<String, Integer, Product> prodConstructor = Product::new;

		System.out.println(productFactory.apply("아이폰"));
		System.out.println(prodConstructor.apply("볼펜", 1000));

	}

	class Product {
		private String name;
		private int price;

		public Product(String name) {
			this.name = name;
		}

		public Product(String name, int price) {
			this.name = name;
			this.price = price;
		}

		@Override
		public String toString() {
			return "Product [name=" + name + ", price=" + price + "]";
		}

	}

}
