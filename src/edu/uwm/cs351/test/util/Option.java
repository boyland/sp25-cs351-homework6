package edu.uwm.cs351.test.util;

public class Option<T> {

	private final T value;
	public Option(T val) {
		value = val;
	}
	
	public static <T> Option<T> none() {
		return null;
	}
	
	public static <T> Option<T> some(T val) {
		return new Option<T>(val);
	}

	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}
	
	public boolean equals(Object x) {
		if (!(x instanceof Option<?>)) return false;
		Option<?> other = (Option<?>)x;
		if (value == null) return other.value == null;
		return value.equals(other.value);
	}
	
	public static <T,U> boolean equals(Option<T> e1, Option<U> e2) {
		if (e1 == null || e2 == null) return e1 == e2;
		return e1.equals(e2);
	}
	
	public T getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "Option(" + value + ")";
	}
}
