package kitchenpos.application;

import static kitchenpos.OrderFixtures.createOrder;
import static kitchenpos.OrderFixtures.createOrderChangeRequest;
import static kitchenpos.OrderFixtures.createOrderLineItemRequest;
import static kitchenpos.OrderFixtures.createOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.MenuFixtures;
import kitchenpos.OrderTableFixtures;
import kitchenpos.TableGroupFixtures;
import kitchenpos.application.dto.request.OrderRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    private final OrderService orderService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public OrderServiceTest(
            OrderService orderService,
            TableGroupRepository tableGroupRepository,
            OrderTableRepository orderTableRepository,
            OrderRepository orderRepository,
            MenuRepository menuRepository
    ) {
        this.orderService = orderService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
    }

    private TableGroup tableGroup;
    private OrderTable emptyOrderTable;
    private OrderTable filledOrderTable;

    @BeforeEach
    void setUp() {
        this.tableGroup = tableGroupRepository.save(TableGroupFixtures.createTableGroup());
        this.emptyOrderTable = orderTableRepository.save(
                OrderTableFixtures.createOrderTable(
                        tableGroup.getId(),
                        0,
                        true
                )
        );
        this.filledOrderTable = orderTableRepository.save(
                OrderTableFixtures.createOrderTable(
                        tableGroup.getId(),
                        2,
                        false
                )
        );
    }

    @Test
    void create() {
        // given
        Menu menu = menuRepository.save(MenuFixtures.createMenu());
        OrderRequest request = createOrderRequest(
                filledOrderTable.getId(),
                List.of(createOrderLineItemRequest(menu.getId()))
        );

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
                .hasCauseInstanceOf(IllegalArgumentException.class);
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
        Order savedOrder = orderRepository.save(createOrder());

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
        )).hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatusWithAlreadyCompletedStatus() {
        // given
        OrderStatus orderStatus = OrderStatus.COMPLETION;
        Order savedOrder = orderRepository.save(createOrder(orderStatus));

        // when
        assertThatThrownBy(() -> orderService.changeOrderStatus(
                savedOrder.getId(),
                createOrderChangeRequest()
        )).isInstanceOf(IllegalStateException.class);
    }
}
