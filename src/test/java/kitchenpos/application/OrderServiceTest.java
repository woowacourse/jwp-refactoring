package kitchenpos.application;

import static kitchenpos.application.OrderServiceTest.OrderRequestFixture.주문_생성_요청;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.domain.menu.MenuFixture.메뉴;
import static kitchenpos.domain.menu.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.domain.menu.MenuProductFixture.메뉴_상품;
import static kitchenpos.domain.order.OrderFixture.주문;
import static kitchenpos.domain.order.OrderLineItemFixture.주문_항목;
import static kitchenpos.domain.product.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTableFixture;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderCreateRequest;
import kitchenpos.dto.order.OrderLineItemCreateRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@ServiceTest
class OrderServiceTest {

    private static final BigDecimal PRICE = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    private OrderLineItem orderLineItem;
    private Long orderTableId;
    private Long emptyOrderTableId;

    @BeforeEach
    void setUp() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹()).getId();
        Product product = productRepository.save(상품(PRICE));

        MenuProduct menuProduct = 메뉴_상품(product);
        Menu menu = menuRepository.save(메뉴(menuGroupId, PRICE, List.of(menuProduct)));

        orderTableId = orderTableRepository.save(OrderTableFixture.단체_지정_없는_주문_테이블()).getId();
        emptyOrderTableId = orderTableRepository.save(OrderTableFixture.단체_지정_없는_빈_주문_테이블()).getId();
        orderLineItem = 주문_항목(menu.getId());
    }

    @Nested
    class 주문을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            // given
            OrderCreateRequest order = 주문_생성_요청(orderTableId, List.of(orderLineItem));

            // when
            OrderResponse createdOrder = orderService.create(order);

            // then
            assertSoftly(softly -> {
                softly.assertThat(createdOrder.getId()).isNotNull();
                softly.assertThat(createdOrder).usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems.seq", "orderLineItems.orderId")
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(OrderResponse.from(주문(orderTableId, COOKING, List.of(orderLineItem))));
            });
        }

        @Test
        void 주문을_생성할_때_주문_항목이_비었으면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(orderTableId, List.of());

            // expect
            assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_주문_항목_개수와_메뉴의_개수가_다르면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(orderTableId, List.of(orderLineItem, orderLineItem));

            // expect
            assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_존재하지_않는_주문_테이블이면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(Long.MIN_VALUE, List.of(orderLineItem));

            // expect
            assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_주문_테이블이_비었으면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(emptyOrderTableId, List.of(orderLineItem));

            // expect
            assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문을_조회한다() {
        // given
        OrderResponse createdOrder = orderService.create(주문_생성_요청(orderTableId, List.of(orderLineItem)));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(createdOrder));
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Long orderId = orderService.create(주문_생성_요청(orderTableId, List.of(orderLineItem))).getId();
        OrderStatusUpdateRequest request = new OrderStatusUpdateRequest(MEAL.name());

        // when
        OrderResponse order = orderService.changeOrderStatus(orderId, request);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(MEAL.name());
    }

    static class OrderRequestFixture {

        public static OrderCreateRequest 주문_생성_요청(Long orderTableId, List<OrderLineItem> orderLineItems) {
            List<OrderLineItemCreateRequest> items = orderLineItems.stream()
                    .map(OrderRequestFixture::주문_항목_생성_요청)
                    .collect(Collectors.toList());
            return new OrderCreateRequest(orderTableId, items);
        }

        public static OrderLineItemCreateRequest 주문_항목_생성_요청(OrderLineItem orderLineItem) {
            return new OrderLineItemCreateRequest(orderLineItem.getMenuId(), orderLineItem.getQuantityValue());
        }
    }
}
