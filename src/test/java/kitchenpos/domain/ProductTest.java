package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "0")
    void 상품의_가격이_null이거나_0보다_작은_경우_잘못된_가격임을_반환한다(BigDecimal price) {
        // given
        Product 치킨 = new Product(null, "치킨", price);

        // when
        boolean actual = 치킨.hasInvalidPrice();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 상품의_가격이_0보다_큰_경우_올바른_가격임을_반환한다() {
        // given
        Product 치킨 = new Product(null, "치킨", BigDecimal.valueOf(1L));

        // when
        boolean actual = 치킨.hasInvalidPrice();

        // then
        assertThat(actual).isFalse();
    }
}
