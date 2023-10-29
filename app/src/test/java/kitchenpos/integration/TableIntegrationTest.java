package kitchenpos.integration;

import static kitchenpos.integration.fixture.MenuAPIFixture.createDefaultMenu;
import static kitchenpos.integration.fixture.MenuGroupAPIFixture.createDefaultMenuGroup;
import static kitchenpos.integration.fixture.OrderAPIFixture.changeOrderStatus;
import static kitchenpos.integration.fixture.OrderAPIFixture.createOrderAndReturnResponse;
import static kitchenpos.integration.fixture.ProductAPIFixture.createDefaultProduct;
import static kitchenpos.integration.fixture.TableAPIFixture.DEFAULT_ORDER_TABLE_EMPTY;
import static kitchenpos.integration.fixture.TableAPIFixture.DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS;
import static kitchenpos.integration.fixture.TableAPIFixture.changeOrderEmpty;
import static kitchenpos.integration.fixture.TableAPIFixture.changeOrderNumberOfGuests;
import static kitchenpos.integration.fixture.TableAPIFixture.createDefaultOrderTable;
import static kitchenpos.integration.fixture.TableAPIFixture.createOrderTableAndReturnResponse;
import static kitchenpos.integration.fixture.TableAPIFixture.listOrderTables;
import static kitchenpos.integration.fixture.TableGroupAPIFixture.createTableGroupAndReturnResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
import kitchenpos.application.dto.request.OrderTableChangeEmptyRequest;
import kitchenpos.application.dto.request.OrderTableChangeNumberOfGuestRequest;
import kitchenpos.application.dto.request.OrderTableCreateRequest;
import kitchenpos.application.dto.request.TableGroupCreateOrderTableRequest;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.OrderResponse;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.integration.helper.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

class TableIntegrationTest extends InitIntegrationTest {

    @Test
    @DisplayName("주문 테이블을 성공적으로 생성한다.")
    void testCreateOrderTableSuccess() {
        //given
        //when
        final OrderTableResponse response = createDefaultOrderTable();

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS),
                () -> assertThat(response.isEmpty()).isEqualTo(DEFAULT_ORDER_TABLE_EMPTY)
        );
    }

    @Test
    @DisplayName("주문 테이블 조회를 성공한다.")
    void testListOrderTablesSuccess() {
        //given
        createDefaultOrderTable();

        //when
        final List<OrderTableResponse> responses = listOrderTables();

        //then
        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses.get(0).getNumberOfGuests()).isEqualTo(DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS),
                () -> assertThat(responses.get(0).isEmpty()).isEqualTo(DEFAULT_ORDER_TABLE_EMPTY)
        );
    }

    @Test
    @DisplayName("주문 테이블의 상태를 빈 상태로 만드는데 성공한다.")
    void testChangeEmptySuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        final MenuResponse menuResponse = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());

        final OrderTableResponse orderTableResponse = createDefaultOrderTable();

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getMenuGroupId(), 2L);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableResponse.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderCreateResponse = createOrderAndReturnResponse(orderCreateRequest);

        final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());
        changeOrderStatus(orderCreateResponse.getId(), orderStatusChangeRequest);

        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest(true);

        //when
        final OrderTableResponse response = changeOrderEmpty(orderTableResponse.getId(), orderTableChangeEmptyRequest);

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(orderTableResponse.getId()),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(DEFAULT_ORDER_TABLE_NUMBER_OF_GUESTS),
                () -> assertThat(response.isEmpty()).isEqualTo(orderTableChangeEmptyRequest.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블의 인원을 변경하는데 성공한다.")
    void testChangeNumberOfGuestsSuccess() {
        //given
        final ProductResponse productResponse = createDefaultProduct();
        final MenuGroupResponse menuGroupResponse = createDefaultMenuGroup();
        final MenuResponse menuResponse = createDefaultMenu(productResponse.getId(), productResponse.getPrice(), menuGroupResponse.getId());

        final OrderTableResponse orderTableResponse1 = createDefaultOrderTable();
        final OrderTableResponse orderTableResponse2 = createOrderTableAndReturnResponse(new OrderTableCreateRequest(5, false));
        final List<TableGroupCreateOrderTableRequest> tableGroupCreateOrderTableRequests = List.of(
                new TableGroupCreateOrderTableRequest(orderTableResponse1.getId()),
                new TableGroupCreateOrderTableRequest(orderTableResponse2.getId())
        );
        final TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(tableGroupCreateOrderTableRequests);
        createTableGroupAndReturnResponse(tableGroupCreateRequest);

        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuResponse.getMenuGroupId(), 2L);
        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTableResponse1.getId(), List.of(orderLineItemRequest));
        final OrderResponse orderCreateResponse = createOrderAndReturnResponse(orderCreateRequest);

        final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest(OrderStatus.COMPLETION.name());
        changeOrderStatus(orderCreateResponse.getId(), orderStatusChangeRequest);

        final OrderTableChangeNumberOfGuestRequest orderTableChangeNumberOfGuestRequest = new OrderTableChangeNumberOfGuestRequest(10);

        //when
        final OrderTableResponse response = changeOrderNumberOfGuests(orderTableResponse1.getId(), orderTableChangeNumberOfGuestRequest);

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(orderTableResponse1.getId()),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(orderTableChangeNumberOfGuestRequest.getNumberOfGuests()),
                () -> assertThat(response.isEmpty()).isEqualTo(DEFAULT_ORDER_TABLE_EMPTY)
        );
    }
}
