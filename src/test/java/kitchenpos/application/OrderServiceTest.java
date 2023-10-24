package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.*;
import kitchenpos.fixture.MenuFixtures;
import kitchenpos.fixture.MenuProductFixtures;
import kitchenpos.fixture.OrderFixtures;
import kitchenpos.fixture.OrderLineItemFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.한마리_메뉴;
import static kitchenpos.fixture.ProductFixtures.양념치킨_17000원;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class OrderServiceTest extends ServiceTest {

    @Autowired
    OrderTableDao orderTableDao;
    @Autowired
    ProductDao productDao;
    @Autowired
    MenuGroupDao menuGroupDao;
    @Autowired
    MenuDao menuDao;

    @Autowired
    OrderService orderService;

    @DisplayName("주문을 생성한다.")
    @Test
    void create() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(5, false));

        Menu savedMenu = menuDao.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedMenu.getId(), 1);
        Order order = OrderFixtures.create(
                savedOrderTable.getId(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );
        // when
        Order savedOrder = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).allMatch(
                        item -> item.getOrderId() != null
                )
        );
    }

    @DisplayName("주문 항목이 비어 있는 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_EmptyOrderLineItem_ExceptionThrown() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(5, false));

        Order order = OrderFixtures.create(
                savedOrderTable.getId(),
                LocalDateTime.now(),
                Collections.emptyList()
        );

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 주문을 생성하면 예외가 발생한다.")
    @Test
    void create_EmptyTable_ExceptionThrown() {
        // given
        OrderTable savedEmptyTable = orderTableDao.save(new OrderTable(5, true));

        Menu savedMenu = menuDao.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedMenu.getId(), 1);
        Order order = OrderFixtures.create(
                savedEmptyTable.getId(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("저장된 주문 목록을 전체 조회한다.")
    @Test
    void list() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(5, false));

        Menu savedMenu = menuDao.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedMenu.getId(), 1);
        Order order = OrderFixtures.create(
                savedOrderTable.getId(),
                LocalDateTime.now(),
                List.of(orderLineItem)
        );
        orderService.create(order);
        orderService.create(order);

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(5, false));

        Menu savedMenu = menuDao.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedMenu.getId(), 1);
        Order savedOrder = orderService.create(
                OrderFixtures.create(
                        savedOrderTable.getId(),
                        LocalDateTime.now(),
                        List.of(orderLineItem)
                )
        );

        savedOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo("MEAL");
    }

    @DisplayName("계산 완료된 주문의 상태를 변경하면 예외가 발생한다.")
    @Test
    void changeOrderStatus_AlreadyCompleted_ExceptionThrown() {
        // given
        OrderTable savedOrderTable = orderTableDao.save(new OrderTable(5, false));

        Menu savedMenu = menuDao.save(createMenu("양념치킨", 17_000));
        OrderLineItem orderLineItem = OrderLineItemFixtures.create(savedMenu.getId(), 1);
        Order savedOrder = orderService.create(
                OrderFixtures.create(
                        savedOrderTable.getId(),
                        LocalDateTime.now(),
                        List.of(orderLineItem)
                )
        );
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), savedOrder);

        changedOrder.setOrderStatus(OrderStatus.MEAL.name());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(changedOrder.getId(), changedOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu createMenu(String name, int price) {
        Product product = productDao.save(양념치킨_17000원);
        MenuProduct menuProduct = MenuProductFixtures.create(product, 1);
        MenuGroup menuGroup = menuGroupDao.save(한마리_메뉴);
        return MenuFixtures.create(
                name,
                price,
                menuGroup,
                List.of(menuProduct)
        );
    }
}
