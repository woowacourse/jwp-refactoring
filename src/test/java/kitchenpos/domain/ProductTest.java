package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.config.CustomParameterizedTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Product 도메인 단위테스트")
class ProductTest {

    @DisplayName("생성 - 성공 - price가 null 또는 음수가 아닐 때")
    @CustomParameterizedTest
    @ValueSource(ints = {0, 1, 100, 1_000, 1_000_000_000})
    void create_Success_When_PriceIsValid(int price) {
        // given
        // when
        // then
        assertThatCode(() -> new Product("양념치킨", price))
            .doesNotThrowAnyException();
    }

    @DisplayName("생성 - 실패 - price가 음수일 때")
    @CustomParameterizedTest
    @ValueSource(ints = {-1_000_000, -1_000, -1})
    void create_Fail_When_PriceIsNegative(int price) {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Product("양념치킨", price))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성 - 실패 - price가 null일 때")
    @Test
    void create_Fail_When_PriceIsNull() {
        // given
        // when
        // then
        assertThatThrownBy(() -> new Product("양념치킨", null))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
