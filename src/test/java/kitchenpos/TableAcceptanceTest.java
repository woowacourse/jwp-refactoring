package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("주문 테이블 기능")
@Sql({"classpath:tableInit.sql", "classpath:dataInsert.sql"})
public class TableAcceptanceTest extends ApplicationTest {

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("주문 테이블을 추가하는데 성공하면 201 응답을 받는다.")
    @Test
    void createOrderTable() {

        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);

        //when
        ExtractableResponse<Response> response = 주문_테이블_추가(orderTable);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("테이블 상태를 변경하는데 성공하면, 200 응답을 받는다.")
    @ValueSource(booleans = {true, false})
    @ParameterizedTest
    void changeOrderTableEmptyStatus(final boolean emptyStatus) {

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(emptyStatus);

        //when
        ExtractableResponse<Response> response = 주문_테이블_비우거나_채우기(orderTable, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("전체 주문 테이블을 불러오는데 성공하면, 200 응답을 받는다.")
    @Test
    void getTables() {

        //given
        ExtractableResponse<Response> response = 전체_주문테이블_조회();

        //when
        List<OrderTable> orderTables = response.jsonPath().getList(".", OrderTable.class);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(orderTables).hasSize(8);
    }

    @DisplayName("손님수를 변경하는데 성공하면, 200 응답을 받는다.")
    @Test
    void changeGuestNumber(){

        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        주문_테이블_비우거나_채우기(orderTable, 1L);

        orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);

        //when
        ExtractableResponse<Response> response = 손님_수_변경(orderTable, 1L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("잘못된 ")
    @Nested
    class FailTest {
        @DisplayName("주문 테이블 id로 테이블을 비우거나, 채우려고 하면 500 응답을 받는다.")
        @ValueSource(longs = {-1L, 2000L})
        @ParameterizedTest
        void fail1(final long tableId) {

            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(true);

            //when
            ExtractableResponse<Response> response = 주문_테이블_비우거나_채우기(orderTable, tableId);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("손님의 수로 변경하려고 하면, 500 응답을 받는다.")
        @Test
        void fail2() {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            주문_테이블_비우거나_채우기(orderTable, 1L);

            orderTable = new OrderTable();
            orderTable.setNumberOfGuests(-1);

            //when
            ExtractableResponse<Response> response = 손님_수_변경(orderTable, 1L);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        @DisplayName("테이블 Id로 손님의 수를 변경하려고 하면, 500 응답을 받는다.")
        @Test
        void fail3() {
            //given
            OrderTable orderTable = new OrderTable();
            orderTable.setEmpty(false);
            주문_테이블_비우거나_채우기(orderTable, 1L);

            orderTable = new OrderTable();
            orderTable.setNumberOfGuests(1);

            //when
            ExtractableResponse<Response> response = 손님_수_변경(orderTable, 1000L);

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @DisplayName("비어있는 테이블의 손님의 수를 변경하려고 하면, 500 응답을 받는다.")
    @Test
    void fail4() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        //when
        ExtractableResponse<Response> response = 손님_수_변경(orderTable, 1L);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 주문_테이블_비우거나_채우기(final OrderTable orderTable, final Long tableId) {
        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .put(String.format("/api/tables/%s/empty", tableId))
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 손님_수_변경(final OrderTable orderTable, final Long tableId) {
        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .put(String.format("/api/tables/%s/number-of-guests", tableId))
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 주문_테이블_추가(final OrderTable orderTable) {
        return RestAssured
            .given().log().all()
            .body(orderTable)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .post("/api/tables")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 전체_주문테이블_조회() {
        return RestAssured
            .given().log().all()
            .when().get("/api/tables")
            .then().log().all()
            .extract();
    }
}
