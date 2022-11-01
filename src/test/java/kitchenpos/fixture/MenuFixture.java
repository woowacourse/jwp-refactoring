package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;

@SuppressWarnings("NonAsciiCharacters")
public enum MenuFixture {

    후라이드_양념치킨_두마리세트("후라이드, 양념치킨 두마리 세트", new BigDecimal(30_000));

    private final String name;
    private final BigDecimal price;

    MenuFixture(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public MenuRequest toRequest(Long menuGroupId, Long... productIds) {
        List<MenuProductRequest> menuProductRequests = Arrays.stream(productIds)
                .map(productId -> new MenuProductRequest(productId, 1L))
                .collect(Collectors.toList());

        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }

    public Menu toMenu(MenuGroup menuGroup, Product... products) {
        Menu menu = new Menu(name, price, menuGroup);
        addMenuProducts(menu, products);
        return menu;
    }

    private void addMenuProducts(Menu menu, Product... products) {
        List<MenuProduct> menuProducts = Arrays.stream(products)
                .map(p -> new MenuProduct(menu, p, 1))
                .collect(Collectors.toList());

        menu.addMenuProducts(menuProducts);
    }
}
