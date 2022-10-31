package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.CustomErrorCode;
import kitchenpos.exception.DomainLogicException;
import kitchenpos.domain.GuestNumber;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GuestNumberTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, Integer.MIN_VALUE})
    void 방문_손님_수가_0보다_작으면_예외를_던진다(final int number) {
        // when & then
        assertThatThrownBy(() -> new GuestNumber(number))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_GUEST_NUMBER_NEGATIVE_ERROR);
    }
}
