package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.domain.order.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.ChangeOrderStatusRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderTableRequest;

class OrderServiceTest extends BaseServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private OrderRequest orderRequest;
    private OrderLineItemRequest orderLineItemRequest;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        final MenuGroup boonsik = menuGroupRepository.save(new MenuGroup("분식"));
        final Product productD = productRepository.save(new Product("떡볶이", BigDecimal.TEN));

        final List<MenuProduct> menuProducts = List.of(new MenuProduct(productD.getId(), 2));

        final Menu menu = menuRepository.save(
                new Menu("떡순튀", BigDecimal.valueOf(20), boonsik.getId(), new MenuProducts(menuProducts)));
        orderTable = tableService.create(new OrderTableRequest(4, false));
        orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 1);
        orderRequest = new OrderRequest(orderTable.getId(), List.of(orderLineItemRequest));
    }


    @Nested
    @DisplayName("주문 생성 테스트")
    class OrderCreatedTest {
        @Test
        @DisplayName("주문 생성 - 정상")
        void createOrderWithValidData() {
            //given & when
            Order createdOrder = orderService.create(orderRequest);

            assertSoftly(softly -> {
                softly.assertThat(createdOrder.getOrderStatus()).isEqualTo(COOKING);
                softly.assertThat(createdOrder.getOrderLineItems().getOrderLineItems().size())
                        .isEqualTo(orderRequest.getOrderLineItemRequests().size());
                softly.assertThat(createdOrder.getOrderedTime()).isNotNull();
                softly.assertThat(createdOrder.getId()).isNotNull();
            });
        }

        @Test
        @DisplayName("주문 항목이 비어있을 때 예외 발생")
        void createOrderWithEmptyLineItemsShouldThrowException() {
            //given
            final OrderRequest emptyLineItemsRequest = new OrderRequest(orderTable.getId(), List.of());

            assertThatThrownBy(() -> orderService.create(emptyLineItemsRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("주문 항목이 비었어요");
        }

        @Test
        @DisplayName("메뉴 ID가 존재하지 않을 때 예외 발생")
        void createOrderWithNonExistentMenuShouldThrowException() {
            final OrderLineItemRequest unExistedOrderLineItem = new OrderLineItemRequest(-1L, 2);
            final OrderRequest unExistedMenuOrderRequest = new OrderRequest(orderTable.getId(),
                    List.of(unExistedOrderLineItem));

            assertThatThrownBy(() -> orderService.create(unExistedMenuOrderRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("없는 메뉴에요");
        }

        @Test
        @DisplayName("테이블이 비어있을 때 예외 발생")
        void createOrderWithEmptyTableShouldThrowException() {
            // Given
            final OrderTable emptyTable = tableService.create(new OrderTableRequest(4, true));
            final OrderRequest orderRequestWithEmptyTable = new OrderRequest(emptyTable.getId(),
                    List.of(orderLineItemRequest));

            // when & then
            assertThatThrownBy(() -> orderService.create(orderRequestWithEmptyTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class ChangedOrderStatusTest {

        @Test
        @DisplayName("주문의 상태를 변경한다.")
        void changeOrderStatus() {
            //given
            final Order savedOrder = orderService.create(orderRequest);
            final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(MEAL);

            //when
            final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest);

            //then
            assertSoftly(softAssertions -> {
                assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId());
                assertThat(changedOrder.getOrderStatus()).isEqualTo(MEAL);
            });
        }

        @Test
        @DisplayName("주문의 상태가 COMPLETION 인 경우 예외가 발생한다.")
        void changedOrderStatusWithComplication() {
            //given
            final Order savedOrder = orderService.create(orderRequest);
            final ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(COMPLETION);
            final Order chagedOrder = orderService.changeOrderStatus(savedOrder.getId(), changeOrderStatusRequest);

            final ChangeOrderStatusRequest changeOrderStatusRequest1 = new ChangeOrderStatusRequest(MEAL);

            //when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(chagedOrder.getId(), changeOrderStatusRequest1))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("더 이상 상태를 변경할 수 없습니다");
        }
    }
}
