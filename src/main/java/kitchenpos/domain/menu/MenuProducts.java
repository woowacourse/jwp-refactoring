package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> collection;

    public MenuProducts(List<MenuProduct> collection) {
        this.collection = collection;
    }

    public BigDecimal calculateTotalPrice() {
        return collection.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<MenuProduct> getCollection() {
        return new ArrayList<>(collection);
    }
}
