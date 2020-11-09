package kitchenpos.application.command;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateMenuGroupCommandTest extends CommandTest {
    @DisplayName("메뉴 그룹 유효성 검사")
    @Test
    void validation() {
        CreateMenuGroupCommand request = new CreateMenuGroupCommand("추천메뉴");
        CreateMenuGroupCommand badRequest = new CreateMenuGroupCommand("");

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}