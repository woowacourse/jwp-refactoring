package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    private String name;

    public ProductName() {
    }

    public ProductName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
