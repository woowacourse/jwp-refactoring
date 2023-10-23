package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.request.TableCreateRequest;
import kitchenpos.ui.request.TableUpdateEmptyRequest;
import kitchenpos.ui.request.TableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.step.TableStep.테이블_상태_Empty로_변경_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static kitchenpos.step.TableStep.테이블_조회_요청;
import static kitchenpos.step.TableStep.테이블에_앉은_사람_수_변경_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class TableAcceptanceTest extends AcceptanceTest {

    @Nested
    class TableCreateTest {

        @Test
        void 테이블을_생성한다() {
            final ExtractableResponse<Response> response = 테이블_생성_요청(new TableCreateRequest(6, false));

            assertThat(response.statusCode()).isEqualTo(CREATED.value());
        }
    }

    @Nested
    class TableQueryTest {

        @Test
        void 테이블을_조회한다() {
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(new TableCreateRequest(6, false));

            final ExtractableResponse<Response> response = 테이블_조회_요청();

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getList("", OrderTable.class).get(0))
                            .usingRecursiveComparison()
                            .isEqualTo(savedOrderTable)
            );
        }
    }

    @Nested
    class TableUpdateTest {

        @Test
        void 테이블을_Empty_상태로_변경한다() {
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(new TableCreateRequest(6, false));
            final ExtractableResponse<Response> response = 테이블_상태_Empty로_변경_요청(savedOrderTable.getId(), new TableUpdateEmptyRequest(true));

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getBoolean("empty")).isTrue()
            );
        }

        @Test
        void 테이블의_사람_수를_변경한다() {
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(new TableCreateRequest(6, false));

            final int newNumberOfGuests = 5;

            final ExtractableResponse<Response> response = 테이블에_앉은_사람_수_변경_요청(
                    savedOrderTable.getId(),
                    new TableUpdateNumberOfGuestsRequest(newNumberOfGuests)
            );

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(newNumberOfGuests)
            );
        }
    }
}
