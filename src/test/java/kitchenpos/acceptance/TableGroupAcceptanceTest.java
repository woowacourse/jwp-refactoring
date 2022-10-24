package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import java.util.Arrays;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("여러 테이블을 단체로 지정한다.")
    @Test
    void create() {
        // given
        final OrderTable savedTable1 = dataSupport.saveOrderTable(0, true);
        final OrderTable savedTable2 = dataSupport.saveOrderTable(0, true);
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(savedTable1, savedTable2));

        // when
        final ValidatableResponse response = post("/api/table-groups", tableGroup);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @DisplayName("지정된 단체를 해제한다.")
    @Test
    void ungroup() {
        // given
        final TableGroup savedTableGroup = dataSupport.saveTableGroup();
        final Long savedTableGroupId = savedTableGroup.getId();
        dataSupport.saveOrderTableWithGroup(savedTableGroupId, 2, false);
        dataSupport.saveOrderTableWithGroup(savedTableGroupId, 2, false);

        // when
        final ValidatableResponse response = delete("/api/table-groups/" + savedTableGroupId);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
