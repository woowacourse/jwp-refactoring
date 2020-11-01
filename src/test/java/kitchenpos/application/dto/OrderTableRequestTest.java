package kitchenpos.application.dto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderTableRequestTest extends DTOTest {
    @Test
    void validation() {
        OrderTableRequest request = new OrderTableRequest(0, true);
        OrderTableRequest badRequest = new OrderTableRequest(-1, true);

        assertAll(
                () -> assertThat(validator.validate(request).isEmpty()).isTrue(),
                () -> assertThat(validator.validate(badRequest).size()).isEqualTo(1)
        );
    }
}