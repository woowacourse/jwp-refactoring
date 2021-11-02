package kitchenpos.application;

import static kitchenpos.application.fixture.MenuProductFixture.후라이드;
import static kitchenpos.application.fixture.OrderFixture.완료된_세번째테이블_주문;
import static kitchenpos.application.fixture.OrderFixture.조리중인_첫번째테이블_주문;
import static kitchenpos.application.fixture.OrderTableFixture.비어있는_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.한명있는_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.fixture.MenuFixture;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderLineItemFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 기능에서")
class OrderServiceTest {

    private OrderTableFixture orderTableFixture;
    private OrderDao orderDao;
    private MenuDao menuDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderTableFixture = OrderTableFixture.createFixture();
        orderLineItemDao = OrderLineItemFixture.createFixture().getOrderLineItemDao();
        orderTableDao = orderTableFixture.getOrderTableDao();
        orderDao = OrderFixture.createFixture().getOrderDao();
        menuDao = MenuFixture.createFixture().getTestMenuDao();

        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Nested
    @DisplayName("주문을 생성하면")
    class OrderCreateTest {

        @DisplayName("주문 항목 목록이 비어있다면 실패한다.")
        @Test
        void whenOrderLineItemsIsEmpty() {
            //given
            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderTableId(비어있는_테이블);

            //when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 항목에 중복된 메뉴가 있다면 실패한다.")
        @Test
        void whenOrderLineItemContainsSameMenu() {
            //given
            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderTableId(비어있는_테이블);

            OrderLineItem duplicateMenu1 = new OrderLineItem();
            duplicateMenu1.setMenuId(후라이드);
            duplicateMenu1.setQuantity(2L);

            OrderLineItem duplicateMenu2 = new OrderLineItem();
            duplicateMenu2.setMenuId(후라이드);
            duplicateMenu2.setQuantity(1L);

            order.setOrderLineItems(Arrays.asList(duplicateMenu1, duplicateMenu2));

            //when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 없으면 실패한다.")
        @Test
        void whenOrderTableNotPresent() {
            //given
            Long neverExistOrderTableId = 999_999_999_999L;

            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderTableId(neverExistOrderTableId);

            OrderLineItem duplicateMenu1 = new OrderLineItem();
            duplicateMenu1.setMenuId(후라이드);
            duplicateMenu1.setQuantity(2L);

            order.setOrderLineItems(Collections.singletonList(duplicateMenu1));

            //when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있지 않다면 실패한다.")
        @Test
        void whenOrderTableNotEmpty() {
            //given
            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderTableId(한명있는_테이블);

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(후라이드);
            orderLineItem.setQuantity(2L);

            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            //when & then
            assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void createTest() {
            //given
            Order order = new Order();
            order.setOrderStatus(OrderStatus.COOKING.name());
            order.setOrderedTime(LocalDateTime.now());
            order.setOrderTableId(비어있는_테이블);

            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(후라이드);
            orderLineItem.setQuantity(2L);

            order.setOrderLineItems(Collections.singletonList(orderLineItem));

            Order persistedOrder = orderService.create(order);

            //when & then
            assertAll(
                () -> assertThat(persistedOrder.getId()).isNotNull(),
                () -> assertThat(persistedOrder.getOrderStatus())
                    .isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(persistedOrder.getOrderTableId()).isEqualTo(비어있는_테이블)
            );
        }
    }

    @DisplayName("주문 목록을 받아올 수 있다.")
    @Test
    void listTest() {
        //given
        List<Order> expectedFixtures = OrderFixture.createFixture().getFixtures();

        List<Order> list = orderService.list();

        //when & then
        assertAll(
            () -> assertThat(list.size()).isEqualTo(expectedFixtures.size()),
            () -> expectedFixtures.forEach(
                order -> assertThat(list).usingRecursiveFieldByFieldElementComparator()
                    .usingElementComparatorIgnoringFields("orderLineItems", "orderedTime")
                    .contains(order)
            )
        );
    }

    @Nested
    @DisplayName("상태를 변경할 때")
    class changedOrderTest {

        @DisplayName("상태가 완료이면 실패한다.")
        @Test
        void whenStatusEqualsCompletion() {
            //given
            Order setStatus = new Order();
            setStatus.setOrderStatus(OrderStatus.COMPLETION.name());
            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(완료된_세번째테이블_주문, setStatus))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void changeOrderStatusTest() {
            //given
            Order setStatus = new Order();
            setStatus.setOrderStatus(OrderStatus.MEAL.name());
            //when
            Order changedOrder = orderService.changeOrderStatus(조리중인_첫번째테이블_주문, setStatus);
            //then
            assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
        }
    }
}