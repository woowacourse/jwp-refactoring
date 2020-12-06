package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductsTest {

    @DisplayName("MenuProduct의 상품*양 합은 만드려는 Menu의 가격보다 적어야한다.")
    @Test
    void createException() {
        Product product = new Product("상품", 100L);
        MenuProduct menuProduct1 = new MenuProduct(product, 1L);
        MenuProduct menuProduct2 = new MenuProduct(product, 1L);

        Long requestPrice = 201L;
        Menu menu = new Menu("상품이름", requestPrice, new MenuGroup("메뉴 그룹"));

        assertThatThrownBy(() -> menu.addMenuProducts(Arrays.asList(menuProduct1, menuProduct2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품 금액의 합(%d)이 메뉴의 가격(%d)보다 작습니다.", 200L, requestPrice);
    }

}