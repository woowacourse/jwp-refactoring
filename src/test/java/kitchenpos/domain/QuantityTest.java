package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuantityTest {

    @ParameterizedTest
    @ValueSource(longs = {Long.MIN_VALUE, -1})
    void 수량이_0개_보다_작으면_예외를_던진다(final long quantity) {
        // when & then
        assertThatThrownBy(() -> new Quantity(quantity))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.QUANTITY_NEGATIVE_ERROR);
    }
}
