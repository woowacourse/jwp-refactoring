package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class PendingMenuProducts {

    private final List<PendingMenuProduct> products;

    public static PendingMenuProducts empty() {
        return new PendingMenuProducts(List.of());
    }

    public PendingMenuProducts(final List<PendingMenuProduct> products) {
        this.products = products;
    }

    public BigDecimal getTotalPrice() {
        return products.stream()
                .map(PendingMenuProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> createMenuProducts() {
        return products.stream()
                .map(PendingMenuProduct::createMenuProduct)
                .collect(Collectors.toList());
    }

    public List<PendingMenuProduct> getValue() {
        return products;
    }
}
