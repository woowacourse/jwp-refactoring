package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

import static kitchenpos.application.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private ProductService productService;

    @Transactional
    @BeforeEach
    void setUp() {
        menuGroupService.create(MENU_GROUP);
        productService.create(PRODUCT);
        menuService.create(MENU);

        final OrderTable firstOrderTable = orderTableDao.save(FIRST_ORDER_TABLE);
        final OrderTable secondOrderTable = orderTableDao.save(SECOND_ORDER_TABLE);
        TABLE_GROUP.setOrderTables(Arrays.asList(firstOrderTable, secondOrderTable));
        tableGroupService.create(TABLE_GROUP);
    }

    @Test
    @DisplayName("주문 생성")
    void createTest() {

        // when
        final Order order = orderService.create(COOKING_ORDER);

        // then
        assertThat(orderDao.findById(1L).get()).isEqualTo(order);
    }

    @Test
    @DisplayName("주문 목록 조회")
    void listTest() {

        // given
        final Order order = orderService.create(COOKING_ORDER);

        // when
        final List<Order> orders = orderService.list();

        // then
        assertThat(orders).contains(order);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatusTest() {

        // given
        final Order cookingOrder = orderService.create(COOKING_ORDER);
        final Order completionOrder = orderService.create(COMPLETION_ORDER);
        completionOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // when
        final Order changedOrder = orderService.changeOrderStatus(cookingOrder.getId(), completionOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }
}
