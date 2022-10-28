package kitchenpos.application;

import static kitchenpos.DomainFixture.getEmptyTable;
import static kitchenpos.DomainFixture.getMenuGroup;
import static kitchenpos.DomainFixture.getNotEmptyTable;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.OrderCreateRequest;
import kitchenpos.ui.request.OrderLineItemDto;
import kitchenpos.ui.request.OrderStatusChangeRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    private Menu createMenu() {
        final MenuGroup menuGroup = 메뉴_그룹_등록(getMenuGroup());
        return 메뉴_등록(getMenuCreateRequest(menuGroup.getId(), createMenuProductDtos()));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        final Menu menu = createMenu();
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());

        final Order savedOrder = 주문_등록(request);

        assertAll(
                () -> assertThat(savedOrder.getId()).isNotNull(),
                () -> assertThat(savedOrder.getOrderTableId()).isEqualTo(orderTable.getId()),
                () -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
        );
    }

    @DisplayName("주문을 등록한다. - 존재하지 않는 메뉴가 포함되어 있으면 예외를 반환한다.")
    @Test
    void create_exception_noSuchMenu() {
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(5));
        final OrderCreateRequest request = new OrderCreateRequest(
                orderTable.getId(),
                List.of(new OrderLineItemDto(null, 1)));

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다. - 주문 테이블이 존재하지 않는다면 예외를 반환한다.")
    @Test
    void create_exception_noSuchTable() {
        final Menu menu = createMenu();
        final OrderCreateRequest request = getOrderCreateRequest(null, menu.getId());

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다. - 주문 테이블이 빈 테이블이면 예외를 반환한다.")
    @Test
    void create_exception_tableIsEmpty() {
        final Menu menu = createMenu();
        final OrderTable orderTable = 테이블_등록(getEmptyTable());
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        final Menu menu = createMenu();
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());
        주문_등록(request);

        final List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        final Menu menu = createMenu();
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(5));
        final OrderCreateRequest orderCreateRequest = getOrderCreateRequest(orderTable.getId(), menu.getId());
        final Order savedOrder = 주문_등록(orderCreateRequest);

        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());
        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest);

        assertAll(
                () -> assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(changedOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
                () -> assertThat(changedOrder.getOrderLineItems()).hasSize(1),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(statusChangeRequest.getOrderStatus())
        );
    }

    @DisplayName("주문 상태를 변경한다. - 존재하지 않는 주문이면 예외를 반환한다.")
    @Test
    void changeOrderStatus_exception_noSuchOrder() {
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(null, statusChangeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다. - 현재 상태가 COMPLETION이면 예외를 반환한다.")
    @Test
    void changeOrderStatus_statusIsCompletion() {
        final Menu menu = createMenu();
        final OrderTable orderTable = 테이블_등록(getNotEmptyTable(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());
        final Order savedOrder = 주문_등록(request);

        final OrderStatusChangeRequest statusChangeRequest1 = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION.name());
        orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest1);

        final OrderStatusChangeRequest statusChangeRequest2 = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
