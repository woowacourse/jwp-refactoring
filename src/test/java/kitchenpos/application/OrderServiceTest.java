package kitchenpos.application;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.Product;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import kitchenpos.domain.order.service.OrderService;
import kitchenpos.domain.order.service.dto.OrderCreateRequest;
import kitchenpos.domain.order.service.dto.OrderLineItemCreateRequest;
import kitchenpos.domain.order.service.dto.OrderResponse;
import kitchenpos.domain.order.service.dto.OrderUpateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static kitchenpos.application.fixture.MenuFixture.menu;
import static kitchenpos.application.fixture.MenuGroupFixture.menuGroup;
import static kitchenpos.application.fixture.MenuProductFixture.menuProduct;
import static kitchenpos.application.fixture.OrderFixture.order;
import static kitchenpos.application.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.application.fixture.ProductFixture.product;
import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Menu noodle;

    private Menu potato;

    private OrderLineItem wooDong;

    private OrderLineItem frenchFries;

    @BeforeEach
    void setUp() {
        noodle = mock(Menu.class);
        potato = mock(Menu.class);
        wooDong = new OrderLineItem(null, noodle, 1);
        frenchFries = new OrderLineItem(null, potato, 1);
    }

    @Nested
    class Create {

        @Test
        void 주문을_생성할_수_있다() {
            // given
            final Menu menu = menu("우동세트", BigDecimal.valueOf(5_000), menuGroup("일식"), List.of());
            final MenuProduct menuProduct = menuProduct(menu, product("면", BigDecimal.valueOf(5_000)), 5000L);
            final MenuProducts menuProducts = new MenuProducts();
            menuProducts.addAll(List.of(menuProduct));
            menu.addMenuProducts(menuProducts);

            final Order order = spy(order(mock(OrderTable.class), COOKING, now(), new ArrayList<>()));
            final OrderLineItem orderLineItem = orderLineItem(order, menu, 1L);
            order.addAllOrderLineItems(OrderLineItems.from(List.of(orderLineItem)));
            given(orderLineItemRepository.findAllByMenuIds(anyList())).willReturn(List.of(orderLineItem));

            given(menuRepository.countByIdIn(anyList())).willReturn(1L);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(mock(OrderTable.class)));
            given(orderRepository.save(any(Order.class))).willReturn(order);

            final long savedId = 1L;
            given(order.getId()).willReturn(savedId);

            // when
            final List<OrderLineItemCreateRequest> orderLineItemCreateRequest = List.of(new OrderLineItemCreateRequest(1L, 2L));
            final OrderCreateRequest request = new OrderCreateRequest(1L, orderLineItemCreateRequest);
            final OrderResponse actual = orderService.create(request);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(order.getOrderStatus().name()),
                    () -> assertThat(actual.getOrderedTime()).isEqualTo(order.getOrderedTime()),
                    () -> assertThat(actual.getOrderTable()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(2)
            );
        }

        @Test
        void 주문_항목이_없으면_예외가_발생한다() {
            // given
            given(orderLineItemRepository.findAllByMenuIds(anyList())).willReturn(List.of());

            // when, then
            final long emptyMenuId = -1L;
            final OrderCreateRequest request = new OrderCreateRequest(1L, List.of(new OrderLineItemCreateRequest(emptyMenuId, 2L)));
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_항목의_총합과_메뉴의_총합이_다르면_예외를_발생한다() {
            // given
            final Order order = order(COMPLETION, now(), List.of(wooDong, frenchFries));
            final Product product = product("우동", BigDecimal.valueOf(3_000L));
            final Menu menu = menu("우동세트", BigDecimal.valueOf(8_000), menuGroup("일식"), List.of(menuProduct(product, 3_000L)));
            given(orderLineItemRepository.findAllByMenuIds(anyList())).willReturn(List.of(new OrderLineItem(order, menu, 1)));

            // when
            final long incorrectMenuSize = order.getOrderLineItems().getOrderLineItems().size() - 1;
            when(menuRepository.countByIdIn(anyList())).thenReturn(incorrectMenuSize);
            final long orderTableId = 1L;
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, List.of(new OrderLineItemCreateRequest(1L, 2L)));

            // then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문에_있는_주문_테이블이_없으면_예외가_발생한다() {
            // given
            final Menu menu = menu("우동세트", BigDecimal.valueOf(5_000), menuGroup("일식"), List.of());
            final MenuProduct menuProduct = menuProduct(menu, product("면", BigDecimal.valueOf(5_000)), 5000L);
            final MenuProducts menuProducts = new MenuProducts();
            menuProducts.addAll(List.of(menuProduct));
            menu.addMenuProducts(menuProducts);

            final Order order = spy(order(mock(OrderTable.class), COOKING, now(), new ArrayList<>()));
            final OrderLineItem orderLineItem = orderLineItem(order, menu, 1L);
            order.addAllOrderLineItems(OrderLineItems.from(List.of(orderLineItem)));
            given(orderLineItemRepository.findAllByMenuIds(anyList())).willReturn(List.of(orderLineItem));

            given(menuRepository.countByIdIn(anyList())).willReturn(1L);
            given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

            // then
            final long orderTableId = 1L;
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, List.of(new OrderLineItemCreateRequest(1L, 2L)));
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FindAll {

        @Test
        void 주문을_전체_조회_할_수_있다() {
            // when
            orderService.list();

            // then
            verify(orderRepository, only()).findAll();
        }
    }

    @Nested
    class ChangeOrderStatus {

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태를_변경할_수_있다(final OrderStatus validedOrderStatus) {
            // given
            final long orderId = 1;
            final List<OrderLineItem> orderLineItems = new ArrayList<>(List.of(wooDong, frenchFries));

            final Order order = order(validedOrderStatus, now(), orderLineItems);
            given(orderRepository.findById(orderId)).willReturn(Optional.ofNullable(order));

            // when
            final OrderUpateRequest request = new OrderUpateRequest(COOKING.name());

            // then
            assertThatCode(() -> orderService.changeOrderStatus(orderId, request))
                    .doesNotThrowAnyException();
        }

        @Test
        void 주문_상태가_완료_상태라면_상태를_변경할_수_없다() {
            // given
            final long orderId = 1;
            final List<OrderLineItem> orderLineItems = List.of(wooDong, frenchFries);

            final Order expected = order(COMPLETION, now(), orderLineItems);
            final Order actual = spy(order(expected.getOrderStatus(), expected.getOrderedTime(), new ArrayList<>()));
            given(orderRepository.findById(orderId)).willReturn(Optional.ofNullable(actual));

            // when
            final OrderUpateRequest request = new OrderUpateRequest(COMPLETION.name());

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}

