package kitchenpos.application;

import static kitchenpos.table.domain.OrderStatus.COMPLETION;
import static kitchenpos.table.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.table.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.ui.dto.ChangeOrderStatusRequest;
import kitchenpos.order.ui.dto.OrderCreateRequest;
import kitchenpos.order.ui.dto.OrderLineItemRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("OrderService의")
class OrderServiceTest extends ServiceTest {

    @Nested
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("주문을 생성할 수 있다.")
        void create_validOrder_success() {
            // given
            final List<OrderLineItemRequest> orderLineItemRequests = getOrderLineItemsRequest();
            final Long orderTableId = saveOrderTable(10, false).getId();
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, orderLineItemRequests);

            // when
            final OrderResponse actual = orderService.create(request);

            // then
            softly.assertThat(actual.getOrderTableId()).isEqualTo(orderTableId);
            softly.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            softly.assertThat(actual.getOrderLineItems()).extracting("orderId", "menuId", "quantity")
                    .containsExactly(tuple(actual.getId(), orderLineItemRequests.get(0).getMenuId(), 2L));
            softly.assertAll();
        }

        @Test
        @DisplayName("주문 항목은 비어있거나 0개일 수 없다.")
        void create_orderLineItemLessThenOne_exception() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("각각의 주문 항목의 메뉴 ID는 서로 중복되지 않아야 한다.")
        void create_duplicateMenu_exception() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final List<OrderLineItemRequest> orderLineItemRequests = getOrderLineItemsRequest();
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(
                    orderLineItemRequests.get(0).getMenuId(), 5L);
            orderLineItemRequests.add(orderLineItemRequest);
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, orderLineItemRequests);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("모든 메뉴는 시스템에 등록된 상태여야 한다.")
        void create_notExistMenu_exception() {
            // given
            final Long orderTableId = saveOrderTable(10, false).getId();
            final List<OrderLineItemRequest> orderLineItemRequests = getOrderLineItemsRequest();
            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(999L, 5L);
            orderLineItemRequests.add(orderLineItemRequest);
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, orderLineItemRequests);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블은 시스템에 등록된 상태여야 한다.")
        void create_notExistOrderTable_exception() {
            // given
            final List<OrderLineItemRequest> orderLineItemRequests = getOrderLineItemsRequest();
            final OrderCreateRequest request = new OrderCreateRequest(999L, orderLineItemRequests);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("주문 테이블은 빈 테이블일 수 없다.")
        void create_emptyOrderTable_exception() {
            // given
            final Long orderTableId = saveOrderTable(10, true).getId();
            final List<OrderLineItemRequest> orderLineItemRequests = getOrderLineItemsRequest();
            final OrderCreateRequest request = new OrderCreateRequest(orderTableId, orderLineItemRequests);

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private List<OrderLineItemRequest> getOrderLineItemsRequest() {
            final MenuGroup menuGroup = saveMenuGroup("치킨");
            final Product product1 = saveProduct("후라이드치킨");
            final Product product2 = saveProduct("양념치킨");
            final Menu menu = saveMenu("치킨세트", BigDecimal.ONE, menuGroup,
                    new MenuProduct(product1.getId(), 1L),
                    new MenuProduct(product2.getId(), 2L));

            final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menu.getId(), 2L);
            final List<OrderLineItemRequest> requests = new ArrayList<>();
            requests.add(orderLineItemRequest);
            return requests;
        }
    }

    @Nested
    @DisplayName("list 메서드는")
    class ListTest {

        @Test
        @DisplayName("주문 목록을 조회한다.")
        void list_savedOrders_success() {
            // given
            final Product chicken1 = saveProduct("간장치킨");
            final Product chicken2 = saveProduct("앙념치킨");
            final MenuGroup chickenMenuGroup = saveMenuGroup("치킨");
            final Menu chickenMenu = saveMenu("반반치킨", BigDecimal.valueOf(10_000), chickenMenuGroup,
                    new MenuProduct(chicken1.getId(), 2L),
                    new MenuProduct(chicken2.getId(), 4L));

            final OrderTable orderTable1 = saveOrderTable(10, false);
            saveOrder(orderTable1, "COOKING", new OrderLineItem(2L, OrderMenu.from(chickenMenu)));

            final Product sushi1 = saveProduct("연어초밥");
            final Product sushi2 = saveProduct("광어초밥");
            final Product sushi3 = saveProduct("참치초밥");
            final MenuGroup sushiMenuGroup = saveMenuGroup("초밥");
            final Menu sushiMenu = saveMenu("모둠초밥", BigDecimal.valueOf(15_000), sushiMenuGroup,
                    new MenuProduct(sushi1.getId(), 3L),
                    new MenuProduct(sushi2.getId(), 2L),
                    new MenuProduct(sushi3.getId(), 1L));

            final OrderTable orderTable2 = saveOrderTable(2, false);
            saveOrder(orderTable2, "MEAL", new OrderLineItem(3L, OrderMenu.from(sushiMenu)));

            // when
            final List<OrderResponse> actual = orderService.list();

            // then
            assertThat(actual).extracting(OrderResponse::getOrderTableId, OrderResponse::getOrderStatus)
                    .contains(
                            tuple(orderTable1.getId(), OrderStatus.COOKING.name()),
                            tuple(orderTable2.getId(), MEAL.name())
                    );
        }
    }

    @Nested
    @DisplayName("changeOrderStatus 메서드는")
    class ChangeOrderStatus {

        @ParameterizedTest(name = "{0} -> {1}")
        @DisplayName("주문 상태를 변경할 수 있다.")
        @CsvSource(value = {"COOKING,COMPLETION", "MEAL,COMPLETION", "MEAL,COOKING"})
        void changeOrderStatus_validOrderStatus_success(final String sourceOrderStatus,
                                                        final String targetOrderStatus) {
            // given
            final OrderMenu orderMenu = getOrderMenu();
            final OrderTable orderTable = saveOrderTable(10, false);
            final Long orderId = saveOrder(orderTable, sourceOrderStatus, new OrderLineItem( 2L, orderMenu)).getId();

            final ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(targetOrderStatus);

            // when
            final OrderResponse actual = orderService.changeOrderStatus(orderId, request);

            // then
            assertThat(actual.getOrderStatus()).isEqualTo(targetOrderStatus);
        }

        @Test
        @DisplayName("주문이 존재하지 않으면 주문 상태를 변경할 수 없다.")
        void changeOrderStatus_notExistOrder_exception() {
            // given
            final ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(COMPLETION.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(999L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("계산 완료인 주문은 주문 상태를 변경할 수 없다.")
        void changeOrderStatus_orderStatusIsCompletion_exception() {
            // given
            final OrderMenu orderMenu = getOrderMenu();
            final OrderTable orderTable = saveOrderTable(10, false);
            final Long orderId = saveOrder(orderTable, COMPLETION.name(), new OrderLineItem(2L, orderMenu)).getId();

            final ChangeOrderStatusRequest request = new ChangeOrderStatusRequest(MEAL.name());

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private OrderMenu getOrderMenu() {
            final Product chicken1 = saveProduct("간장치킨");
            final Product chicken2 = saveProduct("앙념치킨");
            final MenuGroup chickenMenuGroup = saveMenuGroup("치킨");
            final Menu menu = saveMenu("반반치킨", BigDecimal.valueOf(10_000), chickenMenuGroup,
                    new MenuProduct(chicken1.getId(), 2L),
                    new MenuProduct(chicken2.getId(), 4L));
            return OrderMenu.from(menu);
        }
    }
}
