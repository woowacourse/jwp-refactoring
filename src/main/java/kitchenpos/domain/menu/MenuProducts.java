package kitchenpos.domain.menu;

import kitchenpos.domain.product.Price;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.response.MenuResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MenuProducts {
    private final List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts of(Menu menu, List<Product> products, Map<Long, Long> productIdToQuantity) {
        List<MenuProduct> menuProducts = products.stream()
                .map(product -> new MenuProduct(menu, product, productIdToQuantity.get(product.getId())))
                .collect(Collectors.toList());
        validateTotalAmount(menu, menuProducts);
        return new MenuProducts(menuProducts);
    }

    private static void validateTotalAmount(Menu menu, List<MenuProduct> menuProducts) {
        Price result = Price.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            result = result.sum(menuProduct.calculateAmount());
        }
        if (menu.isSmallerPrice(result)) {
            throw new IllegalArgumentException("MenuProduct 전부를 합한 금액이 Menu 금액보다 작을 수 없습니다.");
        }
    }

    public List<MenuResponse> createMenuResponses() {
        Set<Menu> menus = extractDistinctMenu(menuProducts);
        return menus.stream()
                .map(menu -> MenuResponse.of(menu, selectiveByMenu(menu, menuProducts)))
                .collect(Collectors.toList());
    }

    private Set<Menu> extractDistinctMenu(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getMenu)
                .collect(Collectors.toSet());
    }

    private List<MenuProduct> selectiveByMenu(Menu menu, List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .filter(menuProduct -> menuProduct.equalsByMenuId(menu.getId()))
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
