package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTest {

    @DisplayName("상품을 생성한다.")
    @Test
    void construct() {
        Product product = new Product("치킨", BigDecimal.valueOf(10000));

        assertThat(product).isNotNull();
    }

    @DisplayName("상품을 생성 시, 가격이 0보다 작으면 예외가 발생한다.")
    @ValueSource(ints = {-1, -100})
    @ParameterizedTest
    void constructWithInvalidPrice(int price) {
        assertThatThrownBy(() -> new Product("치킨", BigDecimal.valueOf(price)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 생성 시, 상품의 가격이 null이면 예외가 발생한다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> new Product("치킨", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
