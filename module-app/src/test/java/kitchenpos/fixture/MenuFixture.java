package kitchenpos.fixture;

import application.dto.MenuCreateRequest;
import application.dto.MenuCreateRequest.MenuProductRequest;
import application.dto.MenuResponse;
import domain.Menu;
import domain.MenuGroup;
import domain.MenuName;
import domain.MenuValidator;
import domain.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;
import domain.menu_product.MenuProduct;
import domain.menu_product.MenuProductValidator;
import domain.menu_product.MenuProducts;
import support.AggregateReference;
import vo.Price;

public class MenuFixture {

    public static Menu 메뉴(
            final String name, final Long price,
            final MenuProducts menuProducts,
            final MenuGroup menuGroup,
            final MenuValidator menuValidator
    ) {
        final MenuName menuName = 메뉴_이름(name);
        final Price menuPrice = 메뉴_가격(price);
        final AggregateReference<MenuGroup> menuGroupId = new AggregateReference<>(menuGroup.getId());
        return new Menu(menuName, menuPrice, menuProducts, menuGroupId, menuValidator);
    }

    public static MenuName 메뉴_이름(final String name) {
        return new MenuName(name);
    }

    public static Price 메뉴_가격(final Long price) {
        return new Price(BigDecimal.valueOf(price));
    }

    public static MenuProduct 메뉴_상품(
            final Long productId,
            final Long quantity,
            final MenuProductValidator menuProductValidator
    ) {
        final AggregateReference<Product> product = new AggregateReference<>(productId);
        return new MenuProduct(product, quantity, menuProductValidator);
    }

    public static MenuProducts 메뉴_상품들(final MenuProduct... menuProduct) {
        return new MenuProducts(Arrays.stream(menuProduct)
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
                        .map(it -> new MenuProductRequest(it.getProductId().getId(), it.getQuantity()))
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
                        .map(it -> new MenuProductRequest(it.getProductId().getId(), it.getQuantity()))
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    public static MenuProductValidator 메뉴_상품_검증() {
        return new MenuProductValidator(null) {
            @Override
            public void validate(final AggregateReference<Product> productId) {
            }
        };
    }

    public static MenuResponse 메뉴_등록_응답(final Menu menu) {
        return MenuResponse.from(menu);
    }
}
