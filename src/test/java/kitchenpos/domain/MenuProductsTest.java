package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MenuProducts 도메인 테스트")
class MenuProductsTest {

    @DisplayName("가격은 주문 금액의 총 합보다 작거나 같아야 한다")
    @Test
    void validatePriceIsLowerThanTotalPrice() {
        final MenuProduct menuProduct = new MenuProduct(1L, 1, BigDecimal.valueOf(15_000));
        final Price price = new Price(new BigDecimal(30_000));

        final MenuProducts menuProducts = new MenuProducts(List.of(menuProduct));

        assertThatThrownBy(() -> menuProducts.validatePriceIsLowerThanTotalPrice(price))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("메뉴의 가격은 상품의 총 합보다 같거나 작아야 합니다.");
    }
}
