package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.supports.IntegrationTest;
import kitchenpos.supports.MenuFixture;
import kitchenpos.supports.MenuGroupFixture;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("주문 서비스 테스트")
@IntegrationTest
class OrderServiceTest {

    private static final long INVALID_ID = -1L;

    @Autowired
    private OrderService orderService;
    @Autowired
    private TableService tableService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private ProductService productService;

    private MenuResponse setUpMenu() {
        final ProductResponse product = productService.create(ProductFixture.create());
        final MenuGroupResponse menuGroup = menuGroupService.create(MenuGroupFixture.create());

        final MenuRequest request = MenuFixture.of(menuGroup.getId(), List.of(product));
        return menuService.create(request);
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void findAllOrders() {
        // given
        final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
        final Order order = orderService.create(OrderFixture.of(setUpMenu().getId(), orderTable.getId()));

        // when
        final List<Order> list = orderService.list();

        // then
        assertSoftly(softly -> {
            assertThat(list).hasSize(1);
            assertThat(list.get(0))
                    .usingRecursiveComparison()
                    .isEqualTo(order);
        });
    }

    @Nested
    @DisplayName("주문을 생성할 때")
    class Create {

        @DisplayName("조리 상태로 생성된다")
        @Test
        void success() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final Order order = OrderFixture.of(setUpMenu().getId(), orderTable.getId());

            // when
            final Order savedOrder = orderService.create(order);

            // then
            assertSoftly(softly -> {
                assertThat(savedOrder.getId()).isPositive();
                assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");
            });
        }

        @DisplayName("주문할 메뉴와 수량 목록이 비어있으면 예외처리 한다")
        @Test
        void throwExceptionWhenOrderLineItemEmpty() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());

            final Order order = new Order();
            order.setOrderTableId(orderTable.getId());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴를 주문하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidMenu() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final Order order = OrderFixture.of(INVALID_ID, orderTable.getId());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 주문 테이블로 주문하면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrderTable() {
            // given
            final Order order = OrderFixture.of(setUpMenu().getId(), INVALID_ID);

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블이 빈 테이블이면 예외처리 한다")
        @Test
        void throwExceptionWhenEmptyTable() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createEmpty());
            final Order order = OrderFixture.of(setUpMenu().getId(), orderTable.getId());

            // then
            assertThatThrownBy(() -> orderService.create(order))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("주문 상태를 변경할 때")
    class ChangeOrderStatus {

        @DisplayName("주문 상태는 조리 ➜ 식사 ➜ 계산완료 순서로 변경된다")
        @Test
        void success() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final Order order = OrderFixture.of(setUpMenu().getId(), orderTable.getId());

            // when, then (조리)
            final Order savedOrder = orderService.create(order);
            assertThat(savedOrder.getOrderStatus()).isEqualTo("COOKING");

            // when, then (식사)
            final Order meal = OrderFixture.createMeal();
            final Order savedOrder2 = orderService.changeOrderStatus(savedOrder.getId(), meal);
            assertThat(savedOrder2.getOrderStatus()).isEqualTo("MEAL");

            // when, then (계산완료)
            final Order completion = OrderFixture.createCompletion();
            final Order savedOrder3 = orderService.changeOrderStatus(savedOrder2.getId(), completion);
            assertThat(savedOrder3.getOrderStatus()).isEqualTo("COMPLETION");
        }

        @DisplayName("존재하지 않는 주문이면 예외처리 한다")
        @Test
        void throwExceptionWhenInvalidOrder() {
            // given
            final Order change = OrderFixture.createMeal();

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(INVALID_ID, change))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("계산 완료 상태를 변경 하려하면 예외처리 한다")
        @Test
        void throwExceptionWhenChangeCompletionStatus() {
            // given
            final OrderTable orderTable = tableService.create(OrderTableFixture.createNotEmpty());
            final Order order = OrderFixture.of(setUpMenu().getId(), orderTable.getId());
            final Order savedOrder = orderService.create(order);
            final Long savedOrderId = savedOrder.getId();

            final Order completion = OrderFixture.createCompletion();
            orderService.changeOrderStatus(savedOrderId, completion); // 계산 완료 상태로 변경

            // then
            assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrderId, completion))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
