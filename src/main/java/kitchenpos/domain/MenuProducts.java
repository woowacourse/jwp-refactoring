package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId")
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {

    }

    public MenuProducts(MenuProduct... menuProducts) {
        this(Arrays.asList(menuProducts));
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateTotalPrice(Products products) {
        return menuProducts.stream()
            .map(products::totalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void changeMenuInfo(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.changeMenuInfo(menu);
        }
    }

    public List<MenuProduct> toList() {
        return Collections.unmodifiableList(menuProducts);
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
            .map(MenuProduct::getProductId)
            .collect(Collectors.toList());
    }
}
