package kitchenpos.domain.product;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ProductTest {

    @DisplayName("수량에 따른 상품 가격을 계산한다.")
    @CsvSource({"13000,2,26000", "15000,3,45000"})
    @ParameterizedTest(name = "{0}원인 상품 {1}개로 구성된 메뉴 상품의 총 정가는 {2}이다.")
    void calculatePrice(final long price, final long quantity, final int expected) {
        final Product product = Product.ofUnsaved("피자", BigDecimal.valueOf(price));
        final Price actual = product.calculatePrice(quantity);

        assertThat(actual).isEqualTo(Price.from(expected));
    }
}
