package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        orderRequest = createOrderRequest();
        savedOrder = orderDao.save(orderRequest);
    }

    private Order createOrderRequest() {
        OrderTable orderTable = orderTableDao.save(new OrderTable(null, 1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A그룹"));
        Menu menu = menuDao.save(new Menu("신메뉴", BigDecimal.ONE, menuGroup.getId(), List.of()));

        return new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                List.of(new OrderLineItem(null, menu.getId(), 1)));
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        //when
        Order order = orderService.create(orderRequest);

        //then
        assertThat(order.getId()).isNotNull();
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
}
