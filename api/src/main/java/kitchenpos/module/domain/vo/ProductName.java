package kitchenpos.module.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {

    private static final int NAME_LENGTH_MAXIMUM = 255;

    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    private ProductName(final String name) {
        validate(name);
        this.name = name;
    }

    public static ProductName from(final String name) {
        return new ProductName(name);
    }

    private static void validate(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 항목입니다.");
        }
        if (name.length() > NAME_LENGTH_MAXIMUM) {
            throw new IllegalArgumentException("상품 이름의 최대 길이는 " + NAME_LENGTH_MAXIMUM + "입니다.");
        }
    }

    public String getName() {
        return name;
    }
}
