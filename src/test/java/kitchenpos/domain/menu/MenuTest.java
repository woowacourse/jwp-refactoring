package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.domain.product.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MenuTest {

    @DisplayName("메뉴가 주어진 가격보다 비싼지 비교한다.")
    @CsvSource({"1000,false", "1001,false", "999,true"})
    @ParameterizedTest(name = "1000원인 메뉴를 {0}원과 비교한 결과는 {1}이다.")
    void isExpensiveThan(final int priceValue, final boolean expected) {
        final Menu menu = Menu.ofUnsaved("공기밥", BigDecimal.valueOf(1000), 0L);
        final Price price = Price.from(priceValue);

        assertThat(menu.isExpensiveThan(price)).isEqualTo(expected);
    }
}
