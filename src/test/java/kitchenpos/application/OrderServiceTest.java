package kitchenpos.application;

import static kitchenpos.OrderFixtures.createOrder;
import static kitchenpos.OrderFixtures.createOrderChangeRequest;
import static kitchenpos.OrderFixtures.createOrderLineItemRequest;
import static kitchenpos.OrderFixtures.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.OrderTableFixtures;
import kitchenpos.TableGroupFixtures;
import kitchenpos.application.dto.OrderRequest;
import kitchenpos.application.dto.OrderResponse;
import kitchenpos.application.dto.OrderStatusChangeRequest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    private OrderService orderService;
    private TableGroupRepository tableGroupRepository;
    private OrderTableRepository orderTableRepository;
    private OrderDao orderDao;

    @Autowired
    public OrderServiceTest(
            OrderService orderService,
            TableGroupRepository tableGroupRepository,
            OrderTableRepository orderTableRepository,
            OrderDao orderDao
    ) {
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderDao = orderDao;
    }

    private TableGroup tableGroup;
    private OrderTable emptyOrderTable;
    private OrderTable filledOrderTable;

    @BeforeEach
    void setUp() {
        this.tableGroup = tableGroupRepository.save(TableGroupFixtures.createTableGroup());
        this.emptyOrderTable = orderTableRepository.save(OrderTableFixtures.createOrderTable(tableGroup, 0, true));
        this.filledOrderTable = orderTableRepository.save(OrderTableFixtures.createOrderTable(tableGroup, 2, false));
    }

    @Test
    void create() {
        // given
        OrderRequest request = createOrderRequest(filledOrderTable.getId());

        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void createWithEmptyOrderLineItems() {
        // given
        OrderRequest request = createOrderRequest(filledOrderTable.getId(), List.of());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithInvalidMenu() {
        // given
        long invalidMenuId = 999L;
        OrderRequest request = createOrderRequest(
                filledOrderTable.getId(),
                List.of(createOrderLineItemRequest(invalidMenuId))
        );

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithInvalidOrderTable() {
        // given
        long invalidOrderTableId = 999L;
        OrderRequest request = createOrderRequest(invalidOrderTableId);

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createWithEmptyOrderTable() {
        // given
        OrderRequest request = createOrderRequest(emptyOrderTable.getId());

        // when & then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given & when
        List<OrderResponse> responses = orderService.list();
        // then
        assertThat(responses).isEmpty();
    }

    @Test
    void changeOrderStatus() {
        // given
        Order savedOrder = orderDao.save(createOrder());

        // when
        OrderStatusChangeRequest changeRequest = createOrderChangeRequest();
        OrderResponse response = orderService.changeOrderStatus(
                savedOrder.getId(),
                changeRequest
        );

        // then
        assertThat(response.getOrderStatus()).isEqualTo(changeRequest.getOrderStatus());
    }

    @Test
    void changeOrderStatusWithInvalidOrder() {
        // given
        long invalidOrderId = 999L;

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(
                invalidOrderId,
                createOrderChangeRequest()
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatusWithAlreadyCompletedStatus() {
        // given
        String orderStatus = OrderStatus.COMPLETION.name();
        Order savedOrder = orderDao.save(createOrder(orderStatus));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(
                savedOrder.getId(),
                createOrderChangeRequest()
        )).isInstanceOf(IllegalArgumentException.class);
    }
}
