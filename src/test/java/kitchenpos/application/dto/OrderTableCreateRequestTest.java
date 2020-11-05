package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderTableCreateRequestTest extends DTOTest {
    @Test
    void validation() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);
        OrderTableCreateRequest badRequest = new OrderTableCreateRequest(-1, true);

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}