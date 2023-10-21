package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;
    private Menu menu;

    @BeforeEach
    void init() {
        orderTable = orderTableDao.save(OrderTableFixture.create(false, 4));
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup("Leo's Pick"));
        Price price = new Price(BigDecimal.valueOf(1000));
        Product product = productRepository.save(new Product("치킨", price));

        menu = menuRepository.save(
                Menu.create("후라이드", price, savedMenuGroup, List.of(new MenuProduct(product.getId(), 1L)),
                        List.of(product)));
    }

    @Nested
    class 주문을_생성할_때 {

        @Test
        void success() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

            // when
            OrderResponse actual = orderService.create(orderRequest);

            // then
            assertThat(actual.getOrderLineItems()).hasSize(1);
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        }

        @Test
        void 주문_상품이_비어있으면_실패() {
            // given
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), Collections.emptyList());

            // when
            // then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴에_등록되지_않은_주문_상품이_있으면_실패() {
            // given
            Long inValidOrderLineItemId = 0L;
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(inValidOrderLineItemId, 1L);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 등록되지_않은_주문테이블이면_실패() {
            // given
            Long inValidOrderTableId = 0L;
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderRequest orderRequest = new OrderRequest(inValidOrderTableId, List.of(orderLineItemRequest));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_실패() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderTable emptyTable = orderTableDao.save(OrderTableFixture.create(true, 0));
            OrderRequest orderRequest = new OrderRequest(emptyTable.getId(), List.of(orderLineItemRequest));

            // when
            // then
            assertThatThrownBy(() -> orderService.create(orderRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 주문_목록_조회() {
        // given
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
        OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
        OrderResponse savedOrder = orderService.create(orderRequest);

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(List.of(savedOrder));
    }

    @Nested
    class 주문_상태를_변경할_때 {

        @Test
        void success() {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
            OrderResponse savedOrder = orderService.create(orderRequest);

            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL);

            // when
            OrderResponse result = orderService.changeOrderStatus(savedOrder.getId(), orderStatusRequest);

            // then
            assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
        }

        @Test
        void 등록되어있지_않은_주문이면_실패() {
            // given
            Long invalidId = 0L;
            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.COOKING);

            // when
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(invalidId, orderStatusRequest)).isInstanceOf(
                    IllegalArgumentException.class);

        }

        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void 주문_상태가_완료이면_실패(OrderStatus orderStatus) {
            // given
            OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1L);
            OrderRequest orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
            OrderResponse orderResponse = orderService.create(orderRequest);

            Long savedOrderId = orderResponse.getId();

            orderService.changeOrderStatus(savedOrderId, new OrderStatusRequest(OrderStatus.MEAL));
            orderService.changeOrderStatus(savedOrderId, new OrderStatusRequest(OrderStatus.COMPLETION));

            // when
            OrderStatusRequest orderStatusRequest = new OrderStatusRequest(orderStatus);
            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, orderStatusRequest)).isInstanceOf(
                    IllegalArgumentException.class);
        }

    }

}
