package kitchenpos.domain;

import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Product Test")
class ProductTest {

    private static final BigDecimal INVALID_PRICE = BigDecimal.valueOf(-1);

    @DisplayName("Product 추가 테스트 - 실패 - 가격이 0보다 작은 경우")
    @Test
    void createFailureWhenInvalidPrice() {
        //given
        //when
        //then
        assertThatThrownBy(() -> ProductFixture.create(1L, "INVALID", INVALID_PRICE))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> ProductFixture.create(1L, "INVALID", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
