package kitchenpos.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import kitchenpos.exception.InvalidPriceException;

public class Price implements Serializable {
    private static final BigDecimal THRESHOLD = BigDecimal.ZERO;

    private BigDecimal value;

    protected Price() {
    }

    private Price(BigDecimal value) {
        if (value.compareTo(THRESHOLD) < 0) {
            throw new InvalidPriceException();
        }
        this.value = value;
    }

    public static Price of(BigDecimal value) {
        return new Price(value);
    }

    public static Price sumOf(List<Price> prices) {
        return prices.stream()
            .reduce(Price.of(BigDecimal.ZERO), Price::add);
    }

    public Price add(Price other) {
        return new Price(value.add(other.value));
    }

    public Price multiply(long quantity) {
        return new Price(value.multiply(BigDecimal.valueOf(quantity)));
    }

    public boolean isLargerThan(Price other) {
        return value.compareTo(other.value) > 0;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = BigDecimal.valueOf(Double.parseDouble(value));
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(value.toString());
        oos.defaultWriteObject();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        setValue(ois.readUTF());
        ois.defaultReadObject();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
