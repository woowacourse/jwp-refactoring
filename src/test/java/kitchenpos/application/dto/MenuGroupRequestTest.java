package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupRequestTest extends DTOTest {
    @DisplayName("메뉴 그룹 유효성 검사")
    @Test
    void validation() {
        MenuGroupRequest request = new MenuGroupRequest("추천메뉴");
        MenuGroupRequest badRequest = new MenuGroupRequest("");

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}