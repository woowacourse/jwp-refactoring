package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.DomainLogicException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    "})
    void 이름이_빈값인_경우_예외를_던진다(final String value) {
        // when & then
        assertThatThrownBy(() -> new Name(value))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.NAME_BLANK_ERROR);
    }

}
