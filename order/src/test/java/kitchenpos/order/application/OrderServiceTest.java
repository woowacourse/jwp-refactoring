package kitchenpos.order.application;

import static kitchenpos.menu.domain.MenuFixture.메뉴;
import static kitchenpos.menu.domain.MenuProductFixture.메뉴_상품;
import static kitchenpos.menugroup.domain.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.order.application.OrderServiceTest.OrderRequestFixture.주문_생성_요청;
import static kitchenpos.order.domain.OrderFixture.주문;
import static kitchenpos.order.domain.OrderLineItemFixture.주문_항목;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.product.domain.ProductFixture.상품;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_빈_주문_테이블;
import static kitchenpos.table.domain.OrderTableFixture.단체_지정_없는_채워진_주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.vo.ProductSpecification;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusUpdateRequest;
import kitchenpos.order.vo.MenuSpecification;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Sql(value = "/test_truncate_table.sql", executionPhase = BEFORE_TEST_METHOD)
@SpringBootTest
class OrderServiceTest {

    private static final BigDecimal PRICE = BigDecimal.valueOf(1).setScale(2, RoundingMode.HALF_UP);

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderLineItem orderLineItem;
    private OrderTable orderTable;
    private Long emptyOrderTableId;

    @BeforeEach
    void setUp() {
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹()).getId();
        Product product = productRepository.save(상품(PRICE));

        ProductSpecification productSpec = ProductSpecification.from(product.getName(), product.getPriceValue());
        MenuProduct menuProduct = 메뉴_상품(product.getId(), 1L, productSpec);
        Menu menu = menuRepository.save(메뉴(menuGroupId, PRICE, List.of(menuProduct)));

        orderTable = orderTableRepository.save(단체_지정_없는_채워진_주문_테이블());
        emptyOrderTableId = orderTableRepository.save(단체_지정_없는_빈_주문_테이블()).getId();
        orderLineItem = 주문_항목(menu.getId(), new MenuSpecification(menu.getName(), menu.getPriceValue()));
    }

    @Nested
    class 주문을_생성할_때 {

        @Test
        void 정상적으로_생성한다() {
            // given
            OrderCreateRequest order = 주문_생성_요청(orderTable.getId(), List.of(orderLineItem));

            // when
            OrderResponse createdOrder = orderService.create(order);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(createdOrder.getId()).isNotNull();
                softly.assertThat(createdOrder).usingRecursiveComparison()
                        .ignoringFields("id", "orderLineItems.seq", "orderLineItems.orderId")
                        .ignoringFieldsOfTypes(LocalDateTime.class)
                        .isEqualTo(OrderResponse.from(주문(orderTable.getId(), COOKING, List.of(orderLineItem))));
            });
        }

        @Test
        void 주문을_생성할_때_주문_항목이_비었으면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(orderTable.getId(), List.of());

            // expect
            Assertions.assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_주문_항목_개수와_메뉴의_개수가_다르면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(orderTable.getId(), List.of(orderLineItem, orderLineItem));

            // expect
            Assertions.assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_존재하지_않는_주문_테이블이면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(Long.MIN_VALUE, List.of(orderLineItem));

            // expect
            Assertions.assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문을_생성할_때_주문_테이블이_비었으면_예외를_던진다() {
            // given
            OrderCreateRequest invalidOrder = 주문_생성_요청(emptyOrderTableId, List.of(orderLineItem));

            // expect
            Assertions.assertThatThrownBy(() -> orderService.create(invalidOrder))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 주문을_조회한다() {
        // given
        OrderResponse createdOrder = orderService.create(주문_생성_요청(orderTable.getId(), List.of(orderLineItem)));

        // when
        List<OrderResponse> orders = orderService.list();

        // then
        Assertions.assertThat(orders).usingRecursiveComparison()
                .isEqualTo(List.of(createdOrder));
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Long orderId = orderService.create(주문_생성_요청(orderTable.getId(), List.of(orderLineItem))).getId();
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
