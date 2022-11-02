package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;

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
        List<MenuProduct> menuProducts = createMenuProducts(products);
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private List<MenuProduct> createMenuProducts(Product... products) {
        return Arrays.stream(products)
                .map(p -> new MenuProduct(p, 1))
                .collect(Collectors.toList());
    }
}
