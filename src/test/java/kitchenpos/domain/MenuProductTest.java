package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {
    @ParameterizedTest
    @DisplayName("가격이 주어지면 (가격 * 수량)의 값을 반환한다")
    @CsvSource(value = {"10,1000,10000", "5,21,105"})
    void multiplyQuantityWithPrice(int quantity, BigDecimal price, BigDecimal expected) {
        MenuProduct menuProduct = createMenuProduct(1L, 1L, quantity, 1L);

        assertThat(menuProduct.calculateTotal(price)).isEqualTo(expected);
    }
}