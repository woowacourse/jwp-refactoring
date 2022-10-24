package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
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
        Product product = productDao.save(new Product("짜장면", new BigDecimal(15_000)));
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("중식"));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 1L);

        this.menu = menuDao.save(new Menu("세트1", new BigDecimal(10_000), menuGroup.getId(), List.of(menuProduct)));
        this.orderLineItem = new OrderLineItem(menu.getId(), 1L);
        this.orderTable = orderTableDao.save(new OrderTable(1, false));
        this.order = orderService.create(new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem)));
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문을 생성한다.")
        @Test
        void Should_CreateOrder() {
            // given
            Order order = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

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
            Order order = new Order(orderTable.getId(), LocalDateTime.now(), List.of());

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 항목의 메뉴가 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_MenuDoesNotExist() {
            // given
            OrderLineItem orderLineItem = new OrderLineItem(menu.getId() + 1, 1L);
            Order order = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            Order order = new Order(orderTable.getId() + 1, LocalDateTime.now(), List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 주문 테이블이 빈 테이블이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            OrderTable emptyTable = orderTableDao.save(new OrderTable(1, true));
            Order order = new Order(emptyTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

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
            Order order1 = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
            Order order2 = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
            Order order3 = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

            orderService.create(order1);
            orderService.create(order2);
            orderService.create(order3);

            // when
            List<Order> actual = orderService.list();

            // then
            assertThat(actual).hasSize(4);
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
            Order oldOrder = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
            orderService.create(oldOrder);

            Order newOrder = new Order(after);

            // when
            Order actual = orderService.changeOrderStatus(order.getId(), newOrder);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(after);
        }

        @DisplayName("주문이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderDoesNotExist() {
            // given
            Order newOrder = new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId() + 1, newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문의 상태가 COMPLETION 이라면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderStatusIsCompletion() {
            // given
            Order order = orderService.create(
                    new Order(orderTable.getId(), LocalDateTime.now(), List.of(orderLineItem))
            );
            orderService.changeOrderStatus(order.getId(), new Order(OrderStatus.COMPLETION.name()));

            Order newOrder = new Order(OrderStatus.COOKING.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), newOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
