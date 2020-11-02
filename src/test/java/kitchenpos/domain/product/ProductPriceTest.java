package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ProductPriceTest {

    @DisplayName("생성자 테스트 - NPE 발생, Price가 Null인 경우")
    @Test
    void constructor_NullPrice_ThrownNullPointerException() {
        assertThatThrownBy(() -> new ProductPrice(null))
            .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("생성자 테스트 - IAE 발생, Price가 0보다 작은 경우")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void constructor_PriceLessThanZero_ThrownIllegalArgumentException(int price) {
        BigDecimal expectedPrice = BigDecimal.valueOf(price);

        assertThatThrownBy(() -> new ProductPrice(expectedPrice))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, Price가 0보다 크거나 같음")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void constructor_PriceMoreThanOrEqualZero_Success(int price) {
        BigDecimal expectedPrice = BigDecimal.valueOf(price);

        ProductPrice ProductPrice = new ProductPrice(expectedPrice);

        assertThat(ProductPrice.getPrice()).isEqualTo(expectedPrice);
    }
}
