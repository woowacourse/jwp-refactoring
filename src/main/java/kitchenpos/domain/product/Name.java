package kitchenpos.domain.product;

import kitchenpos.exception.ExceptionInformation;
import kitchenpos.exception.KitchenposException;

import java.util.Objects;

public class Name {
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 20;

    private final String value;

    public Name(final String value) {
        this.value = value;
    }

    public static Name from(final String value) {
        validateNotNull(value);
        validateBlank(value);
        validateLength(value);
        return new Name(value);
    }

    private static void validateNotNull(final String value) {
        if(Objects.isNull(value)){
            throw new KitchenposException(ExceptionInformation.PRODUCT_NAME_IS_NULL);
        }
    }

    private static void validateBlank(final String value) {
        if (value.isBlank()) {
            throw new KitchenposException(ExceptionInformation.PRODUCT_NAME_LENGTH_OUT_OF_BOUNCE);
        }
    }

    private static void validateLength(final String value) {
        if (value.length() < MIN_NAME_LENGTH || value.length() > MAX_NAME_LENGTH) {
            throw new KitchenposException(ExceptionInformation.PRODUCT_NAME_LENGTH_OUT_OF_BOUNCE);
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
