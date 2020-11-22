package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuPriceTest {

    @DisplayName("생성자 테스트 - IAE 발생, Price가 Null인 경우")
    @Test
    void constructor_NullPrice_ThrownIllegalArgumentException() {
        assertThatThrownBy(() -> new MenuPrice(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - IAE 발생, Price가 0보다 작은 경우")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -3})
    void constructor_PriceLessThanZero_ThrownIllegalArgumentException(int price) {
        BigDecimal expectedPrice = BigDecimal.valueOf(price);

        assertThatThrownBy(() -> new MenuPrice(expectedPrice))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성자 테스트 - 성공, Price가 0보다 크거나 같음")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void constructor_PriceMoreThanOrEqualZero_Success(int price) {
        BigDecimal expectedPrice = BigDecimal.valueOf(price);

        MenuPrice menuPrice = new MenuPrice(expectedPrice);

        assertThat(menuPrice.getPrice()).isEqualTo(expectedPrice);
    }

    @DisplayName("input 값보다 큰지 확인 - True, 큰 경우")
    @ParameterizedTest
    @ValueSource(ints = {99, 98, 97})
    void isMoreThan_MoreThan_ReturnTrue(int input) {
        MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(100));
        boolean moreThan = menuPrice.isMoreThan(BigDecimal.valueOf(input));

        assertThat(moreThan).isTrue();
    }

    @DisplayName("input 값보다 큰지 확인 - False, 작거나 같은 경우")
    @ParameterizedTest
    @ValueSource(ints = {100, 101, 102})
    void isMoreThan_LessThanOrEqual_ReturnFalse(int input) {
        MenuPrice menuPrice = new MenuPrice(BigDecimal.valueOf(100));
        boolean moreThan = menuPrice.isMoreThan(BigDecimal.valueOf(input));

        assertThat(moreThan).isFalse();
    }
}
