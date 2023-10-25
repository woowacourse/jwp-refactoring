package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.dto.ChangeOrderDto;
import kitchenpos.application.dto.CreateOrderDto;
import kitchenpos.application.dto.ReadOrderDto;
import kitchenpos.application.exception.MenuNotFoundException;
import kitchenpos.application.exception.OrderTableNotFoundException;
import kitchenpos.config.IntegrationTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.exception.InvalidOrderLineItemException;
import kitchenpos.domain.exception.InvalidOrderStatusException;
import kitchenpos.domain.menugroup.repository.MenuGroupRepository;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.ordertable.repository.OrderTableRepository;
import kitchenpos.domain.product.repository.ProductRepository;
import kitchenpos.ui.dto.request.CreateOrderLineItemRequest;
import kitchenpos.ui.dto.request.CreateOrderRequest;
import kitchenpos.ui.dto.request.UpdateOrderStatusRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Test
    void create_메서드는_order를_전달하면_order를_저장하고_반환한다() {
        // given
        final Menu persistMenu = persistMenu();
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(persistMenu.getId(), 1L);
        final CreateOrderRequest request = new CreateOrderRequest(persistOrderTable.getId(), List.of(createOrderLineItemRequest));

        // when
        final CreateOrderDto actual = orderService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @Test
    void create_메서드는_order의_orderLineItem이_없다면_예외가_발생한다() {
        // given
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final CreateOrderRequest invalidRequest = new CreateOrderRequest(persistOrderTable.getId(), Collections.emptyList());

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(InvalidOrderLineItemException.class);
    }

    @Test
    void create_메서드는_order의_orderLineItem의_menu가_없다면_예외가_발생한다() {
        // given
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(-999L, 1L);
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final CreateOrderRequest invalidRequest = new CreateOrderRequest(persistOrderTable.getId(), List.of(createOrderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @Test
    void create_메서드는_order의_orderTable이_없다면_예외가_발생한다() {
        // given
        final Menu persistMenu = persistMenu();
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(persistMenu.getId(), 1L);
        final CreateOrderRequest invalidRequest = new CreateOrderRequest(-999L, List.of(createOrderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void list_메서드는_등록한_모든_order를_반환한다() {
        // given
        final Menu persistMenu = persistMenu();
        final Order expected = persistOrder(persistMenu, OrderStatus.COOKING);

        // when
        final List<ReadOrderDto> actual = orderService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getId()).isEqualTo(expected.getId())
        );
    }

    @ParameterizedTest(name = "orderStatus를 {0}으로 변경한다")
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_메서드는_전달한_orderId의_상태가_COMPLETION이_아닌_order라면_전달한_상태로_변경한다(final String orderStatus) {
        // given
        final Menu persistMenu = persistMenu();
        final Order persistOrder = persistOrder(persistMenu, OrderStatus.COOKING);
        final UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(orderStatus);

        // when
        final ChangeOrderDto actual = orderService.changeOrderStatus(persistOrder.getId(), request);

        // then
        assertThat(actual.getOrderStatus().name()).isEqualTo(orderStatus);
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_메서드는_전달한_orderId의_상태가_COMPLETION이라면_예외가_발생한다(final String orderStatus) {
        // given
        final Menu persistMenu = persistMenu();
        final Order persistOrder = persistOrder(persistMenu, OrderStatus.COMPLETION);
        final UpdateOrderStatusRequest invalidRequest = new UpdateOrderStatusRequest(orderStatus);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(persistOrder.getId(), invalidRequest))
                .isInstanceOf(InvalidOrderStatusException.class);
    }

    private Menu persistMenu() {
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(
                persistProduct.getId(),
                persistProduct.price(),
                persistProduct.name(),
                1L
        );

        return menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup.getId())
        );
    }

    private Order persistOrder(final Menu persistMenu, final OrderStatus orderStatus) {
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final OrderLineItem persistOrderLineItem = new OrderLineItem(persistMenu.getId(), 1L);

        return orderRepository.save(
                new Order(persistOrderTable.getId(), orderStatus, LocalDateTime.now(), List.of(persistOrderLineItem))
        );
    }
}
