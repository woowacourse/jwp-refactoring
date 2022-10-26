package kitchenpos.acceptance;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        TableGroup tableGroup = createTableGroup();

        // when, then
        _테이블그룹생성_Id반환(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        TableGroup tableGroup = createTableGroup();
        long tableGroupId = _테이블그룹생성_Id반환(tableGroup);

        // when, then
        delete("api/table-groups/" + tableGroupId).assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private long _테이블그룹생성_Id반환(final TableGroup tableGroup) {
        return post("api/table-groups", tableGroup).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    private TableGroup createTableGroup() {
        OrderTable orderTable1 = new OrderTable(1, true);
        OrderTable orderTable2 = new OrderTable(2, true);
        long tableId1 = _테이블생성_Id반환(orderTable1);
        long tableId2 = _테이블생성_Id반환(orderTable2);

        OrderTable savedOrderTable1 = orderTableDao.findById(tableId1).orElseThrow();
        OrderTable savedOrderTable2 = orderTableDao.findById(tableId2).orElseThrow();

        return new TableGroup(List.of(savedOrderTable1, savedOrderTable2));
    }
}
