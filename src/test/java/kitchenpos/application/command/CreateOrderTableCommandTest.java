package kitchenpos.application.command;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateOrderTableCommandTest extends CommandTest {
    @DisplayName("테이블 생성 요청 유효성 검사")
    @Test
    void validation() {
        CreateOrderTableCommand request = new CreateOrderTableCommand(0, true);
        CreateOrderTableCommand badRequest = new CreateOrderTableCommand(-1, true);

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}