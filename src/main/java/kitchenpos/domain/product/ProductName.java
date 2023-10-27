package kitchenpos.domain.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class ProductName {
    private static final int MAX_LENGTH = 255;

    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    public ProductName(final String name) {
        validation(name);
        this.name = name;
    }

    private void validation(final String name) {
        if (Objects.isNull(name) || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public String getName() {
        return name;
    }
}
