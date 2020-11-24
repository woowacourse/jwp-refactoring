package kitchenpos.domain;

import kitchenpos.domain.product.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class PriceTest {

    @DisplayName("isSmaller 기능 테스트")
    @ParameterizedTest
    @CsvSource(value = {"9:true", "10:false", "11:false"}, delimiter = ':')
    void isSmaller(int price, boolean expect) {
        Price priceOne = new Price(BigDecimal.TEN);

        boolean actual = priceOne.isSmaller(new Price(price));

        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("multiply 기능 테스트")
    @ParameterizedTest
    @CsvSource(value = {"3:3", "5:5", "7:7"}, delimiter = ':')
    void multiply(Long quantity, int price) {
        Price priceOne = new Price(BigDecimal.ONE);

        Price actual = priceOne.multiply(quantity);

        assertThat(actual).isEqualToComparingFieldByField(new Price(price));
    }

    @DisplayName("price가 0보다 작은 경우 예외 반환")
    @Test
    void validateGreaterOrSameThanZero() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(-1))
                .withMessage("price가 null이거나 0보다 작을 수 없습니다.");
    }
}