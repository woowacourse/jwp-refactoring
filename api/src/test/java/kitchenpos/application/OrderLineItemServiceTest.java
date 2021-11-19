package kitchenpos.application;

import kitchenpos.application.order.OrderLineItemService;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.exception.NonExistentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.ServiceTestFixture.DomainFactory.CREATE_ORDER;
import static kitchenpos.application.ServiceTestFixture.DomainFactory.CREATE_ORDER_TABLE;
import static kitchenpos.application.ServiceTestFixture.RequestFactory.CREATE_ORDER_ITEM_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ServiceTest
@DisplayName("OrderLineItem 서비스 테스트")
class OrderLineItemServiceTest {
    @InjectMocks
    private OrderLineItemService orderLineItemService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @DisplayName("OrderLineItem을 생성한다. - 실패, 메뉴를 찾을 수 없음")
    @Test
    void createFailedWhenMenuNotFound() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
                CREATE_ORDER_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_ITEM_REQUEST(2L, 1L)
        );
        Order order = CREATE_ORDER(
                null,
                CREATE_ORDER_TABLE(null, null, 10, false),
                OrderStatus.MEAL.name()
        );
        given(menuRepository.findById(anyLong())).willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> orderLineItemService.createOrderLineItem(orderLineItemRequests, order))
                .isInstanceOf(NonExistentException.class);
        then(menuRepository).should(times(1))
                .findById(anyLong());
        then(orderLineItemRepository).should(never())
                .save(any(OrderLineItem.class));
    }
}
