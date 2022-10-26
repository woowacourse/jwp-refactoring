package kitchenpos.application;

import static kitchenpos.fixtures.domain.MenuFixture.createMenu;
import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixtures.domain.MenuProductFixture.createMenuProduct;
import static kitchenpos.fixtures.domain.OrderFixture.createOrder;
import static kitchenpos.fixtures.domain.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixtures.domain.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixtures.domain.OrderFixture.OrderRequestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    private Order order;

    @BeforeEach
    void setUp() {
        Product product = productDao.save(createProduct("짜장면", new BigDecimal(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(createMenuGroup("중식"));
        MenuProduct menuProduct = createMenuProduct(product.getId(), 1L);

        this.menu = menuDao.save(createMenu("세트1", new BigDecimal(10_000), menuGroup.getId(), List.of(menuProduct)));
        this.orderLineItem = createOrderLineItem(menu.getId(), 1L);
        this.orderTable = orderTableDao.save(new OrderTable(1, false));
        this.order = orderDao.save(
                createOrder(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem)));
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            Order order = new OrderRequestBuilder()
                    .orderTableId(orderTable.getId())
                    .addOrderLineItem(orderLineItem)
                    .build();

            // when
            Order actual = orderService.create(order);

            // then
            assertAll(() -> {
                assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus());
                assertThat(actual.getOrderedTime()).isEqualTo(order.getOrderedTime());
            });
        }

        @DisplayName("주문의 주문 항목이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderLineItemsIsEmpty() {
            // given
            Order order = new OrderRequestBuilder()
                    .orderTableId(orderTable.getId())
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 항목의 메뉴가 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuDoesNotExist() {
            // given
            OrderLineItem orderLineItemHasNotSavedMenu = createOrderLineItem(menu.getId() + 1, 1L);
            Order order = new OrderRequestBuilder()
                    .orderTableId(orderTable.getId())
                    .addOrderLineItem(orderLineItemHasNotSavedMenu)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            long notSavedOrderTableId = orderTable.getId() + 1;
            Order order = new OrderRequestBuilder()
                    .orderTableId(notSavedOrderTableId)
                    .addOrderLineItem(orderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable emptyTable = orderTableDao.save(new OrderTable(1, true));
            Order order = new OrderRequestBuilder()
                    .orderTableId(emptyTable.getId())
                    .addOrderLineItem(orderLineItem)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("저장된 모든 주문 목록을 조회한다.")
        @Test
        void Should_ReturnAllOrderList() {
            // given
            int expected = 3;
            for (int i = 0; i < expected; i++) {
                Order order = createOrder(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(orderLineItem));
                orderDao.save(order);
            }

            // when
            List<Order> actual = orderService.list();

            // then
            assertThat(actual).hasSize(expected + 1); // TODO: @BeforeEach에 영향받는 assertion
        }
    }

    @DisplayName("changeOrderStatus 메소드는")
    @Nested
    class ChangeOrderStatusMethod {

        @DisplayName("주문의 주문 상태를 변경한다.")
        @ValueSource(strings = {"MEAL", "COMPLETION"})
        @ParameterizedTest
        void Should_ChangeOrderStatus(final String after) {
            // given
            Order oldOrder = createOrder(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
                    List.of(orderLineItem));
            orderDao.save(oldOrder);

            Order orderRequest = new OrderRequestBuilder()
                    .orderStatus(after)
                    .build();

            // when
            Order actual = orderService.changeOrderStatus(order.getId(), orderRequest);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(after);
        }

        @DisplayName("주문이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderDoesNotExist() {
            // given
            Order orderRequest = new OrderRequestBuilder().build();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 상태가 COMPLETION 이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            Order order = orderDao.save(createOrder(orderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
                    List.of(orderLineItem)));

            Order request = new OrderRequestBuilder()
                    .orderStatus(OrderStatus.COOKING)
                    .build();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
