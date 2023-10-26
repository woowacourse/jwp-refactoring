package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuProductException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() {
    }

    public void add(List<MenuProduct> menuProducts) {
        if (isNull(menuProducts)) {
            throw new MenuProductException("메뉴 상품이 존재하지 않습니다.");
        }
        this.menuProducts.addAll(menuProducts);
    }

    private boolean isNull(List<MenuProduct> menuProducts) {
        return Objects.isNull(menuProducts);
    }

    public BigDecimal calculateTotalMenuProductsPrice() {
        BigDecimal totalMenuProductsPrice = BigDecimal.valueOf(0L);
        for (MenuProduct menuProduct : menuProducts) {
            totalMenuProductsPrice = totalMenuProductsPrice.add(menuProduct.calculateTotalPrice());
        }
        return totalMenuProductsPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
