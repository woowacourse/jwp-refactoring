package kitchenpos.domain;

import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Price;
import kitchenpos.domain.menu.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class MenuProductTest {

    @Test
    @DisplayName("메뉴 상품의 가격을 계산한다.")
    void calculateMenuProductPrice() {
        // given
        final Product 후라이드 = new Product("후라이드", BigDecimal.valueOf(17000));
        final MenuProduct 후라이드_2개 = new MenuProduct(후라이드, 2L);

        // when
        final Price actual = 후라이드_2개.calculateMenuProductPrice();

        // then
        assertThat(actual).isEqualTo(new Price(new BigDecimal(34000)));
    }
}
