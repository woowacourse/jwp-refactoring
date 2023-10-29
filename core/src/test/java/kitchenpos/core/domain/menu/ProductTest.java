package kitchenpos.core.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.core.domain.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    @DisplayName("상품에 이름 정보는 필수 항목이다.")
    void 상품_생성_실패_이름_없음() {
        // given
        String nullName = null;

        // expected
        assertThatThrownBy(() -> new Product(nullName, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 256})
    @DisplayName("상품 이름의 길이는 최소 1자, 최대 255자이다.")
    void 상품_생성_실패_이름_길이(final int nameLength) {
        // given
        final String name = "a".repeat(nameLength);

        // expected
        assertThatThrownBy(() -> new Product(name, BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품에 가격 정보는 필수 항목이다.")
    void 상품_생성_실패_가격_없음() {
        // given
        final BigDecimal nullPrice = null;

        // expected
        assertThatThrownBy(() -> new Product("새우탕면", nullPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 최소 가격은 0원이다.")
    void 상품_생성_실패_최소_가격() {
        assertThatThrownBy(() -> new Product("새우탕면", BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격은 19자리 수까지 가능하다.")
    void 상품_생성_실패_최대_가격() {

        assertThatThrownBy(() -> new Product("새우탕면", new BigDecimal("9".repeat(20))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격의 소수점은 둘째 자리수 아래로는 버린다.")
    void 상품_생성_성공_가격_소수점_버림() {
        // given
        final BigDecimal overScaledPrice = new BigDecimal("5000.123");

        // when
        final Product scaled = new Product("새우탕면", overScaledPrice);

        // then
        assertThat(scaled.getPrice().value()).hasScaleOf(2);
    }
}
