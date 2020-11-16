package kitchenpos.menu.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuProductTest {

    @DisplayName("멤버변수에서 갯수와 상품의 가격을 곱한 값을 반한환다.")
    @Test
    void calculateSum() {
        Product product = new Product("상품이름", 123L);
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        assertThat(menuProduct.calculateSum()).isEqualTo(BigDecimal.valueOf(246L));
    }

    @DisplayName("MenuProduct에 Menu를 set한다.")
    @Test
    void placeMenu() {
        //given
        Product product = new Product("상품이름", 123L);
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        Menu menu = new Menu("메뉴이름", 123L, new MenuGroup("메뉴그룹"));

        //when
        menuProduct.placeMenu(menu);

        //then
        assertThat(menuProduct.getMenu()).isNotNull();
    }

    @DisplayName("MenuProduct에 set할 Menu는 null이면 안된다.")
    @Test
    void placeMenuException() {
        //given
        Product product = new Product("상품이름", 123L);
        MenuProduct menuProduct = new MenuProduct(product, 2L);

        assertThatThrownBy(() -> menuProduct.placeMenu(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록할 메뉴가 없습니다.");
    }
}