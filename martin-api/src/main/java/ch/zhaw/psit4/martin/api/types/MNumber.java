package ch.zhaw.psit4.martin.api.types;

import java.util.Optional;

public class MNumber extends BaseType {
	private Optional<Integer> integerNumber = Optional.ofNullable(null);
	private Optional<Double> doubleNumber = Optional.ofNullable(null);
	private RawFormat rawFormat = RawFormat.UNKNOWN;

	public enum RawFormat {
		UNKNOWN, NUMERIC, WORD_EN;
	}

	public MNumber(String data) {
		super(data);
	}

	public RawFormat getRawFormat() {
		return rawFormat;
	}

	public void setRawFormat(RawFormat rawFormat) {
		this.rawFormat = rawFormat;
	}

	public Optional<Integer> getIntegerNumber() {
		return integerNumber;
	}

	public void setIntegerNumber(Optional<Integer> integerNumber) {
		this.integerNumber = integerNumber;
	}

	public Optional<Double> getDoubleNumber() {
		return doubleNumber;
	}

	public void setDoubleNumber(Optional<Double> doubleNumber) {
		this.doubleNumber = doubleNumber;
	}

}
