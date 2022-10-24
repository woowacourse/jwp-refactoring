package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문을 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup newMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = menuService.create(new Menu("후라이드+후라이드",
                new BigDecimal("19000"),
                newMenuGroup.getId(),
                List.of(new MenuProduct(1L, 2))));
        OrderTable orderTable = tableService.create(new OrderTable());
        Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 1)));

        // when
        Order savedOrder = orderService.create(order);

        // then
        Order dbOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow();
        assertThat(dbOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @DisplayName("주문을 생성할 때 주문항목이 빈 리스트이면 예외를 반환한다.")
    @Test
    void create_fail_if_orderLineItems_is_empty() {
        // given
        OrderTable orderTable = tableService.create(new OrderTable());
        Order order = new Order(orderTable.getId(), List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 메뉴라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_menu() {
        // given
        MenuGroup newMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = menuService.create(new Menu("후라이드+후라이드",
                new BigDecimal("19000"),
                newMenuGroup.getId(),
                List.of(new MenuProduct(1L, 2))));
        OrderTable orderTable = tableService.create(new OrderTable());
        Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId() + 1L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_orderTable() {
        // given
        MenuGroup newMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = menuService.create(new Menu("후라이드+후라이드",
                new BigDecimal("19000"),
                newMenuGroup.getId(),
                List.of(new MenuProduct(1L, 2))));
        OrderTable orderTable = tableService.create(new OrderTable());
        Order order = new Order(orderTable.getId() + 1L, List.of(new OrderLineItem(menu.getId(), 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup newMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));
        Menu menu = menuService.create(new Menu("후라이드+후라이드",
                new BigDecimal("19000"),
                newMenuGroup.getId(),
                List.of(new MenuProduct(1L, 2))));
        OrderTable orderTable = tableService.create(new OrderTable());
        Order order = new Order(orderTable.getId(), List.of(new OrderLineItem(menu.getId(), 1)));
        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        List<Long> orderTableIds = orders.stream()
                .map(Order::getOrderTableId)
                .collect(Collectors.toList());
        assertThat(orderTableIds).contains(order.getOrderTableId());
    }
}
