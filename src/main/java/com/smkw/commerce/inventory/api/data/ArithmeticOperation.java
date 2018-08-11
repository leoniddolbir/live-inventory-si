package com.smkw.commerce.inventory.api.data;

public interface ArithmeticOperation<T extends Number, V> {
	V multiply(T arg);

	V divide(T arg1);

	V add(T arg1);

	V subtract(T arg1);
}
