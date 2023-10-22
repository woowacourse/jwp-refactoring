package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.config.IntegrationTest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
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
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(persistMenu.getId(), 1L);
        final CreateOrderRequest request = new CreateOrderRequest(persistOrderTable.getId(), List.of(createOrderLineItemRequest));

        // when
        final Order actual = orderService.create(request);

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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_order의_orderLineItem의_menu가_없다면_예외가_발생한다() {
        // given
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(-999L, 1L);
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final CreateOrderRequest invalidRequest = new CreateOrderRequest(persistOrderTable.getId(), List.of(createOrderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_메서드는_order의_orderTable이_없다면_예외가_발생한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = Menu.of("메뉴", BigDecimal.TEN, List.of(persistMenuProduct), persistMenuGroup);
        final CreateOrderLineItemRequest createOrderLineItemRequest = new CreateOrderLineItemRequest(persistMenu.getId(), 1L);
        final CreateOrderRequest invalidRequest = new CreateOrderRequest(-999L, List.of(createOrderLineItemRequest));

        // when & then
        assertThatThrownBy(() -> orderService.create(invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list_메서드는_등록한_모든_order를_반환한다() {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final OrderLineItem persistOrderLineItem = new OrderLineItem(persistMenu, 1L);
        final Order expected = orderRepository.save(
                new Order(persistOrderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(persistOrderLineItem))
        );

        // when
        final List<Order> actual = orderService.list();

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
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final OrderLineItem orderLineItem = new OrderLineItem(persistMenu, 1L);
        final Order persistOrder = orderRepository.save(
                new Order(persistOrderTable, OrderStatus.COOKING, LocalDateTime.now(), List.of(orderLineItem))
        );
        final UpdateOrderStatusRequest request = new UpdateOrderStatusRequest(orderStatus);

        // when
        final Order actual = orderService.changeOrderStatus(persistOrder.getId(), request);

        // then
        assertThat(actual.getOrderStatus().name()).isEqualTo(orderStatus);
    }

    @ParameterizedTest(name = "orderStatus가 {0}일 때 예외가 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_메서드는_전달한_orderId의_상태가_COMPLETION이라면_예외가_발생한다(final String orderStatus) {
        // given
        final MenuGroup persistMenuGroup = menuGroupRepository.save(new MenuGroup("메뉴 그룹"));
        final Product persistProduct = productRepository.save(new Product("상품", BigDecimal.TEN));
        final MenuProduct persistMenuProduct = new MenuProduct(persistProduct, 1);
        final Menu persistMenu = menuRepository.save(Menu.of(
                "메뉴",
                BigDecimal.TEN,
                List.of(persistMenuProduct),
                persistMenuGroup)
        );
        final OrderTable persistOrderTable = orderTableRepository.save(new OrderTable(0, false));
        final OrderLineItem persistOrderLineItem = new OrderLineItem(persistMenu, 1L);
        final Order persistOrder = orderRepository.save(
                new Order(persistOrderTable, OrderStatus.COMPLETION, LocalDateTime.now().minusHours(3L), List.of(persistOrderLineItem))
        );
        final UpdateOrderStatusRequest invalidRequest = new UpdateOrderStatusRequest(orderStatus);

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(persistOrder.getId(), invalidRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
