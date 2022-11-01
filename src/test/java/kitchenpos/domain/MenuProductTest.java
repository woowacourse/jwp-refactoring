package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import kitchenpos.fixtures.TestFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuProductTest {

    @ParameterizedTest
    @ValueSource(longs = {100L, 500L, 1_000L, 5_000L})
    void 상품_가격과_수량을_곱한_값을_반환한다(final long productPrice) {
        // given
        final Product product = TestFixtures.상품_생성("피자", BigDecimal.valueOf(productPrice));
        final MenuProduct menuProduct = TestFixtures.메뉴_상품_생성(null, product, 5);

        // when
        final BigDecimal price = menuProduct.getPrice();

        // then
        assertThat(price).isEqualTo(BigDecimal.valueOf(productPrice * 5));
    }
}
