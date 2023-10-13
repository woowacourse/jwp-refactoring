package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.상품_저장;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu 메뉴_저장(
            final Function<Menu, Menu> persistable,
            final BigDecimal price,
            final List<Product> products
    ) {
        Menu menu = new Menu();
        menu.setName("할인 이벤트 치킨");
        menu.setPrice(price);
        menu.setMenuGroupId(1L);

        products.forEach(product -> menu.setMenuProducts(List.of(메뉴_상품_생성(product))));
        if (products.isEmpty()) {
            menu.setMenuProducts(Collections.emptyList());
        }

        return persistable.apply(menu);
    }

    public static MenuProduct 메뉴_상품_생성(Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);
        return menuProduct;
    }

    public static Menu 메뉴_저장(
            final Function<Menu, Menu> persistable,
            final Function<Product, Product> productPersistable
    ) {
        final Product product = 상품_저장(productPersistable);
        Menu menu = new Menu();
        menu.setName("할인 이벤트 치킨");
        menu.setPrice(new BigDecimal("10000"));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(List.of(메뉴_상품_생성(product)));
        return persistable.apply(menu);
    }

}
