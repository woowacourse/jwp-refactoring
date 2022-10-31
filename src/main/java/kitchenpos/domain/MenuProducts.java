package kitchenpos.domain;

import java.util.*;
import java.util.stream.Collectors;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public boolean isOverThanTotalPrice(Map<Long, Long> groupedPriceByProduct, long menuPrice) {
        return calculateTotalPrice(groupedPriceByProduct) < menuPrice;
    }

    private long calculateTotalPrice(Map<Long, Long> groupedPriceByProduct) {
        return this.menuProducts.stream()
                .mapToLong(each -> groupedPriceByProduct.get(each.getProductId()) * each.getQuantity())
                .sum();
    }

    public MenuProducts changeMenuId(Long menuId) {
        List<MenuProduct> changedMenuProducts = this.menuProducts.stream()
                .map(each -> each.changeMenuId(menuId))
                .collect(Collectors.toUnmodifiableList());
        return new MenuProducts(changedMenuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public List<Long> getProductsIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toUnmodifiableList());
    }
}
