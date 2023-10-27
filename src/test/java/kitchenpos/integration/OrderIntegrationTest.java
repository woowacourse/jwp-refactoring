package kitchenpos.integration;

import static kitchenpos.integration.fixture.MenuAPIFixture.createDefaultMenu;
import static kitchenpos.integration.fixture.MenuGroupAPIFixture.createDefaultMenuGroup;
import static kitchenpos.integration.fixture.OrderAPIFixture.changeOrderStatus;
import static kitchenpos.integration.fixture.OrderAPIFixture.createOrderAndReturnResponse;
import static kitchenpos.integration.fixture.OrderAPIFixture.listOrder;
import static kitchenpos.integration.fixture.ProductAPIFixture.createDefaultProduct;
import static kitchenpos.integration.fixture.TableAPIFixture.createDefaultOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.integration.helper.InitIntegrationTest;
import kitchenpos.menu.application.dto.response.MenuGroupResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.application.dto.response.ProductResponse;
import kitchenpos.order.application.dto.request.OrderCreateRequest;
import kitchenpos.order.application.dto.request.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.order.application.dto.response.OrderResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class OrderIntegrationTest extends InitIntegrationTest {

    @Test
    @DisplayName("주문을 성공적으로 생성한다.")
    void testCreateSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        final MenuResponse menuResponse = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());
        final OrderTableResponse orderTableResponse = createDefaultOrderTable();

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getMenuGroupId(), 2L);

        final OrderCreateRequest request = new OrderCreateRequest(orderTableResponse.getId(), List.of(orderLineItemRequest));

        //when
        final OrderResponse response = createOrderAndReturnResponse(request);

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTableId()).isEqualTo(orderTableResponse.getId()),
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(response.getOrderedTime().toLocalDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getOrderLineItems()).isNotEmpty()
        );
    }

    @Test
    @DisplayName("주문 목록을 성공적으로 조회한다.")
    void testListSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        final MenuResponse menuResponse = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());
        final OrderTableResponse orderTableResponse = createDefaultOrderTable();

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getMenuGroupId(), 2L);

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableResponse.getId(), List.of(orderLineItemRequest));
        createOrderAndReturnResponse(orderCreateRequest);

        //when
        final List<OrderResponse> responses = listOrder();

        //then
        assertThat(responses).isNotEmpty();
        final OrderResponse response = responses.get(0);
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTableId()).isEqualTo(orderTableResponse.getId()),
                () -> assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(response.getOrderedTime().toLocalDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getOrderLineItems()).isNotEmpty()
        );
    }

    @Test
    @DisplayName("주문의 상태를 성공적으로 수정한다.")
    void testChangeOrderStatusSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        final MenuResponse menuResponse = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());
        final OrderTableResponse orderTableResponse = createDefaultOrderTable();

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getMenuGroupId(), 2L);

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableResponse.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderCreateResponse = createOrderAndReturnResponse(orderCreateRequest);

        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        //when
        final OrderResponse response = changeOrderStatus(orderCreateResponse.getId(), request);

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getOrderTableId()).isEqualTo(orderTableResponse.getId()),
                () -> assertThat(response.getOrderStatus()).isEqualTo(request.getOrderStatus()),
                () -> assertThat(response.getOrderedTime().toLocalDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getOrderLineItems()).isNotEmpty()
        );
    }
}
