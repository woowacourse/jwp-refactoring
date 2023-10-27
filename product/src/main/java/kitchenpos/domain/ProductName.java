package kitchenpos.domain;

import exception.InvalidNameException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {

    private static final int MAXIMUM_NAME_LENGTH = 255;

    @Column(name = "name", nullable = false)
    private String value;

    protected ProductName() {
    }

    public ProductName(final String value) {
        validate(value);
        this.value = value.strip();
    }

    private void validate(final String value) {
        validateNotBlank(value);
        validateLength(value.strip());
    }

    private static void validateNotBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException("상품 이름은 공백일 수 없습니다.");
        }
    }

    private static void validateLength(final String value) {
        if (value.length() > MAXIMUM_NAME_LENGTH) {
            throw new InvalidNameException("상품 이름은 255자를 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
