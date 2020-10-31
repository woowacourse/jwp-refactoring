package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderMenuDao;
import kitchenpos.dao.TableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.Table;
import kitchenpos.fixture.TestFixture;

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
        Order emptyLineItemOrder = new Order();
        emptyLineItemOrder.setId(ORDER_ID_1);
        emptyLineItemOrder.setTableId(TABLE_ID_1);
        emptyLineItemOrder.setOrderStatus(ORDER_STATUS_1);
        emptyLineItemOrder.setOrderedTime(ORDERED_TIME_1);
        emptyLineItemOrder.setOrderMenus(new ArrayList<>());

        assertThatThrownBy(() -> orderService.create(emptyLineItemOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 중복 메뉴가 있을 때")
    @Test
    void createFailByDuplicatedMenu() {
        Order duplicatedMenusOrder = new Order();
        duplicatedMenusOrder.setId(ORDER_ID_1);
        duplicatedMenusOrder.setTableId(TABLE_ID_1);
        duplicatedMenusOrder.setOrderStatus(ORDER_STATUS_1);
        duplicatedMenusOrder.setOrderedTime(ORDERED_TIME_1);
        duplicatedMenusOrder.setOrderMenus(Arrays.asList(ORDER_MENU_1, ORDER_MENU_1));

        given(menuDao.countByIdIn(any())).willReturn(1L);

        assertThatThrownBy(() -> orderService.create(duplicatedMenusOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 존재하지 않는 테이블 테이블에서 주문 할 때")
    @Test
    void createFailByNotExistTable() {
        Order notExistTableOrder = ORDER_1;

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(tableDao.findById(notExistTableOrder.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(notExistTableOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 예외 테스트: 빈 테이블에서 주문 할 때")
    @Test
    void createFailByEmptyTable() {
        Order emptyTableOrder = ORDER_1;

        Table emptyTable = new Table(TABLE_ID_1, TABLE_GROUP_ID, TABLE_NUMBER_OF_GUESTS_1, true);

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(tableDao.findById(emptyTableOrder.getId())).willReturn(Optional.of(emptyTable));

        assertThatThrownBy(() -> orderService.create(emptyTableOrder))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void createTest() {
        given(menuDao.countByIdIn(any())).willReturn((long) ORDER_1.getOrderMenus().size());
        given(tableDao.findById(any())).willReturn(Optional.of(TABLE_1));
        given(orderDao.save(any())).willReturn(ORDER_1);
        given(orderMenuDao.save(ORDER_MENU_1)).willReturn(ORDER_MENU_1);

        assertThat(orderService.create(ORDER_1)).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("주문 상태 변경 예외 테스트: 존재하지 않는 주문 id")
    @Test
    void changeOrderStatusFailByNotExistOrderId() {
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID_1, ORDER_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 예외 테스트: 이미 완료 상태인 주문")
    @Test
    void changeOrderStatusFailByAlreadyCompletedOrder() {
        Order completedOrder = new Order();
        completedOrder.setId(ORDER_ID_1);
        completedOrder.setTableId(TABLE_ID_1);
        completedOrder.setOrderStatus("COMPLETION");
        completedOrder.setOrderedTime(ORDERED_TIME_1);
        completedOrder.setOrderMenus(ORDER_MENUS_1);

        given(orderDao.findById(anyLong())).willReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(ORDER_ID_1, ORDER_1))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 변경 성공 테스트")
    @Test
    void changeOrderStatus() {
        given(orderDao.findById(anyLong())).willReturn(Optional.of(ORDER_1));
        given(orderDao.save(any())).willReturn(ORDER_1);
        given(orderMenuDao.findAllByOrderId(ORDER_ID_1)).willReturn(ORDER_MENUS_1);

        assertThat(orderService.changeOrderStatus(ORDER_ID_1, ORDER_1)).usingRecursiveComparison().isEqualTo(ORDER_1);
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void listTest() {
        given(orderDao.findAll()).willReturn(Arrays.asList(ORDER_1, ORDER_2));
        given(orderMenuDao.findAllByOrderId(ORDER_ID_1)).willReturn(ORDER_MENUS_1);
        given(orderMenuDao.findAllByOrderId(ORDER_ID_2)).willReturn(ORDER_MENUS_2);

        List<Order> orders = orderService.list();
        assertAll(
            () -> assertThat(orders).hasSize(2),
            () -> assertThat(orders.get(0)).usingRecursiveComparison().isEqualTo(ORDER_1),
            () -> assertThat(orders.get(1)).usingRecursiveComparison().isEqualTo(ORDER_2)
        );
    }
}