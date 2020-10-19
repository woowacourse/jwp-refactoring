package kitchenpos.application;

import kitchenpos.config.IsolatedTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends IsolatedTest {

    @Autowired
    private OrderService service;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 생성 실패 - 주문 아이템이 비었을 때")
    @Test
    public void createFailItemEmpty() {
        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(), Lists.newArrayList());

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 아이템이 실제 등록된 메뉴와 다를 때")
    @Test
    public void createFailItemMenuCount() {
        orderTableRepository.save(new OrderTable(1L, null, 3, false));
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(
                        new OrderLineItemRequest(1L, null, 1L, 1),
                        new OrderLineItemRequest(2L, null, 2L, 1)
                )
        );

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 테이블이 존재하지 않을 때")
    @Test
    public void createFailNotExistedOrderTable() {
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(new OrderLineItemRequest(1L, null, 1L, 1)));

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 실패 - 주문 테이블이 이미 사용 중일 때")
    @Test
    public void createFailOrderTableEmpty() {
        orderTableRepository.save(new OrderTable(1L, null, 3, true));
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(new OrderLineItemRequest(1L, null, 1L, 1)));

        assertThatThrownBy(() -> service.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성")
    @Test
    public void createOrder() {
        orderTableRepository.save(new OrderTable(1L, null, 3, false));
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(new OrderLineItemRequest(1L, null, 1L, 1)));

        final OrderResponse response = service.create(request);

        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(response.getOrderTableId()).isEqualTo(1L);
        assertThat(response.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 목록 조회")
    @Test
    public void readOrders() {
        orderTableRepository.save(new OrderTable(1L, null, 3, false));
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(new OrderLineItemRequest(1L, null, 1L, 1)));

        service.create(request);
        final OrderResponses orders = service.list();

        assertThat(orders.getOrders()).hasSize(1);
        assertThat(orders.getOrders().get(0).getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    @DisplayName("주문 상태 변경 - 주문이 없을 때")
    @Test
    public void changeFailNotExistedOrder() {
        OrderRequest changeRequest = new OrderRequest(1L, OrderStatus.MEAL, LocalDateTime.now(), Lists.newArrayList());

        assertThatThrownBy(() -> service.changeOrderStatus(1L,changeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경")
    @Test
    public void changeOrderStatus() {
        orderTableRepository.save(new OrderTable(1L, null, 3, false));
        menuRepository.save(new Menu(1L, "파스타", BigDecimal.valueOf(8_000L), null, null));

        OrderRequest request = new OrderRequest(1L, OrderStatus.COOKING, LocalDateTime.now(),
                Lists.newArrayList(new OrderLineItemRequest(1L, null, 1L, 1)));
        OrderResponse response = service.create(request);

        OrderRequest changeRequest = new OrderRequest(1L, OrderStatus.MEAL, LocalDateTime.now(), Lists.newArrayList());
        OrderResponse changedResponse = service.changeOrderStatus(response.getId(), changeRequest);

        assertThat(changedResponse.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
