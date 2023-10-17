package kitchenpos.domain;

import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.Test;

class PriceTest {

    @Test
    void 가격이_null_이면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Price(null)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NULL);
    }

    @Test
    void 가격이_0보다_작으면_예외가_발생한다() {
        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                new Price(BigDecimal.valueOf(-1))
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NEGATIVE);
    }
}
