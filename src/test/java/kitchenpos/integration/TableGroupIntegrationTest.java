package kitchenpos.integration;

import static kitchenpos.integration.fixture.TableAPIFixture.createDefaultOrderTable;
import static kitchenpos.integration.fixture.TableAPIFixture.createOrderTableAndReturnResponse;
import static kitchenpos.integration.fixture.TableGroupAPIFixture.createTableGroupAndReturnResponse;
import static kitchenpos.integration.fixture.TableGroupAPIFixture.ungroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.integration.helper.InitIntegrationTest;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateOrderTableRequest;
import kitchenpos.table.application.dto.request.TableGroupCreateRequest;
import kitchenpos.table.application.dto.response.OrderTableResponse;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class TableGroupIntegrationTest extends InitIntegrationTest {

    @Test
    @DisplayName("테이블 그룹을 성공적으로 생성한다.")
    void testCreateSuccess() {
        //given
        final OrderTableResponse orderTableResponse1 = createDefaultOrderTable();
        final OrderTableResponse orderTableResponse2 = createOrderTableAndReturnResponse(new OrderTableCreateRequest(5, false));
        final List<TableGroupCreateOrderTableRequest> tableGroupCreateOrderTableRequests = List.of(
                new TableGroupCreateOrderTableRequest(orderTableResponse1.getId()),
                new TableGroupCreateOrderTableRequest(orderTableResponse2.getId())
        );
        final TableGroupCreateRequest request = new TableGroupCreateRequest(tableGroupCreateOrderTableRequests);

        //when
        final TableGroupResponse response = createTableGroupAndReturnResponse(request);

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getCreatedDate().toLocalDate()).isEqualTo(LocalDate.now()),
                () -> assertThat(response.getOrderTables().get(0)).isEqualTo(orderTableResponse1)
        );
    }

    @Test
    @DisplayName("그룹을 성공적으로 해제한다.")
    void testUngroupSuccess() {
        //given
        final OrderTableResponse orderTableResponse1 = createDefaultOrderTable();
        final OrderTableResponse orderTableResponse2 = createOrderTableAndReturnResponse(new OrderTableCreateRequest(5, false));
        final List<TableGroupCreateOrderTableRequest> tableGroupCreateOrderTableRequests = List.of(
                new TableGroupCreateOrderTableRequest(orderTableResponse1.getId()),
                new TableGroupCreateOrderTableRequest(orderTableResponse2.getId())
        );
        final TableGroupCreateRequest request = new TableGroupCreateRequest(tableGroupCreateOrderTableRequests);

        final TableGroupResponse tableGroupResponse = createTableGroupAndReturnResponse(request);

        //when
        //then
        assertDoesNotThrow(() -> ungroup(tableGroupResponse.getId()));
    }
}
