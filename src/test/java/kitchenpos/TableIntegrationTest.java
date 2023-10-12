package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_상태_Empty로_변경_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static kitchenpos.step.TableStep.테이블_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class TableIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 테이블을_생성한다() {
        final OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(4));
        final OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(5));
        final List<OrderTable> orderTables = List.of(savedOrderTable1, savedOrderTable2);

        final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(new TableGroup(orderTables));
        savedOrderTable1.setTableGroupId(tableGroupId);
        savedOrderTable2.setTableGroupId(tableGroupId);

        final OrderTable orderTable = new OrderTable(4);
        final ExtractableResponse<Response> response = 테이블_생성_요청(orderTable);
        final OrderTable result = response.jsonPath().getObject("", OrderTable.class);

        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id", "empty")
                .isEqualTo(orderTable);
    }

    @Test
    void 테이블을_조회한다() {
        final OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(4));
        final OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(5));
        final List<OrderTable> orderTables = List.of(savedOrderTable1, savedOrderTable2);

        final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(new TableGroup(orderTables));
        savedOrderTable1.setTableGroupId(tableGroupId);
        savedOrderTable2.setTableGroupId(tableGroupId);

        final OrderTable orderTable = new OrderTable(4);
        테이블_생성_요청(orderTable);

        final ExtractableResponse<Response> response = 테이블_조회_요청();

        assertThat(response.statusCode()).isEqualTo(OK.value());
    }

    @Test
    void 테이블을_Empty_상태로_변경한다() {
        final OrderTable savedOrderTable1 = orderTableDao.save(new OrderTable(4));
        final OrderTable savedOrderTable2 = orderTableDao.save(new OrderTable(5));
        final List<OrderTable> orderTables = List.of(savedOrderTable1, savedOrderTable2);

        final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(new TableGroup(orderTables));
        savedOrderTable1.setTableGroupId(tableGroupId);
        savedOrderTable2.setTableGroupId(tableGroupId);

        final OrderTable orderTable = new OrderTable(4);
        final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(orderTable);

        final ExtractableResponse<Response> response = 테이블_상태_Empty로_변경_요청(savedOrderTable);

        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.jsonPath().getBoolean("empty")).isTrue();
    }
}
