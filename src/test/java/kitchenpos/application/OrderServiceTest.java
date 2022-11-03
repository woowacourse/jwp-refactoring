package kitchenpos.application;

import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.OrderTableFixture.비어있는_주문_테이블;
import static kitchenpos.support.OrderTableFixture.비어있지_않은_주문_테이블;
import static kitchenpos.support.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.dto.OrderRequestDto;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.UpdateOrderStatusRequestDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.presentation.dto.OrderLineItemRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.table.application.dto.OrderTableRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    MenuResponse savedMenu;

    @BeforeEach
    void setUp() {
        final ProductResponse savedProduct = 상품_등록(상품);
        final MenuGroup savedMenuGroup = 메뉴_그룹_등록(메뉴_그룹);
        savedMenu = 메뉴_등록(메뉴_생성("메뉴이름", BigDecimal.valueOf(9000), savedMenuGroup.getId(), savedProduct));
    }

    @Nested
    @DisplayName("주문 생성 로직 테스트")
    class create {


        @Test
        @DisplayName("주문을 생성한다.")
        void create() {
            final OrderTableRequestDto orderTableRequestDto = 비어있지_않은_주문_테이블;
            final Long tableId = tableService.create(orderTableRequestDto).getId();
            final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId,
                    List.of(new OrderLineItemRequest(savedMenu.getId(), 1L)));

            final OrderResponse actual = orderService.create(orderRequestDto);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(tableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                    () -> assertThat(actual.getOrderedTime()).isNotNull(),
                    () -> assertThat(actual.getOrderLineItems()).hasSize(1)
            );
        }

        @Test
        @DisplayName("orderLineItems가 비어있는데 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_emptyOrderLineItems() {
            final OrderTableRequestDto orderTableRequestDto = 비어있는_주문_테이블;
            final Long tableId = tableService.create(orderTableRequestDto).getId();
            final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId, List.of());

            assertThatThrownBy(() -> orderService.create(orderRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("존재하지 않는 메뉴로 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_notExistMenuId() {
            final OrderTableRequestDto orderTableRequestDto = 비어있지_않은_주문_테이블;
            final Long tableId = tableService.create(orderTableRequestDto).getId();
            final Long notExistMenuId = 999999L;
            final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId,
                    List.of(new OrderLineItemRequest(notExistMenuId, 1L)));

            assertThatThrownBy(() -> orderService.create(orderRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("존재하지 않는 테이블에 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_notExistTable() {
            final Long notExistTableId = 999999L;
            final OrderRequestDto orderRequestDto = new OrderRequestDto(notExistTableId,
                    List.of(new OrderLineItemRequest(savedMenu.getId(), 1L)));

            assertThatThrownBy(() -> orderService.create(orderRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블이 비어있는데 주문을 생성하려는 경우 예외를 발생시킨다.")
        void create_emptyTable() {
            final OrderTableRequestDto orderTableRequestDto = 비어있는_주문_테이블;
            final Long tableId = tableService.create(orderTableRequestDto).getId();
            final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId,
                    List.of(new OrderLineItemRequest(savedMenu.getId(), 1L)));

            assertThatThrownBy(() -> orderService.create(orderRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    @DisplayName("주문 내역을 반환한다.")
    void list() {
        final OrderTableRequestDto orderTableRequestDto = 비어있지_않은_주문_테이블;
        final Long tableId = tableService.create(orderTableRequestDto).getId();
        final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId,
                List.of(new OrderLineItemRequest(savedMenu.getId(), 1L)));

        orderService.create(orderRequestDto);
        orderService.create(orderRequestDto);

        final List<Order> actual = orderService.list();

        assertThat(actual).hasSizeGreaterThan(1);
    }

    @Nested
    @DisplayName("주문 상태 변경 테스트")
    class changeOrderStatus {

        @Test
        @DisplayName("주문 상태를 변경한다.")
        void changeOrderStatus() {
            final OrderTableRequestDto orderTableRequestDto = 비어있지_않은_주문_테이블;
            final Long tableId = tableService.create(orderTableRequestDto).getId();
            final OrderRequestDto orderRequestDto = new OrderRequestDto(tableId,
                    List.of(new OrderLineItemRequest(1L, 1L)));
            final Long orderId = orderService.create(orderRequestDto).getId();

            final UpdateOrderStatusRequestDto updateOrderStatusRequestDto =
                    new UpdateOrderStatusRequestDto(OrderStatus.MEAL.name());

            final OrderResponse actual = orderService.changeOrderStatus(orderId, updateOrderStatusRequestDto);

            assertAll(
                    () -> assertThat(actual.getOrderTableId()).isEqualTo(tableId),
                    () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name())
            );
        }

        @Test
        @DisplayName("존재하지 않는 주문의 상태를 변경하려는 경우 예외를 발생 시킨다.")
        void changeOrderStatus_notExistOrder() {
            tableService.create(비어있지_않은_주문_테이블);
            final Long notExistOrderId = 999999L;

            final UpdateOrderStatusRequestDto updateOrderStatusRequestDto =
                    new UpdateOrderStatusRequestDto(OrderStatus.MEAL.name());

            assertThatThrownBy(() -> orderService.changeOrderStatus(notExistOrderId, updateOrderStatusRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
