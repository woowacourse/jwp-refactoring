package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.request.TableGroupCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.EMPTY_테이블;
import static kitchenpos.fixture.OrderTableFixture.NOT_EMPTY_테이블;
import static kitchenpos.step.TableGroupStep.테이블_그룹_삭제_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @Nested
    class TableGroupCreateTest {

        @Test
        void 테이블_그룹을_생성한다() {
            final OrderTable table1 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
            final OrderTable table2 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());

            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1.getId(), table2.getId())));
            final TableGroup result = response.jsonPath().getObject("", TableGroup.class);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(result.getOrderTables())
                            .usingRecursiveComparison()
                            .ignoringFields("id", "tableGroupId", "empty")
                            .isEqualTo(List.of(table1, table2))
            );
        }

        @Test
        void 테이블_그룹으로_묶이는_테이블은_다른_그룹으로_이미_묶인_테이블이_아니다() {
            final OrderTable table1 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
            final OrderTable table2 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
            final OrderTable table3 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());

            테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1.getId(), table2.getId())));
            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1.getId(), table3.getId())));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 테이블_그룹으로_묶이는_테이블은_모두_비어있는_상태여야한다() {
            final OrderTable table1 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
            final OrderTable table2 = 테이블_생성_요청하고_테이블_반환(NOT_EMPTY_테이블());

            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1.getId(), table2.getId())));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class TableGroupDeleteTest {

        @Test
        void 테이블_그룹을_해제한다() {
            final OrderTable table1 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());
            final OrderTable table2 = 테이블_생성_요청하고_테이블_반환(EMPTY_테이블());

            final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(new TableGroupCreateRequest(List.of(table1.getId(), table2.getId())));

            final ExtractableResponse<Response> response = 테이블_그룹_삭제_요청(tableGroupId);

            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        }
    }
}
