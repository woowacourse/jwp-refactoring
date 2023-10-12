package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.EMPTY_테이블;
import static kitchenpos.step.TableGroupStep.테이블_그룹_삭제_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 테이블_그룹을_생성한다() {
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(4);
        final OrderTable savedOrderTable1 = 테이블_생성_요청하고_테이블_반환(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(5);
        final OrderTable savedOrderTable2 = 테이블_생성_요청하고_테이블_반환(orderTable1);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(tableGroup);
        final TableGroup result = response.jsonPath().getObject("", TableGroup.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(result.getOrderTables())
                        .usingRecursiveComparison()
                        .ignoringFields("id", "tableGroupId", "empty")
                        .isEqualTo(tableGroup.getOrderTables())
        );
    }

    @Test
    void 테이블_그룹을_조회한다() {
        final OrderTable savedOrderTable1 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
        final OrderTable savedOrderTable2 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(List.of(savedOrderTable1, savedOrderTable2));

        final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(tableGroup);

        final ExtractableResponse<Response> response = 테이블_그룹_삭제_요청(tableGroupId);

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
