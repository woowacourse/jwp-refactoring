package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProducts 단위 테스트")
class MenuProductsTest {

    @DisplayName("모든 상품 가격의 합보다 메뉴 가격이 더 클 경우 예외가 발생한다.")
    @Test
    void menuPriceException() {
        // given
        MenuGroup menuGroup = new MenuGroup("좋은 그룹");
        Product product1 = new Product("좋은 상품1", BigDecimal.valueOf(3_000));
        Product product2 = new Product("좋은 상품2", BigDecimal.valueOf(5_000));
        MenuProduct menuProduct1 = new MenuProduct(product1, 1);
        MenuProduct menuProduct2 = new MenuProduct(product2, 1);

        MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        Menu goodMenu = new Menu("좋은 메뉴", BigDecimal.valueOf(8_000), menuGroup);
        Menu badMenu = new Menu("나쁜 메뉴", BigDecimal.valueOf(8_001), menuGroup);

        // when, then
        assertThatCode(() -> menuProducts.validateMenuPrice(new Price(goodMenu.getPrice())))
            .doesNotThrowAnyException();
        assertThatThrownBy(() -> menuProducts.validateMenuPrice(new Price(badMenu.getPrice())))
            .isExactlyInstanceOf(InvalidMenuPriceException.class);
    }
}