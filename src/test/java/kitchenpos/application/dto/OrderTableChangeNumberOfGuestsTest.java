package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableChangeNumberOfGuestsTest extends DTOTest {
    @DisplayName("테이블 손님 수 변경 요청 유효성 테스트")
    @Test
    void validation() {
        OrderTableChangeNumberOfGuests request = new OrderTableChangeNumberOfGuests(1);
        OrderTableChangeNumberOfGuests badRequest = new OrderTableChangeNumberOfGuests(-1);

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}