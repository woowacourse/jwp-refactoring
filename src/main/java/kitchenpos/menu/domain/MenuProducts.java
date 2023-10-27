package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false, updatable = false)
    private List<MenuProduct> items = new ArrayList<>();

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> items) {
        this.items = items;
    }

    public static MenuProducts of(List<MenuProduct> menuProducts, Price price) {
        validate(menuProducts, price);
        return new MenuProducts(menuProducts);
    }

    private static void validate(final List<MenuProduct> menuProducts, Price price) {
        if (price.compareTo(sum(menuProducts)) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품들의 가격보다 저렴해야 합니다.");
        }
    }

    private static Price sum(final List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::calculateAmount)
                .reduce(Price.from(BigDecimal.ZERO), Price::add);
    }

    public List<MenuProduct> getItems() {
        return Collections.unmodifiableList(items);
    }
}
