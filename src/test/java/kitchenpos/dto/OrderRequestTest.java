package kitchenpos.dto;

import kitchenpos.dto.order.request.OrderRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class OrderRequestTest {


    @DisplayName("OrderRequset 주문 내역이 비어있을 때 예외 반환")
    @Test
    void create() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new OrderRequest(1L, new ArrayList<>()))
                .withMessage("주문 내역이 비어있습니다.");
    }
}