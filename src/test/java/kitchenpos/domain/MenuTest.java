package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.Menu.Builder;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴의 가격이 메뉴에 속한 상품 가격의 합보다 클 경우 예외가 발생한다.")
    void validatePrice() {
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
        Builder menuBuilder = Menu.builder()
                .name("메뉴")
                .price(BigDecimal.valueOf(5001))
                .menuGroup(menuGroup)
                .menuProducts(List.of(menuProduct));

        assertThatThrownBy(menuBuilder::build)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴의 가격은 메뉴에 속한 상품의 합보다 클 수 없습니다.");
    }
}
