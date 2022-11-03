package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.application.dto.TableDto;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class OrderTableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("신규 테이블을 생성할 수 있다")
    void createOrderTable() {
        final OrderTable orderTable = OrderTable.create();
        final ExtractableResponse<Response> response = 테이블_등록_요청(orderTable);
        final OrderTable responseBody = response.body().as(OrderTable.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 단일_데이터_검증(responseBody.isEmpty(), true),
                () -> 단일_데이터_검증(responseBody.getNumberOfGuests(), 0)
        );
    }

    @Test
    @DisplayName("전체 테이블을 조회할 수 있다")
    void getOrderTables() {
        final TableDto orderTable1 = 테이블_등록(2, false);
        final TableDto orderTable2 = 테이블_등록(0, true);

        final ExtractableResponse<Response> response = 테이블_전체_조회_요청();
        final List<OrderTable> responseBody = response.body()
                .jsonPath()
                .getList(".", OrderTable.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 리스트_데이터_검증(responseBody, "id", orderTable1.getId(), orderTable2.getId()),
                () -> 리스트_데이터_검증(responseBody, "tableGroupId", null, null),
                () -> 리스트_데이터_검증(
                        responseBody,
                        "numberOfGuests",
                        orderTable1.getNumberOfGuests(),
                        orderTable2.getNumberOfGuests()
                ),
                () -> 리스트_데이터_검증(responseBody, "empty", false, true)
        );
    }

    @Test
    @DisplayName("테이블을 채우거나 비울 수 있다.")
    void changeEmpty() {
        final TableDto orderTable = 테이블_등록(0, true);

        // when
        final ExtractableResponse<Response> response = 테이블_empty_변경_요청(orderTable.getId(), false);
        final OrderTable responseBody = response.body()
                .as(OrderTable.class);

        // then
        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 단일_데이터_검증(responseBody.isEmpty(), false),
                () -> 단일_데이터_검증(responseBody.getId(), orderTable.getId())
        );
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests() {
        final TableDto orderTable = 테이블_등록(0, true);
        final TableDto fullTable = 테이블_채움(orderTable.getId());
        final int numberOfGuests = 5;

        final ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(fullTable.getId(), numberOfGuests);
        final OrderTable responseBody = response.body()
                .as(OrderTable.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 단일_데이터_검증(responseBody.isEmpty(), false),
                () -> 단일_데이터_검증(responseBody.getNumberOfGuests(), numberOfGuests),
                () -> 단일_데이터_검증(responseBody.getId(), orderTable.getId())
        );
    }
}
