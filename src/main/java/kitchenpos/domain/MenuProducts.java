package kitchenpos.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menuId", fetch = FetchType.EAGER)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<Long> extractProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public Money calculateProductsPriceSum(Products products) {
        Money sum = Money.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            Product product = products.findById(menuProduct.getProductId());
            Money totalPrice = product.calculateTotalPrice(menuProduct.getQuantity());
            sum = sum.plus(totalPrice);
        }

        return sum;
    }

    public void associateMenu(Menu menu) {
        for (final MenuProduct menuProduct : this.menuProducts) {
            menuProduct.setMenuId(menu.getId());
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}
