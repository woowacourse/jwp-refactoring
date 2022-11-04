package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품에 메뉴를 지정한다.")
    void arrangeMenu() {
        MenuGroup menuGroup = MenuGroup.builder()
                .name("메뉴 그룹")
                .build();
        Product product = Product.builder()
                .name("상품")
                .price(BigDecimal.valueOf(5000))
                .build();
        MenuProduct menuProduct = MenuProduct.builder()
                .product(product)
                .quantity(1L)
                .build();
        Menu menu = Menu.builder()
                .name("메뉴")
                .price(BigDecimal.valueOf(5000))
                .menuGroup(menuGroup)
                .menuProducts(List.of(menuProduct))
                .build();

        assertThat(menuProduct.getMenu()).isEqualTo(menu);
    }
}
