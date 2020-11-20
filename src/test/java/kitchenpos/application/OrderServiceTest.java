package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderMenuDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderMenuRequest;
import kitchenpos.dto.OrderStatusChangeRequest;
import kitchenpos.exception.*;
import kitchenpos.fixture.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest extends TestFixture {

    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderMenuDao orderMenuDao;

    @Mock
    private TableDao tableDao;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderMenuDao, tableDao);
    }

    @DisplayName("주문 생성 예외 테스트: 주문이 비었을 때")
    @Test
    void createFailByEmptyOrderMenu() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(new ArrayList<>(), TABLE_ID_1);

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(NullRequestException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 중복 메뉴가 있을 때")
    @Test
    void createFailByDuplicatedMenu() {
        OrderMenuRequest orderMenuRequest1 = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_1);
        OrderMenuRequest orderMenuRequest2 = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_2);
        OrderCreateRequest duplicatedMenusOrderRequest =
            new OrderCreateRequest(Arrays.asList(orderMenuRequest1, orderMenuRequest2), TABLE_ID_1);

        given(menuDao.countByIdIn(any())).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(duplicatedMenusOrderRequest))
            .isInstanceOf(MenuNotExistException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 존재하지 않는 테이블에서 주문 할 때")
    @Test
    void createFailByNotExistTable() {
        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_1);

        OrderCreateRequest notExistTableOrderRequest =
            new OrderCreateRequest(Arrays.asList(orderMenuRequest), -1L);

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(tableDao.findById(notExistTableOrderRequest.getTableId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(notExistTableOrderRequest))
            .isInstanceOf(TableNotExistenceException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 빈 테이블에서 주문 할 때")
    @Test
    void createFailByEmptyTable() {
        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_1);
        OrderCreateRequest emptyTableOrderRequest =
            new OrderCreateRequest(Arrays.asList(orderMenuRequest), TABLE_ID_1);

        Table emptyTable = new Table(TABLE_ID_1, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_1, true);

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(tableDao.findById(TABLE_ID_1)).willReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> orderService.create(emptyTableOrderRequest))
            .isInstanceOf(TableEmptyException.class);
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void createTest() {
        OrderMenuRequest orderMenuRequest = new OrderMenuRequest(MENU_ID_1, ORDER_MENU_QUANTITY_1);
        OrderCreateRequest orderCreateRequest =
            new OrderCreateRequest(Arrays.asList(orderMenuRequest), TABLE_ID_1);

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(tableDao.findById(any())).willReturn(Optional.of(TABLE_1));
        given(orderDao.save(any())).willReturn(ORDER_1);
        given(orderMenuDao.save(any())).willReturn(ORDER_MENU_1);

        assertThat(orderService.create(orderCreateRequest)).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("주문 상태 변경 예외 테스트: 존재하지 않는 주문 id")
    @Test
    void changeOrderStatusFailByNotExistOrderId() {
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest();

        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID_1, orderStatusChangeRequest))
            .isInstanceOf(OrderNotExistException.class);
    }

    @DisplayName("주문 상태 변경 예외 테스트: 이미 완료 상태인 주문")
    @Test
    void changeOrderStatusFailByAlreadyCompletedOrder() {
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest();
        Order completedOrder = new Order(ORDER_ID_1, TABLE_ID_1, OrderStatus.COMPLETION, ORDERED_TIME_1);

        given(orderDao.findById(anyLong())).willReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID_1, orderStatusChangeRequest))
            .isInstanceOf(OrderStatusCannotChangeException.class);
    }

    @DisplayName("주문 상태 변경 성공 테스트")
    @Test
    void changeOrderStatus() {
        OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COOKING);

        given(orderDao.findById(anyLong())).willReturn(Optional.of(ORDER_1));
        given(orderDao.save(any())).willReturn(ORDER_1);

        assertThat(orderService.changeOrderStatus(ORDER_ID_1, orderStatusChangeRequest)).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void listTest() {
        given(orderDao.findAll()).willReturn(Arrays.asList(ORDER_1, ORDER_2));

        List<Order> orders = orderService.list();
        assertAll(
            () -> assertThat(orders).hasSize(2),
            () -> assertThat(orders.get(0)).usingRecursiveComparison().isEqualTo(ORDER_1),
            () -> assertThat(orders.get(1)).usingRecursiveComparison().isEqualTo(ORDER_2)
        );
    }
}