package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"-1"})
    void 상품의_가격이_null이거나_0보다_작은_경우_상품을_생성할_수_없다(BigDecimal price) {
        assertThatThrownBy(() -> new Product("치킨", price))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품의_가격이_0보다_큰_경우_상품을_생성할_수_있다() {
        // given
        assertThatCode(() -> new Product("치킨", BigDecimal.valueOf(1)))
                .doesNotThrowAnyException();
    }
}
