package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("Order 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class OrderServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private Order order;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        orderTable = tableService.create(OrderTableFixture.TABLE_BEFORE_SAVE_EMPTY_FALSE);

        final Order beforeSaveOrder = new Order();
        beforeSaveOrder.setOrderTableId(orderTable.getId());
        beforeSaveOrder.setOrderLineItems(Collections.singletonList(OrderLineItemFixture.FIRST_FIRST_ORDERLINE_NO_SEQ_NO_ORDER));

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        order = orderService.create(beforeSaveOrder);
    }

    @DisplayName("주문 저장 - 실패 - 주문항목이 비어있음")
    @Test
    void createFailureWhenOrderLinesEmpty() {
        //given
        final Order order = new Order();
        order.setOrderTableId(OrderTableFixture.FIRST.getId());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 저장된 메뉴 수와 요청했던 메뉴 수가 다름")
    @Test
    void createFailureWhenMenusNotMatch() {
        //given
        final OrderLineItem firedOrderLineItem = new OrderLineItem();
        firedOrderLineItem.setMenuId(MenuFixture.후라이드치킨.getId());
        firedOrderLineItem.setQuantity(1);
        final OrderLineItem soyOrderLineItem = new OrderLineItem();
        soyOrderLineItem.setMenuId(0L);
        soyOrderLineItem.setQuantity(1);
        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Arrays.asList(firedOrderLineItem, soyOrderLineItem));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 주문테이블의 empty값이 true")
    @Test
    void createFailureWhenOrderTableEmptyTrue() {
        //given
        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 - 실패 - 주문테이블의 존재하지 않음")
    @Test
    void createFailureWhenOrderTableNotFOUNd() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 조회 - 성공 - 전체 주문 조회")
    @Test
    void findAll() {
        //given
        //when
        final List<Order> actual = orderService.list();
        //then
        assertThat(actual).hasSize(1);
        assertThat(actual).extracting(Order::getId)
                .contains(order.getId());
        assertThat(actual).flatExtracting(Order::getOrderLineItems)
                .isNotEmpty();
    }

    @DisplayName("주문 상태 수정 - 성공")
    @Test
    void changeOrderStatus() {
        //given
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(order.getOrderTableId());
        orderTable.setEmpty(false);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        //when
        final Order actual = orderService.changeOrderStatus(order.getId(), OrderFixture.MEAL_STATUS);
        //then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 수정 - 실패 - 주문이 존재하지 않음")
    @Test
    void changeOrderStatusFailureWhenNotFoundOrder() {
        //given
        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(OrderFixture.FIRST_TABLE_후라이드치킨_하나_NO_KEY.getId(), OrderFixture.MEAL_STATUS))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태 수정 - 실패 - 주문상태가 COMPLETION")
    @Test
    void changeOrderStatusFailureOrderStatusCompletion() {
        //given
        //when
        orderService.changeOrderStatus(order.getId(), OrderFixture.COMPLETION_STATUS);
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(OrderFixture.FIRST_TABLE_후라이드치킨_하나_NO_KEY.getId(), OrderFixture.MEAL_STATUS))
                .isInstanceOf(IllegalArgumentException.class);
    }
}