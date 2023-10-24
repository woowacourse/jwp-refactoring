package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private OrderService orderService;

    private List<Menu> menus;
    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        MenuGroup menuGroup = new MenuGroup("menu group");
        menuGroup = testFixtureBuilder.buildMenuGroup(menuGroup);

        menus = new ArrayList<>();
        Menu menu1 = new Menu("name", new BigDecimal(100), menuGroup.getId(), Collections.emptyList());
        menus.add(testFixtureBuilder.buildMenu(menu1));
        Menu menu2 = new Menu("name", new BigDecimal(100), menuGroup.getId(), Collections.emptyList());
        menus.add(testFixtureBuilder.buildMenu(menu2));

        orderTable = new OrderTable(null, 3, false);
        orderTable = testFixtureBuilder.buildOrderTable(orderTable);
    }

    @DisplayName("주문 생성 테스트")
    @Nested
    class OrderCreateTest {

        @DisplayName("주문을 생성한다.")
        @Test
        void orderCreate() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), menus.stream().map(menu -> new OrderCreateRequest.OrderLineItemCreate(menu.getId(), 2L)).collect(Collectors.toList()));

            //when
            final Long id = orderService.create(request);

            //then
            assertSoftly(softly -> {
                softly.assertThat(id).isNotNull();
            });
        }

        @DisplayName("주문 항목이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderLineItemsIsEmpty() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문할 메뉴가 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistMenu() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), List.of(new OrderCreateRequest.OrderLineItemCreate(-1L, 2L)));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 존재하지 않으면 실패한다.")
        @Test
        void orderCreateFailWhenNotExistOrderTable() {
            //given
            final OrderCreateRequest request = new OrderCreateRequest(-1L, menus.stream().map(menu -> new OrderCreateRequest.OrderLineItemCreate(menu.getId(), 2L)).collect(Collectors.toList()));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 비어있으면 실패한다.")
        @Test
        void orderCreateFailWhenOrderTableIsEmpty() {
            //given
            orderTable = new OrderTable(null, 3, true);
            orderTable = testFixtureBuilder.buildOrderTable(orderTable);

            final OrderCreateRequest request = new OrderCreateRequest(orderTable.getId(), menus.stream().map(menu -> new OrderCreateRequest.OrderLineItemCreate(menu.getId(), 2L)).collect(Collectors.toList()));

            // when & then
            assertThatThrownBy(() -> orderService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("주문 조회 테스트")
    @Nested
    class OrderFindTest {

        @DisplayName("주문을 전체 조회한다.")
        @Test
        void orderFindAll() {
            //given
            Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            //when
            final List<OrderResponse> actual = orderService.list();

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual.size()).isEqualTo(1);
                softly.assertThat(actual.get(0).getId()).isEqualTo(orderId);
            });
        }
    }

    @DisplayName("주문 상태 변경 테스트")
    @Nested
    class OrderStatusChangeTest {

        @DisplayName("주문 상태를 변경한다.")
        @Test
        void orderStatusChange() {
            //given
            Order order = new Order(orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), Collections.emptyList());
            order = testFixtureBuilder.buildOrder(order);

            //when
            final Long actual = orderService.changeOrderStatus(order.getId(), OrderStatus.MEAL.name());

            //then
            final Long orderId = order.getId();
            assertSoftly(softly -> {
                softly.assertThat(actual).isEqualTo(orderId);
            });
        }

        @DisplayName("존재하지 않는 주문은 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenNotExistOrder() {
            //given
            final String changeStatus = OrderStatus.MEAL.name();

            // when & then
            assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 상태가 완료면 변경할 수 없다.")
        @Test
        void orderStatusChangeFailWhenStatusIsCompletion() {
            //given
            Order completionOrder = new Order(orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());
            completionOrder = testFixtureBuilder.buildOrder(completionOrder);

            final String changeStatus = OrderStatus.MEAL.name();

            // when & then
            final Long completionOrderId = completionOrder.getId();
            assertThatThrownBy(() -> orderService.changeOrderStatus(completionOrderId, changeStatus))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
