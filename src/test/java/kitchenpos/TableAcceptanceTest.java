package kitchenpos;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static kitchenpos.fixture.OrderTableFixture.NOT_EMPTY_테이블;
import static kitchenpos.step.TableStep.테이블_상태_Empty로_변경_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청;
import static kitchenpos.step.TableStep.테이블_생성_요청하고_테이블_반환;
import static kitchenpos.step.TableStep.테이블_조회_요청;
import static kitchenpos.step.TableStep.테이블에_앉은_사람_수_변경;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

class TableAcceptanceTest extends AcceptanceTest {

    @Nested
    class TableCreateTest {

        @Test
        void 테이블을_생성한다() {
            final ExtractableResponse<Response> response = 테이블_생성_요청(NOT_EMPTY_테이블());
            final OrderTable result = response.jsonPath().getObject("", OrderTable.class);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(CREATED.value()),
                    () -> assertThat(result)
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(NOT_EMPTY_테이블())
            );
        }
    }

    @Nested
    class TableQueryTest {

        @Test
        void 테이블을_조회한다() {
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(NOT_EMPTY_테이블());

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
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(NOT_EMPTY_테이블());
            savedOrderTable.setEmpty(true);

            final ExtractableResponse<Response> response = 테이블_상태_Empty로_변경_요청(savedOrderTable);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getBoolean("empty")).isTrue(),
                    () -> assertThat(response.jsonPath().getObject("", OrderTable.class))
                            .usingRecursiveComparison()
                            .isEqualTo(savedOrderTable)
            );
        }

        @Test
        void 테이블의_사람_수를_변경한다() {
            final OrderTable savedOrderTable = 테이블_생성_요청하고_테이블_반환(NOT_EMPTY_테이블());

            final int newNumberOfGuests = 5;
            savedOrderTable.setNumberOfGuests(newNumberOfGuests);

            final ExtractableResponse<Response> response = 테이블에_앉은_사람_수_변경(savedOrderTable);

            assertAll(
                    () -> assertThat(response.statusCode()).isEqualTo(OK.value()),
                    () -> assertThat(response.jsonPath().getInt("numberOfGuests")).isEqualTo(newNumberOfGuests),
                    () -> assertThat(response.jsonPath().getObject("", OrderTable.class))
                            .usingRecursiveComparison()
                            .isEqualTo(savedOrderTable)
            );
        }
    }
}
