package kitchenpos.application;

import kitchenpos.domain.OrderItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderItemRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.OrderItemRequest;
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

@DisplayName("OrderItem 서비스 테스트")
class OrderItemServiceTest extends ServiceTest {
    @InjectMocks
    private OrderItemService orderItemService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @DisplayName("OrderItem을 생성한다. - 실패, 메뉴를 찾을 수 없음")
    @Test
    void createFailedWhenMenuNotFound() {
        // given
        List<OrderItemRequest> orderItemRequests = Arrays.asList(
                CREATE_ORDER_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_ITEM_REQUEST(2L, 1L)
        );
        Orders orders = new Orders(new OrderTable(10, false));
        given(menuRepository.findById(anyLong())).willThrow(NotFoundException.class);

        // when - then
        assertThatThrownBy(() -> orderItemService.create(orderItemRequests, orders))
                .isInstanceOf(NotFoundException.class);
        then(menuRepository).should(times(1))
                .findById(anyLong());
        then(orderItemRepository).should(never())
                .save(any(OrderItem.class));
    }
}
