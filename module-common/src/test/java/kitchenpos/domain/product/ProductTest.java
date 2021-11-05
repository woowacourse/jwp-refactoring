package kitchenpos.domain.product;


import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.fixture.CustomParameterizedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Product 도메인 단위테스트")
class ProductTest {

    @DisplayName("생성 - 성공 - price value가 null 또는 음수가 아닐 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 100, 1_000, 1_000_000_000})
    void create_Success_When_PriceIsValid(int priceValue) {
        // given
        // when
        // then
        assertThatCode(() -> new Product("양념치킨", priceValue))
            .doesNotThrowAnyException();
    }

    @DisplayName("생성 - 실패 - price value가 음수이거나 null일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {-1_000_000, -1_000, -1})
    @NullSource
    void create_Fail_When_PriceIsNegative(Integer priceValue) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Product("양념치킨", priceValue))
            .isInstanceOf(InvalidArgumentException.class);
    }
}
