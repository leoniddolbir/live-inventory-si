package com.smkw.commerce.inventory.api.data;

import java.math.BigDecimal;
import java.util.Currency;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Data;

import org.springframework.util.Assert;

/**
 * Value object to encapsulate a monetary amount consisting of a currency and a
 * value. Provides basic mathematical abstractions to allow adding up
 * {@link MonetaryAmount}s.
 */
@Data
@Embeddable
public class MonetaryAmount implements ArithmeticOperation<Integer, MonetaryAmount> {

	public static Currency USD = Currency.getInstance("USD");
	public static MonetaryAmount ZERO = new MonetaryAmount();

	@Transient
	private final Currency currency;
	private final BigDecimal amount;

	/**
	 * Creates a new {@link MonetaryAmount} instance with the given value and
	 * {@link Currency}.
	 * 
	 * @param currency
	 *            must not be {@literal null}.
	 * @param value
	 */
	public MonetaryAmount(Currency currency, double value) {
		this(currency, new BigDecimal(value));
	}

	/**
	 * Creates a new {@link MonetaryAmount} of the given value and
	 * {@link Currency}.
	 * 
	 * @param currency
	 *            must not be {@literal null}.
	 * @param value
	 *            must not be {@literal null}.
	 */
	public MonetaryAmount(Currency currency, BigDecimal value) {

		Assert.notNull(currency);
		Assert.notNull(value);

		this.currency = currency;
		this.amount = value.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Creates a new {@link MonetaryAmount} of the given value and defaults
	 * currency to USD.
	 * 
	 * @param currency
	 *            must not be {@literal null}.
	 * @param value
	 *            must not be {@literal null}.
	 */
	public MonetaryAmount(BigDecimal value) {

		Assert.notNull(value);

		this.currency = USD;
		this.amount = value.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * No-arg constructor to satisfy persistence mechanism.
	 */
	protected MonetaryAmount() {
		this(USD, BigDecimal.ZERO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return currency + amount.toString();
	}

	/**
	 * Adds the given {@link MonetaryAmount} to the current one. Treats
	 * {@literal null} values like {@link MonetaryAmount#ZERO}.
	 * 
	 * @param other
	 *            the {@link MonetaryAmount} to add.
	 */
	public MonetaryAmount add(MonetaryAmount other) {

		if (other == null) {
			return this;
		}

		Assert.isTrue(this.currency.equals(other.currency));
		return new MonetaryAmount(this.currency, this.amount.add(other.amount));
	}

	@Override
	public MonetaryAmount multiply(Integer arg) {
		return new MonetaryAmount(this.currency, this.amount.multiply(BigDecimal.valueOf(this.toLong(arg), 0)));
	}

	private long toLong(Integer arg) {
		return (arg == null) ? 0l : arg.longValue();
	}

	@Override
	public MonetaryAmount divide(Integer arg) {
		return new MonetaryAmount(this.currency, this.amount.divide(BigDecimal.valueOf(this.toLong(arg), 0)));
	}

	@Override
	public MonetaryAmount add(Integer arg) {
		return new MonetaryAmount(this.currency, this.amount.add(BigDecimal.valueOf(this.toLong(arg), 0)));
	}

	@Override
	public MonetaryAmount subtract(Integer arg) {
		return new MonetaryAmount(this.currency, this.amount.subtract(BigDecimal.valueOf(this.toLong(arg), 0)));
	}
}
