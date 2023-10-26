package kitchenpos.Product.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {

    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    public ProductName(final String name) {
        validate(name);
        this.name = name;
    }

    public static ProductName from(final String value) {
        return new ProductName(value);
    }

    private static void validate(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("상품 이름은 필수 항목입니다.");
        }
    }

    public String getName() {
        return name;
    }
}
