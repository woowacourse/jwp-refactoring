package kitchenpos.table.acceptance;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.menu.application.request.TableGroupRequest;
import kitchenpos.table.application.request.OrderTableRequest;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        TableGroupRequest request = createTableGroup();

        // when, then
        _테이블그룹생성_Id반환(request);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroupRequest request = createTableGroup();
        long tableGroupId = _테이블그룹생성_Id반환(request);

        // when, then
        delete("api/table-groups/" + tableGroupId).assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private long _테이블그룹생성_Id반환(final TableGroupRequest request) {
        return post("api/table-groups", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    private TableGroupRequest createTableGroup() {
        OrderTableRequest orderTable1 = new OrderTableRequest(NO_ID, NO_ID, 1, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(NO_ID, NO_ID, 2, true);
        long tableId1 = _테이블생성_Id반환(orderTable1);
        long tableId2 = _테이블생성_Id반환(orderTable2);

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(tableId1, NO_ID, 1, true);
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(tableId2, NO_ID, 2, true);
        return new TableGroupRequest(List.of(orderTableRequest1, orderTableRequest2));
    }
}
