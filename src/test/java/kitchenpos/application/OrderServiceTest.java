package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private OrderDao orderDao;

    private Order fixtureOrder;

    @BeforeEach
    void setUp(){
        TableGroup tableGroup = tableGroupDao.save(new TableGroup(LocalDateTime.now()));
        OrderTable orderTable = orderTableDao.save(new OrderTable(tableGroup.getId(), 1, false));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("A그룹"));

        Menu menu = menuDao.save(new Menu("신메뉴", BigDecimal.ONE, menuGroup.getId(), List.of()));

        Order order = new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now());
        order.setOrderLineItems(List.of(new OrderLineItem(order.getId(), menu.getId(), 1)));
        fixtureOrder = orderService.create(order);
    }

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        Order order = orderService.create(fixtureOrder);

        assertThat(order.getId()).isNotNull();
    }

    @DisplayName("주문의 목록들을 반환한다.")
    @Test
    void list() {

        assertThat(orderService.list()).hasSize(1);
    }

    @DisplayName("주문의 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        Order savedOrder = new Order(1L, OrderStatus.COOKING.name(), LocalDateTime.now());
        orderService.changeOrderStatus(fixtureOrder.getId(), savedOrder);
        Order order = orderDao.findById(fixtureOrder.getId()).get();

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }
}
