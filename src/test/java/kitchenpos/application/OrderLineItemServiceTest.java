package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.OrderLineItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("OrderLineItem 서비스 테스트")
class OrderLineItemServiceTest extends ServiceTest {
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
        Order order = new Order(new OrderTable(10, false));
        given(menuRepository.findById(anyLong())).willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> orderLineItemService.create(orderLineItemRequests, order))
                .isInstanceOf(NonExistentException.class);
        then(menuRepository).should(times(1))
                .findById(anyLong());
        then(orderLineItemRepository).should(never())
                .save(any(OrderLineItem.class));
    }
}
