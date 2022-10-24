package kitchenpos.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.OrderTable;

public class TableAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, true);

        // when, then
        _테이블생성검증(orderTable);
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, true);
        OrderTable orderTable2 = new OrderTable(1L, 1, true);

        _테이블생성검증(orderTable1);
        _테이블생성검증(orderTable2);

        // when, then
        _테이블조회검증();
    }

    @Test
    @DisplayName("테이블을 빈 테이블로 변경한다.")
    void changeEmpty() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, false);
        long tableId = _테이블생성_Id반환(orderTable);

        // when, then
        put("/api/tables/" + tableId + "/empty", orderTable).assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable(1L, 1, false);
        long tableId = _테이블생성_Id반환(orderTable);

        // when, then
        OrderTable orderTableToChange = new OrderTable(1L, 2, false);
        put("/api/tables/" + tableId + "/number-of-guests", orderTableToChange).assertThat()
            .statusCode(HttpStatus.OK.value());
    }

    private long _테이블생성_Id반환(final OrderTable orderTable) {
        return post("/api/tables", orderTable).assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .body()
            .jsonPath()
            .getLong("id");
    }

    private void _테이블생성검증(final OrderTable orderTable) {
        post("/api/tables", orderTable).assertThat()
            .statusCode(HttpStatus.CREATED.value());
    }

    private void _테이블조회검증() {
        get("/api/tables").assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}
