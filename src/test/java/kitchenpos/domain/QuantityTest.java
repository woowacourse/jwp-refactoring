package kitchenpos.domain;

import static kitchenpos.exception.QuantityExceptionType.VALUE_NEGATIVE_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.QuantityException;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void 수량은_음수가_될_수_없다() {
        // when
        BaseExceptionType exceptionType = assertThrows(QuantityException.class, () ->
                new Quantity(-1)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(VALUE_NEGATIVE_EXCEPTION);
    }

    @Test
    void 값이_같으면_동등한_객체이다() {
        // given
        Quantity quantity = new Quantity(1);
        Quantity other = new Quantity(1);

        // expect
        assertThat(quantity).isEqualTo(other);
    }
}
