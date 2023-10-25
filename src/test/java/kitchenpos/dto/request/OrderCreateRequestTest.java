package kitchenpos.dto.request;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderCreateRequestTest {

    @DisplayName("orderLineItemRequests에서 중복된 Id가 있으면 예외 처리한다.")
    @Test
    void create_FailWhenValidateDuplicatedMenuId() {
        // given
        final Long duplicatedMenuId = 1L;

        // when & then
        assertThatThrownBy(() -> new OrderCreateRequest(
                1L,
                OrderStatus.COOKING.name(),
                of(new OrderLineItemCreateRequest(duplicatedMenuId, 1),
                        new OrderLineItemCreateRequest(duplicatedMenuId, 1)
                )
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 menuId 입니다. menuId: " + duplicatedMenuId);
    }

}
