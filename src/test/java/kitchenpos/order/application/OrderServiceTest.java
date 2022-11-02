package kitchenpos.order.application;

import static kitchenpos.DtoFixture.getEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getMenuCreateRequest;
import static kitchenpos.DtoFixture.getMenuGroupCreateRequest;
import static kitchenpos.DtoFixture.getNotEmptyTableCreateRequest;
import static kitchenpos.DtoFixture.getOrderCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.order.dto.OrderLineItemDto;
import kitchenpos.order.dto.request.OrderCreateRequest;
import kitchenpos.order.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.product.domain.OrderStatus;
import kitchenpos.table.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    private MenuResponse createMenu() {
        final MenuGroupResponse response = 메뉴_그룹_등록(getMenuGroupCreateRequest());
        return 메뉴_등록(getMenuCreateRequest(response.getId(), createMenuProductDtos()));
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        final MenuResponse menu = createMenu();
        final TableResponse orderTable = 테이블_등록(getNotEmptyTableCreateRequest(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());

        final OrderResponse savedOrder = 주문_등록(request);

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
        final TableResponse orderTable = 테이블_등록(getNotEmptyTableCreateRequest(5));
        final OrderCreateRequest request = new OrderCreateRequest(
                orderTable.getId(),
                List.of(new OrderLineItemDto(null, 1)));

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다. - 주문 테이블이 존재하지 않는다면 예외를 반환한다.")
    @Test
    void create_exception_noSuchTable() {
        final MenuResponse menu = createMenu();
        final OrderCreateRequest request = getOrderCreateRequest(null, menu.getId());

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 등록한다. - 주문 테이블이 빈 테이블이면 예외를 반환한다.")
    @Test
    void create_exception_tableIsEmpty() {
        final MenuResponse menu = createMenu();
        final TableResponse orderTable = 테이블_등록(getEmptyTableCreateRequest());
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());

        assertThatThrownBy(() -> 주문_등록(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회한다.")
    @Test
    void list() {
        final MenuResponse menu = createMenu();
        final TableResponse orderTable = 테이블_등록(getNotEmptyTableCreateRequest(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());
        주문_등록(request);

        final List<OrderResponse> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        final MenuResponse menu = createMenu();
        final TableResponse orderTable = 테이블_등록(getNotEmptyTableCreateRequest(5));
        final OrderCreateRequest orderCreateRequest = getOrderCreateRequest(orderTable.getId(), menu.getId());
        final OrderResponse savedOrder = 주문_등록(orderCreateRequest);

        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL);
        final OrderResponse changedOrder = orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest);

        assertAll(
                () -> assertThat(changedOrder.getId()).isEqualTo(savedOrder.getId()),
                () -> assertThat(changedOrder.getOrderTableId()).isEqualTo(savedOrder.getOrderTableId()),
                () -> assertThat(changedOrder.getOrderLineItems()).hasSize(1),
                () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(statusChangeRequest.getOrderStatus().name())
        );
    }

    @DisplayName("주문 상태를 변경한다. - 존재하지 않는 주문이면 예외를 반환한다.")
    @Test
    void changeOrderStatus_exception_noSuchOrder() {
        final OrderStatusChangeRequest statusChangeRequest = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, statusChangeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다. - 현재 상태가 COMPLETION이면 예외를 반환한다.")
    @Test
    void changeOrderStatus_statusIsCompletion() {
        final MenuResponse menu = createMenu();
        final TableResponse orderTable = 테이블_등록(getNotEmptyTableCreateRequest(5));
        final OrderCreateRequest request = getOrderCreateRequest(orderTable.getId(), menu.getId());
        final OrderResponse savedOrder = 주문_등록(request);

        final OrderStatusChangeRequest statusChangeRequest1 = new OrderStatusChangeRequest(
                OrderStatus.COMPLETION);
        orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest1);

        final OrderStatusChangeRequest statusChangeRequest2 = new OrderStatusChangeRequest(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), statusChangeRequest2))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
