package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;

import static kitchenpos.step.TableGroupStep.테이블_그룹_삭제_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청하고_아이디_반환;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.NO_CONTENT;

public class TableGroupIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    void 테이블_그룹을_생성한다() {
        final TableGroup tableGroup = new TableGroup(
                List.of(
                        orderTableDao.save(new OrderTable(4)),
                        orderTableDao.save(new OrderTable(5))
                )
        );

        final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(tableGroup);
        final TableGroup result = response.jsonPath().getObject("", TableGroup.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(result.getOrderTables())
                        .usingRecursiveComparison()
                        .ignoringFields("id", "tableGroupId", "empty")
                        .isEqualTo(tableGroup.getOrderTables())
        );
    }

    @Test
    void 테이블_그룹을_조회한다() {
        final TableGroup tableGroup = new TableGroup(
                List.of(
                        orderTableDao.save(new OrderTable(4)),
                        orderTableDao.save(new OrderTable(5))
                )
        );
        final long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(tableGroup);

        final ExtractableResponse<Response> response = 테이블_그룹_삭제_요청(tableGroupId);

        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
    }
}
