package kitchenpos.fixture;

import java.math.BigDecimal;
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
        return persistable.apply(menu);
    }

    public static MenuProduct 메뉴_상품_생성(Product product) {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1L);
        return menuProduct;
    }
}
