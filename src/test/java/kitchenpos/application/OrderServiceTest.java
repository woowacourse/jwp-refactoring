package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreationDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.ui.dto.request.OrderCreationRequest;
import kitchenpos.order.ui.dto.request.OrderLineItemReeust;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("OrderService 는 ")
@SpringTestWithData
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("주문 목록을 가져온다.")
    @Test
    void getOrders() {
        final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
        final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));
        final Menu menu = menuDao.save(new Menu("menuName", BigDecimal.valueOf(1500L),
                menuGroup.getId(),
                List.of(new MenuProduct(product, 2))));
        final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
        final OrderCreationRequest orderCreationRequest = new OrderCreationRequest(orderTable.getId(),
                List.of(new OrderLineItemReeust(menu.getId(), 2)));
        final OrderDto orderDto = orderService.create(OrderCreationDto.from(orderCreationRequest));

        final List<OrderDto> orders = orderService.getOrders();

        assertAll(
                () -> assertThat(orders.size()).isEqualTo(1),
                () -> assertThat(orders.get(0).getId()).isEqualTo(orderDto.getId())
        );
    }

    @DisplayName("주문을 생성할 떄 ")
    @SpringTestWithData
    @Nested
    class OrderCreationTest {

        @DisplayName("주문 생성이 가능하면 주문을 생성한다.")
        @Test
        void createOrderSuccess() {
            final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
            final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));
            final Menu menu = menuDao.save(new Menu("menuName", BigDecimal.valueOf(1500L),
                    menuGroup.getId(),
                    List.of(new MenuProduct(product, 2))));
            final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            final OrderCreationRequest orderCreationRequest = new OrderCreationRequest(orderTable.getId(),
                    List.of(new OrderLineItemReeust(menu.getId(), 2)));

            final OrderDto orderDto = orderService.create(OrderCreationDto.from(orderCreationRequest));

            assertAll(
                    () -> assertThat(orderDto.getId()).isGreaterThanOrEqualTo(1L),
                    () -> assertThat(orderDto.getOrderTableId()).isEqualTo(orderTable.getId())
            );
        }

        @DisplayName("메뉴가 존재하지 않으면 에러를 던진다.")
        @Test
        void createOrderFail() {
            final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
            final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));
            final Menu menu = menuDao.save(new Menu("menuName", BigDecimal.valueOf(1500L),
                    menuGroup.getId(),
                    List.of(new MenuProduct(product, 2))));
            final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            final OrderCreationRequest orderCreationRequest = new OrderCreationRequest(orderTable.getId(),
                    List.of(new OrderLineItemReeust(menu.getId(), 2), new OrderLineItemReeust(10L, 2)));

            assertThatThrownBy(() -> orderService.create(OrderCreationDto.from(orderCreationRequest)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 상태를 변결할 때 ")
    @SpringTestWithData
    @Nested
    class OrderStatusAlternationTest {

        @DisplayName("기존 주문의 상태가 종료가 아니면 상태를 변경한다.")
        @Test
        void changeOrderStatusSuccess() {
            final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
            final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));
            final Menu menu = menuDao.save(new Menu("menuName", BigDecimal.valueOf(1500L),
                    menuGroup.getId(),
                    List.of(new MenuProduct(product, 2))));
            final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            final OrderCreationRequest orderCreationRequest = new OrderCreationRequest(orderTable.getId(),
                    List.of(new OrderLineItemReeust(menu.getId(), 2)));
            final OrderDto orderDto = orderService.create(OrderCreationDto.from(orderCreationRequest));

            final OrderDto result = orderService.changeOrderStatus(orderDto.getId(), OrderStatus.COOKING.name());

            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        }

        @DisplayName("기존 주문 상태가 종료면 에러를 던진다.")
        @Test
        void changeOrderStatusFail() {
            final MenuGroup menuGroup = menuGroupDao.save(new MenuGroup("menuGroup"));
            final Product product = productDao.save(new Product("productName", BigDecimal.valueOf(1000L)));
            final Menu menu = menuDao.save(new Menu("menuName", BigDecimal.valueOf(1500L),
                    menuGroup.getId(),
                    List.of(new MenuProduct(product, 2))));
            final OrderTable orderTable = orderTableDao.save(new OrderTable(0, false));
            final OrderCreationRequest orderCreationRequest = new OrderCreationRequest(orderTable.getId(),
                    List.of(new OrderLineItemReeust(menu.getId(), 2)));
            final OrderDto orderDto = orderService.create(OrderCreationDto.from(orderCreationRequest));
            orderService.changeOrderStatus(orderDto.getId(), OrderStatus.COMPLETION.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(orderDto.getId(), OrderStatus.COOKING.name()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
