package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class ProductTest {

    @Test
    void 이름_정보가_누락된_상품을_생성하려는_경우_예외발생() {
        assertThatThrownBy(() -> new Product(1L, "", BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("calculateTotalPriceFromQuantity 메서드는 상품을 수량만큼 구매했을 때의 가격을 반환한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    void calculateTotalPriceFromQuantity(int quantity) {
        double price = 10.50;
        Product product = new Product(1L, "상품명", BigDecimal.valueOf(price));

        BigDecimal actual = product.calculateTotalPriceFromQuantity(quantity);
        BigDecimal expected = BigDecimal.valueOf(price * quantity);

        assertThat(actual).isEqualTo(expected);
    }
}
