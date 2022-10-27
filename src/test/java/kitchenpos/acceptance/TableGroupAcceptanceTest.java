package kitchenpos.acceptance;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.application.OrderTableCreateRequest;
import kitchenpos.application.TableGroupCreateRequest;
import kitchenpos.domain.OrderTable;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        TableGroupCreateRequest request = createTableGroup();

        // when, then
        _테이블그룹생성_Id반환(request);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroupCreateRequest request = createTableGroup();
        long tableGroupId = _테이블그룹생성_Id반환(request);

        // when, then
        delete("api/table-groups/" + tableGroupId).assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private long _테이블그룹생성_Id반환(final TableGroupCreateRequest request) {
        return post("api/table-groups", request).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    private TableGroupCreateRequest createTableGroup() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        long tableId1 = _테이블생성_Id반환(orderTable1);
        long tableId2 = _테이블생성_Id반환(orderTable2);

        OrderTableCreateRequest orderTableCreateRequest1 = new OrderTableCreateRequest(tableId1);
        OrderTableCreateRequest orderTableCreateRequest2 = new OrderTableCreateRequest(tableId2);
        return new TableGroupCreateRequest(List.of(orderTableCreateRequest1, orderTableCreateRequest2));
    }
}
