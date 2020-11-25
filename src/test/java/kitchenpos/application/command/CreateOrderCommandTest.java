package kitchenpos.application.command;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateOrderCommandTest extends CommandTest {
    @DisplayName("주문 생성 요청 유효성 검사")
    @Test
    void validation() {
        CreateOrderCommand request = new CreateOrderCommand(1L,
                singletonList(new OrderLineItemRequest(1L, 1L)));
        CreateOrderCommand badRequest = new CreateOrderCommand(null, emptyList());

        assertThat(validator.validate(request)).isEmpty();
        assertThat(validator.validate(badRequest).size()).isEqualTo(2);
    }
}