package kitchenpos.order.apllication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.appllication.MenuService;
import kitchenpos.menu.dto.request.MenuCreateRequest;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.request.MenuGroupCreateRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderLineItemCreateRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.request.OrderTableCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @Autowired
    TableService tableService;

    @Autowired
    MenuService menuService;

    @Autowired
    MenuGroupService menuGroupService;

    @Autowired
    OrderRepository orderRepository;

    @Nested
    @DisplayName("주문을 등록할 때,")
    class CreateOrder {
        @Test
        @DisplayName("정상 등록된다.")
        void create() {
            // given
            final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
            final Long orderTableId = tableService.create(orderTableCreateRequest);

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableId,
                    List.of(orderLineItemCreateRequest)
            );

            // when
            final Long orderId = orderService.create(orderCreateRequest);

            // then
            assertThat(orderId).isPositive();
        }

        @Test
        @DisplayName("주문 메뉴 목록이 비어있을 시 예외 발생")
        void orderLineItemsEmptyException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
            final Long orderTableId = tableService.create(orderTableCreateRequest);

            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableId, Collections.emptyList());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(orderCreateRequest));
        }

        @Test
        @DisplayName("주문 테이블이 존재하지 않을 시 예외 발생")
        void orderTableNotExistException() {
            // given
            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    -1,
                    List.of(orderLineItemCreateRequest)
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(orderCreateRequest));
        }

        @Test
        @DisplayName("주문 테이블이 비어있을 시 예외 발생")
        void orderTableEmptyException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, true);
            final Long orderTableId = tableService.create(orderTableCreateRequest);

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableId,
                    List.of(orderLineItemCreateRequest)
            );

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.create(orderCreateRequest));
        }
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다")
    void getOrders() {
        // given
        final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
        final Long orderTableId = tableService.create(orderTableCreateRequest);

        final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                orderTableId,
                List.of(orderLineItemCreateRequest)
        );

        final Long orderId = orderService.create(orderCreateRequest);

        // when
        final List<OrderResponse> responses = orderService.list();

        // then
        final OrderResponse orderResponse = responses.get(0);
        assertSoftly(softly -> {
            softly.assertThat(orderResponse.getId()).isEqualTo(orderId);
            softly.assertThat(orderResponse.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softly.assertThat(orderResponse.getOrderTable().getId()).isEqualTo(orderTableId);
            softly.assertThat(orderResponse.getOrderLineItems()).hasSize(1);
        });
    }

    @Nested
    @DisplayName("주문 상태를 변경할 때,")
    class ChangeOrderStatus {
        @Test
        @DisplayName("정상 변경된다")
        void changeOrderStatus() {
            // given
            final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest("menuGroupName");
            final Long menuGroupId = menuGroupService.create(menuGroupCreateRequest);

            final String name = "name";
            final BigDecimal price = BigDecimal.ZERO;
            final MenuCreateRequest menuCreateRequest = new MenuCreateRequest(
                    name,
                    price,
                    menuGroupId,
                    Collections.emptyList()
            );

            final Long menuId = menuService.create(menuCreateRequest);

            final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
            final Long orderTableId = tableService.create(orderTableCreateRequest);

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(menuId, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableId,
                    List.of(orderLineItemCreateRequest)
            );

            final Long orderId = orderService.create(orderCreateRequest);
            final OrderStatus orderStatus = OrderStatus.COMPLETION;

            // when
            final OrderResponse orderResponse = orderService.changeOrderStatus(orderId, orderStatus);

            // then
            assertThat(orderResponse.getOrderStatus()).isEqualTo(orderStatus);
        }

        @Test
        @DisplayName("현재 주문이 존재하지 않을 시 예외 처리")
        void orderNotExistException() {
            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(-1L, OrderStatus.COMPLETION));
        }

        @Test
        @DisplayName("현재 주문 상태가 완료 상태일 시 예외 처리")
        void orderStatusCompletionException() {
            // given
            final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(2, false);
            final Long orderTableId = tableService.create(orderTableCreateRequest);

            final OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(1L, 2);
            final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                    orderTableId,
                    List.of(orderLineItemCreateRequest)
            );

            final Long orderId = orderService.create(orderCreateRequest);
            final Order savedOrder = orderRepository.findById(orderId).get();
            savedOrder.changeOrderStatus(OrderStatus.COMPLETION);
            orderRepository.save(savedOrder);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> orderService.changeOrderStatus(orderId, OrderStatus.COMPLETION));
        }
    }
}
