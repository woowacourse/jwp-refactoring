package kitchenpos.application;

import kitchenpos.application.dto.request.OrderLineItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class OrderLineItemValidatorTest {

    @InjectMocks
    private OrderLineItemValidator orderLineItemValidator;

    @Test
    void 주문하려는_상품이_비어있으면_예외발생() {
        assertThatThrownBy(() -> orderLineItemValidator.validate(Collections.emptyList(), 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문하려는_상품_개수와_실제_조회된_메뉴_개수가_다르면_예외발생() {
        OrderLineItemRequest orderLineItemRequest1 = new OrderLineItemRequest(1L, 3);
        OrderLineItemRequest orderLineItemRequest2 = new OrderLineItemRequest(2L, 1);
        assertThatThrownBy(() -> orderLineItemValidator.validate(List.of(orderLineItemRequest1, orderLineItemRequest2), 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
