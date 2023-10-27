package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.ui.request.TableCreateRequest;
import kitchenpos.order.ui.request.TableGroupCreateRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.step.TableGroupStep.테이블_그룹_삭제_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청;
import static kitchenpos.step.TableGroupStep.테이블_그룹_생성_요청하고_아이디_반환;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_아이디_반환;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;

class TableGroupAcceptanceTest extends AcceptanceTest {

    @Nested
    class TableGroupCreateTest {

        @Test
        void 테이블_그룹을_생성한다() {
            final Long table1Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));
            final Long table2Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));

            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1Id, table2Id)));

            assertThat(response.statusCode()).isEqualTo(CREATED.value());
        }

        @Test
        void 테이블_그룹으로_묶이는_테이블은_다른_그룹으로_이미_묶인_테이블이_아니다() {
            final Long table1Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));
            final Long table2Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));
            final Long table3Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));

            테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1Id, table2Id)));
            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1Id, table3Id)));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }

        @Test
        void 테이블_그룹으로_묶이는_테이블은_모두_비어있는_상태여야한다() {
            final Long table1Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, false));
            final Long table2Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));

            final ExtractableResponse<Response> response = 테이블_그룹_생성_요청(new TableGroupCreateRequest(List.of(table1Id, table2Id)));

            assertThat(response.statusCode()).isEqualTo(INTERNAL_SERVER_ERROR.value());
        }
    }

    @Nested
    class TableGroupDeleteTest {

        @Test
        void 테이블_그룹을_해제한다() {
            final Long table1Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));
            final Long table2Id = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(5, true));

            final Long tableGroupId = 테이블_그룹_생성_요청하고_아이디_반환(new TableGroupCreateRequest(List.of(table1Id, table2Id)));

            final ExtractableResponse<Response> response = 테이블_그룹_삭제_요청(tableGroupId);

            assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        }
    }
}
