package kitchenpos.application.dto;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.OrderLineItem;

class OrderCreateRequestTest extends DTOTest {
    @DisplayName("주문 생성 요청 유효성 검사")
    @Test
    void validation() {
        OrderCreateRequest request = new OrderCreateRequest(1L,
                singletonList(new OrderLineItem(null, null, 1L, 1L)));
        OrderCreateRequest badRequest = new OrderCreateRequest(null, emptyList());

        assertThat(validator.validate(request)).isEmpty();
        assertThat(validator.validate(badRequest).size()).isEqualTo(2);
    }
}