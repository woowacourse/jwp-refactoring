package kitchenpos.domain;

import java.util.List;
import kitchenpos.exception.KitchenException;

public class MenuProducts {

    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        this.menuProducts = updateMenu(menuProducts, menu);
        validate(menu);
    }

    private void validate(Menu menu) {
        Price sum = calculateSum();
        if (sum.isLessThan(menu.getPrice())) {
            throw new KitchenException("메뉴 가격은 메뉴에 속한 상품들의 가격 총액보다 낮아야 합니다.");
        }
    }

    private Price calculateSum() {
        return menuProducts.stream()
            .map(MenuProduct::calculateTotalPrice)
            .reduce(Price.of(0L), Price::add);
    }

    private List<MenuProduct> updateMenu(List<MenuProduct> menuProducts, Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
        return menuProducts;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
