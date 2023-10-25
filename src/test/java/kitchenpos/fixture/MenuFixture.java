package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuCreateRequest.MenuProductRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuName;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu_group.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.vo.Price;

public class MenuFixture {

    public static Menu 메뉴(final String name, final Long price, final MenuProducts menuProducts,
                          final MenuGroup menuGroup) {
        final MenuName menuName = 메뉴_이름(name);
        final Price menuPrice = 메뉴_가격(price);
        return new Menu(menuName, menuPrice, menuProducts, menuGroup);
    }

    public static MenuName 메뉴_이름(final String name) {
        return new MenuName(name);
    }

    public static Price 메뉴_가격(final Long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static MenuProduct 메뉴_상품(final Product product, final Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProducts 메뉴_상품들(final MenuProduct... menuProduct) {
        return new MenuProducts(Arrays
                .stream(menuProduct)
                .collect(Collectors.toUnmodifiableList())
        );
    }

    public static MenuCreateRequest 메뉴_등록_요청(
            final String menuName,
            final BigDecimal price,
            final MenuGroup menuGroup,
            final MenuProducts menuProducts

    ) {
        return new MenuCreateRequest(
                menuName,
                price,
                menuGroup.getId(),
                menuProducts.menuProducts()
                        .stream()
                        .map(it -> new MenuProductRequest(it.getProduct().getId(), it.getQuantity()))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public static MenuCreateRequest 메뉴_등록_요청(
            final String menuName,
            final BigDecimal price,
            final Long menuGroupId,
            final MenuProducts menuProducts

    ) {
        return new MenuCreateRequest(
                menuName,
                price,
                menuGroupId,
                menuProducts.menuProducts()
                        .stream()
                        .map(it -> new MenuProductRequest(it.getProduct().getId(), it.getQuantity()))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public static MenuResponse 메뉴_등록_응답(final Menu menu) {
        return MenuResponse.from(menu);
    }
}
