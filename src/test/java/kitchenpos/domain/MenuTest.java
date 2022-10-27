package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 가격이 상품가격의 합보다 클 경우 예외가 발생한다")
    @Test
    void validatePriceIsCheaperThanSum() {
        BigDecimal price = new BigDecimal(30_000);
        Menu menu = new Menu("치킨 세트", price, 1L);

        BigDecimal sumOfProductPrice = new BigDecimal(25_000);

        assertThatThrownBy(() -> menu.validatePriceIsCheaperThanSum(sumOfProductPrice))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
