package kitchenpos.application.command;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangeNumberOfOrderTableGuestsCommandTest extends CommandTest {
    @DisplayName("테이블 손님 수 변경 요청 유효성 테스트")
    @Test
    void validation() {
        ChangeNumberOfOrderTableGuestsCommand request = new ChangeNumberOfOrderTableGuestsCommand(
                1);
        ChangeNumberOfOrderTableGuestsCommand badRequest = new ChangeNumberOfOrderTableGuestsCommand(
                -1);

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}