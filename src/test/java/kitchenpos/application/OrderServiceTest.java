package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceBaseTest {

    @Autowired
    protected OrderService orderService;

    private MenuGroup menuGroup;
    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        menuGroup = menuGroupDao.save(Fixture.menuGroup("메뉴 그룹"));
        menu = menuDao.save(Fixture.menu("메뉴1", 1000, menuGroup.getId(), null));
        orderTable = orderTableDao.save(Fixture.orderTable(null, 0, false));
        orderLineItem = Fixture.orderLineItem(orderTable.getId(), this.menu.getId(), 3L);
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        //given
        final Order order = Fixture.order(null, orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

        //when
        final Order createdOrder = orderService.create(order);

        //then
        assertAll(
                () -> assertThat(createdOrder.getOrderedTime()).isEqualTo(order.getOrderedTime()),
                () -> assertThat(createdOrder.getOrderLineItems()).usingRecursiveComparison()
                        .ignoringFields("id", "seq")
                        .isEqualTo(order.getOrderLineItems())
        );
    }

    @Test
    @DisplayName("주문 항목은 존재해야 한다.")
    void createValidOrder() {
        //given
        final Order order = Fixture.order(null, null, null, null);

        //when&then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴가 모두 존재해야한다.")
    void createValidOrderItem() {
        //given
        final OrderLineItem tmpOrderLineItem = Fixture.orderLineItem(null, null, 1L);
        final Order order = Fixture.order(orderTable.getId(), orderTable.getId(), LocalDateTime.now(), List.of(tmpOrderLineItem));

        //when&then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 시 주문 테이블이 존재해야한다.")
    void createValidOrderTable() {
        //given
        orderTable = orderTableDao.save(Fixture.orderTable(null, 0, true));
        final Order order = Fixture.order(null, orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

        //when&then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다.")
    void list() {
        //given
        final Order order = Fixture.order(orderTable.getId(), orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
        orderService.create(order);

        //when
        final List<Order> orders = orderService.list();

        //then
        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0)).usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems.seq")
                        .isEqualTo(order)
        );
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있다.")
    void changeOrderStatus() {
        //given
        final Order order = orderService.create(Fixture.order(orderTable.getId(), orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem)));
        order.setOrderStatus(COOKING.name());

        //when
        final Order changedOrder = orderService.changeOrderStatus(order.getId(), order);

        //then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(order.getOrderStatus());
    }

    @Test
    @DisplayName("상태를 변경하려는 주문은 존재해야한다.")
    void changeOrderStatusValidOrder() {
        //when&then
        assertThatThrownBy(() -> orderService.changeOrderStatus(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("완료된 주문은 상태를 변경할 수 없다.")
    void changeOrderStatusValidComplitionOrder() {
        //given
        final Order order = orderService.create(Fixture.order(orderTable.getId(), orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem)));
        order.setOrderStatus(COMPLETION.name());

        //when
        orderService.changeOrderStatus(order.getId(), order);

        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
