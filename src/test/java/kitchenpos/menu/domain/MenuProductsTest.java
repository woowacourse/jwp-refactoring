package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProducts 단위 테스트")
class MenuProductsTest {

    @DisplayName("모든 상품 가격의 합보다 메뉴 가격이 더 클 경우 예외가 발생한다.")
    @Test
    void menuPriceException() {
        // given
        MenuGroup menuGroup = new MenuGroup("그룹");
        Menu menu = new Menu("좋은 메뉴", BigDecimal.valueOf(8_001), menuGroup);

        Product product1 = new Product("좋은 상품1", BigDecimal.valueOf(1_500));
        Product product2 = new Product("좋은 상품2", BigDecimal.valueOf(5_000));
        MenuProduct menuProduct1 = new MenuProduct(menu, product1, 2L);
        MenuProduct menuProduct2 = new MenuProduct(menu, product2, 1L);

        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));


        // when, then
        assertThatThrownBy(() -> menuProducts.validateMenuPrice(new MenuPrice(menu.getPrice())))
            .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }
}