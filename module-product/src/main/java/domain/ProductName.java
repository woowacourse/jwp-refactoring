package domain;

import javax.persistence.Embeddable;

@Embeddable
public class ProductName {

    private final String name;

    public ProductName() {
        name = null;
    }

    public ProductName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
