package kitchenpos.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.core.product.domain.Price;
import kitchenpos.core.menu.domain.MenuProduct;
import kitchenpos.core.menu.domain.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    @Test
    @DisplayName("메뉴에 있는 각 상품의 금액 총합보다 주어진 금액이 크다면 true를 반환한다.")
    void invalidMenuPrice() {
        // given
        final MenuProducts menuProducts = new MenuProducts(List.of(
                new MenuProduct(1L, new Price(new BigDecimal("4000")), 1L, 1L),
                new MenuProduct(2L, new Price(new BigDecimal("4000")), 2L, 1L)
        ));

        // when & then
        assertThat(menuProducts.isBiggerThanTotalPrice(new Price(new BigDecimal("13000")))).isTrue();
    }
}
