package kitchenpos.domain;

import java.math.BigDecimal;

public class MenuGroup {
    private final Long id;
    private final String name;

    public MenuGroup(final String name) {
        this(null, name);
    }

    public MenuGroup(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Menu createMenu(final String name, final BigDecimal price, final PendingMenuProducts products) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0 || price.compareTo(products.getTotalPrice()) > 0) {
            throw new IllegalArgumentException();
        }
        return new Menu(name, price, id, products.createMenuProducts());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
