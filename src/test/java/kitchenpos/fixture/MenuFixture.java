package kitchenpos.fixture;

import static kitchenpos.fixture.ProductFixture.상품_저장;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

public class MenuFixture {

    public static Menu 메뉴_저장(
            final Function<Menu, Menu> persistable,
            final Long menuGroupId,
            final BigDecimal price,
            final Product product
    ) {
        Menu menu = new Menu("할인치킨", price, menuGroupId, List.of(메뉴_상품_생성(product)));
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

        return 메뉴_저장(persistable::apply, 1L, BigDecimal.valueOf(5000), product);
    }

}
