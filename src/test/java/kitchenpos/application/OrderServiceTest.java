package kitchenpos.application;

import kitchenpos.application.common.MenuFixtureFactory;
import kitchenpos.application.common.TestObjectFactory;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemDto;
import kitchenpos.dto.order.OrderResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/delete_all.sql")
class OrderServiceTest extends MenuFixtureFactory {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private MenuDao menuDao;

    @DisplayName("주문 생성 메서드 테스트")
    @Test
    void create() {
        OrderResponse savedOrder = orderService.create(makeOrderCreateRequest());

        List<OrderLineItemDto> savedOrderLineItems = savedOrder.getOrderLineItemDtos();
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING),
                () -> assertThat(savedOrderLineItems.get(0).getOrderId()).isEqualTo(savedOrder.getId())
        );
    }

    @DisplayName("주문 생성 메서드 - 테이블이 빈 테이블인 경우 예외 처리")
    @Test
    void createWhenEmptyTable() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        List<OrderLineItemDto> orderLineItemDtos = Arrays.asList(new OrderLineItemDto(1L, 1));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemDtos);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 메서드 - OrderLineItems가 빈 경우 예외 처리")
    @Test
    void createWhenEmptyOrderLineItems() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        ArrayList<OrderLineItemDto> orderLineItemDtos = new ArrayList<>();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemDtos);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 주문 요청 시 orderLineItems의 menuId가 존재하지 않는 menu의 아이디일 경우 예외처리")
    @Test
    void createWhenIllegalMenuId() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        ArrayList<OrderLineItemDto> orderLineItemDtos = new ArrayList<>();
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(savedOrderTable.getId(), orderLineItemDtos);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        orderService.create(makeOrderCreateRequest());
        orderService.create(makeOrderCreateRequest());

        assertThat(orderService.list()).hasSize(2);
    }

    @DisplayName("주문 상태 변경 메서드 테스트")
    @Test
    void changeOrderStatus() {
        OrderResponse savedOrder = orderService.create(makeOrderCreateRequest());
        Order changeOrderStatusDto = TestObjectFactory.createChangeOrderStatusDto(OrderStatus.MEAL);

        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDto);

        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("주문 상태 변경 - 이미 완료 상태인 경우 예외 처리")
    @Test
    void changeOrderStatusWhenCompletion() {
        OrderResponse savedOrder = orderService.create(makeOrderCreateRequest());
        Order changeOrderStatusDtoToCompletion = TestObjectFactory.createChangeOrderStatusDto(OrderStatus.COMPLETION);

        orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDtoToCompletion);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusDtoToCompletion))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderCreateRequest makeOrderCreateRequest() {
        OrderTable orderTable = TestObjectFactory.creatOrderTable();
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Menu savedMenu1 = menuDao.save(makeMenuToSave("추천메뉴", "양념", 12000));
        Menu savedMenu2 = menuDao.save(makeMenuToSave("추천메뉴", "후라이드", 11000));

        List<OrderLineItemDto> orderLineItems = Arrays.asList(
                new OrderLineItemDto(savedMenu1.getId(), 1),
                new OrderLineItemDto(savedMenu2.getId(), 1)
        );

        return new OrderCreateRequest(savedOrderTable.getId(), orderLineItems);
    }

    @AfterEach
    void tearDown() {
        orderLineItemDao.deleteAll();
        orderDao.deleteAll();
        orderTableDao.deleteAll();
        menuDao.deleteAll();
        menuGroupDao.deleteAll();
        productDao.deleteAll();
    }
}
