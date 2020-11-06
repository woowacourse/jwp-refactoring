package kitchenpos.application;

import static kitchenpos.application.ServiceIntegrationTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 상태가 계산 완료인 경우 주문 상태를 변경할 수 없다.")
    @Test
    void changeOrderStatus_willThrowException_whenOrderStatusIsCompletion() {
        Order saved = getOrderWithStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(anyLong())).willReturn(Optional.of(saved));

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, getOrderWithStatus(OrderStatus.COOKING.name())))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
