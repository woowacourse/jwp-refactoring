package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    private Order savedOrder;
    private Order orderRequest;

    @BeforeEach
    void setUp() {
        Menu menu = createMenu();
        orderRequest = createOrderRequest(menu.getId(), OrderStatus.MEAL);
        savedOrder = orderDao.save(orderRequest);
    }

    private Order createOrderRequest(Long menuId, OrderStatus orderStatus) {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));

        return new Order(orderTable.getId(), orderStatus.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(null, menuId, 1)));
    }

    private Menu createMenu() {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A그룹"));
        return menuDao.save(new Menu("신메뉴", BigDecimal.ONE, menuGroup.getId(), List.of()));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        //when
        Order order = orderService.create(orderRequest);

        //then
        assertThat(order.getId()).isNotNull();
    }

    @DisplayName("주문 항목이 없으면 예외가 발생한다.")
    @Test
    void createFailureWhenNotExistsOrderLineItems() {
        //given
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));
        final List<OrderLineItem> emptyOrderLineItem = Collections.emptyList();
        //when
        //then
        assertThatThrownBy(() -> orderService.create(
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), emptyOrderLineItem)));
    }

    @DisplayName("주문 항목의 메뉴가 디비안에 없으면 예외가 발생한다")
    @Test
    void createFailureWhenNotExistsMenu() {
        //given
        Order order = createOrderRequest(null, OrderStatus.COOKING);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 예외가 발생한다")
    @Test
    void createFailureWhenNotOrderTableIsEmpty() {
        //given
        Menu menu = createMenu();
        final Long emptyOrderTableId = null;

        Order order = new Order(emptyOrderTableId, OrderStatus.MEAL.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(null, menu.getId(), 1)));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록들을 반환한다.")
    @Test
    void list() {
        //when
        //then
        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문의 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        //given
        Order order = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now());

        //when
        orderService.changeOrderStatus(savedOrder.getId(), order);
        Order findOrder = orderDao.findById(savedOrder.getId()).get();

        //then
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @DisplayName("기존의 주문 상태가 COMPLETION일 때, 주문 상태를 바꾸려고 하면 예외가 발생한다")
    @Test
    void changeOrderStatusFailureWhenOrderStatusIsCOMPLETION() {
        //given
        Menu menu = createMenu();
        Order order = createOrderRequest(menu.getId(), OrderStatus.COMPLETION);
        //when
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), new Order()))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
