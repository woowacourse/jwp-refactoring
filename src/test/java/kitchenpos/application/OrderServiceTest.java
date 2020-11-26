package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.utils.TestObjectFactory.createOrderTableCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderChangeRequest;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "classpath:/init-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(2, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        OrderResponse savedOrder = orderService.create(orderCreateRequest);

        assertAll(() -> {
            assertThat(savedOrder).isInstanceOf(OrderResponse.class);
            assertThat(savedOrder.getId()).isNotNull();
            assertThat(savedOrder.getOrderTableId()).isNotNull();
            assertThat(savedOrder.getOrderStatus()).isNotNull();
            assertThat(savedOrder.getOrderStatus()).isEqualTo(COOKING.name());
            assertThat(savedOrder.getOrderLineItemResponses()).isNotNull();
            assertThat(savedOrder.getOrderLineItemResponses()).isNotEmpty();
        });
    }

    @DisplayName("주문을 생성한다. - 메뉴가 존재하지 않을 경우")
    @Test
    void create_IfMenuNotExist_ThrowException() {
        Menu menu = menuRepository.getOne(0L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다. - 메뉴 리스트가 비어있을 경우")
    @Test
    void create_IfEmptyMenuList_ThrowException() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        List<OrderLineItem> orderLineItems
            = Collections.emptyList();
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다. - 주문 테이블이 존재하지 않을 경우")
    @Test
    void create_IfTableNotExist_ThrowException() {
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderTable orderedTable = new OrderTable(0L,null,0,false);
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(orderedTable, COOKING.name(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성한다. - 주문 테이블이 비어있을 경우")
    @Test
    void create_IfTableEmpty_ThrowException() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, true));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));

        assertThatThrownBy(() -> TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest orderCreateRequest
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        orderService.create(orderCreateRequest);

        List<OrderResponse> orderResponses = orderService.list();

        assertAll(() -> {
            assertThat(orderResponses).isNotEmpty();
            assertThat(orderResponses).hasSize(1);
        });
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest oldOrder
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        OrderChangeRequest newOrder
            = TestObjectFactory.createOrderChangeRequest(MEAL.name());

        OrderResponse savedOldOrder = orderService.create(oldOrder);
        orderService.changeOrderStatus(savedOldOrder.getId(), newOrder);
        OrderResponse savedNewOrder = orderService.list()
            .get(0);

        assertThat(savedNewOrder.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("주문 상태를 변경한다. - 존재하지 않는 주문일 경우")
    @Test
    void changeOrderStatus_NotExistOrder_ThrowException() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest oldOrder
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        OrderChangeRequest newOrder
            = TestObjectFactory.createOrderChangeRequest(MEAL.name());

        orderService.create(oldOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(0L, newOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다. - 계산 완료인 경우")
    @Test
    void changeOrderStatus_AlreadyPayed_ThrowException() {
        OrderTableResponse orderTableResponse
            = tableService.create(createOrderTableCreateRequest(1, false));
        TableGroup tableGroup = tableGroupRepository.getOne(orderTableResponse.getId());
        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest oldOrder
            = TestObjectFactory
            .createOrderCreateRequest(orderTableResponse.toEntity(tableGroup), COOKING.name(),
                orderLineItems);
        OrderChangeRequest newOrder
            = TestObjectFactory.createOrderChangeRequest(COMPLETION.name());

        OrderResponse savedOldOrder = orderService.create(oldOrder);
        orderService.changeOrderStatus(savedOldOrder.getId(), newOrder);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOldOrder.getId(), newOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
