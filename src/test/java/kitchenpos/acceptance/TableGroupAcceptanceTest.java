package kitchenpos.acceptance;

import io.restassured.response.ValidatableResponse;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.ordertable.TableGroup;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.support.RequestBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("단체 지정 관련 api")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("여러 테이블을 단체로 지정한다.")
    @Test
    void create() {
        // given
        final OrderTable savedTable1 = dataSupport.saveOrderTable(0, true);
        final OrderTable savedTable2 = dataSupport.saveOrderTable(0, true);

        // when
        final TableGroupRequest request = RequestBuilder.ofTableGroup(savedTable1, savedTable2);
        final ValidatableResponse response = post("/api/table-groups", request);

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
        dataSupport.saveOrderTableWithGroup(savedTableGroupId, 2);
        dataSupport.saveOrderTableWithGroup(savedTableGroupId, 2);

        // when
        final ValidatableResponse response = delete("/api/table-groups/" + savedTableGroupId);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }
}
