package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.ui.request.TableCreateRequest;
import kitchenpos.order.ui.request.TableUpdateEmptyRequest;
import kitchenpos.order.ui.request.TableUpdateNumberOfGuestsRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.step.TableStep.테이블_상태_Empty로_변경_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_아이디_반환;
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
            테이블_생성_요청하고_아이디_반환(new TableCreateRequest(6, false));
            final ExtractableResponse<Response> response = 테이블_조회_요청();

            assertThat(response.statusCode()).isEqualTo(OK.value());
        }
    }

    @Nested
    class TableUpdateTest {

        @Test
        void 테이블을_Empty_상태로_변경한다() {
            final Long orderTableId = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(6, false));
            final ExtractableResponse<Response> response = 테이블_상태_Empty로_변경_요청(orderTableId, new TableUpdateEmptyRequest(true));

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getBoolean("empty")).isTrue()
            );
        }

        @Test
        void 테이블의_사람_수를_변경한다() {
            final Long orderTableId = 테이블_생성_요청하고_아이디_반환(new TableCreateRequest(6, false));

            final int newNumberOfGuests = 5;

            final ExtractableResponse<Response> response = 테이블에_앉은_사람_수_변경_요청(
                    orderTableId,
                    new TableUpdateNumberOfGuestsRequest(newNumberOfGuests)
            );

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(newNumberOfGuests)
            );
        }
    }
}
