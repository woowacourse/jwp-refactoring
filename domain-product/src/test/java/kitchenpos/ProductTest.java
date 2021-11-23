package kitchenpos;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Product;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Product 단위 테스트")
class ProductTest {
    // given
    private final String name = "강정치킨";
    private final BigDecimal price = BigDecimal.valueOf(17000);

    @Test
    @DisplayName("상품의 이름과 가격이 조건을 만족한다면 생성한다.")
    void create() {
        // when & then
        assertDoesNotThrow(() -> new Product(name, price));
    }

    @Test
    @DisplayName("상품의 가격이 null이면 상품을 생성할 수 없다.")
    void nullPrice() {
        // when & then
        assertThatThrownBy(() -> new Product(name, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }

    @Test
    @DisplayName("상품 가격이 음수이면 상품을 생성할 수 없다.")
    void minusPrice() {
        // when & then
        assertThatThrownBy(() -> new Product(name, BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품의 가격은 비어있을 수 없고 0 이상이어야 합니다.");
    }
}
