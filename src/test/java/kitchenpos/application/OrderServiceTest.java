package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.OrderCreationDto;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.common.annotation.SpringTestWithData;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.OrderCreationRequest;
import kitchenpos.ui.dto.request.OrderLineItemReeust;
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

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() {
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
