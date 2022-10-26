package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
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
    private OrderDao orderDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao tableDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("주문을 생성한다.")
    @Test
    void create_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = menuDao.save(createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2))));
        OrderTable orderTable = tableDao.save(createOrderTable());
        Order order = createOrder(orderTable.getId(), Collections.singletonList(createOrderLineItem(menu.getId(), 1)));

        // when
        Order savedOrder = orderService.create(order);

        // then
        Order dbOrder = orderDao.findById(savedOrder.getId())
                .orElseThrow(RuntimeException::new);
        assertThat(dbOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
    }

    @DisplayName("주문을 생성할 때 주문항목이 빈 리스트이면 예외를 반환한다.")
    @Test
    void create_fail_if_orderLineItems_is_empty() {
        // given
        OrderTable orderTable = tableDao.save(createOrderTable());
        Order order = createOrder(orderTable.getId(), new ArrayList<>());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 메뉴라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_menu() {
        // given
        OrderTable orderTable = tableDao.save(new OrderTable());
        Order order = createOrder(orderTable.getId(), Collections.singletonList(createOrderLineItem(9999999L, 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 때 존재하지 않는 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_not_exist_orderTable() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = menuDao.save(createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2))));
        Order order = createOrder(9999999L, Collections.singletonList(createOrderLineItem(menu.getId(), 1)));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 메뉴를 조회한다.")
    @Test
    void list_success() {
        // given
        MenuGroup newMenuGroup = menuGroupDao.save(createMenuGroup("추천메뉴"));
        Menu menu = menuDao.save(createMenu("후라이드+후라이드", 19_000L, newMenuGroup.getId(),
                Collections.singletonList(createMenuProduct(1L, 2))));
        OrderTable orderTable = tableDao.save(createOrderTable());
        Order order = orderDao.save(createOrder(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                Collections.singletonList(createOrderLineItem(menu.getId(), 1))));

        // when
        List<Order> orders = orderService.list();

        // then
        List<Long> orderTableIds = orders.stream()
                .map(Order::getOrderTableId)
                .collect(Collectors.toList());
        assertThat(orderTableIds).contains(order.getOrderTableId());
    }
}
