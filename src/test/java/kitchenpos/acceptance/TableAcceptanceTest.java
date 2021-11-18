package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.ui.dto.TableRequest;
import kitchenpos.table.ui.dto.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends DomainAcceptanceTest {

    @DisplayName("POST /api/tables")
    @Test
    void create() {
        // given
        TableRequest tableRequest = TableRequest.of(1, false);
        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableRequest)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("GET /api/tables")
    @Test
    void list() {
        // given
        POST_SAMPLE_ORDER_TABLE(1, false);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/empty")
    @Test
    void changeEmpty() {
        // given
        long orderTableId = POST_SAMPLE_ORDER_TABLE(1, false);
        TableRequest tableRequest = TableRequest.empty(true);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableRequest)
                .when().put("/api/tables/" + orderTableId + "/empty")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        TableResponse tableResponse = response.as(TableResponse.class);
        assertThat(tableResponse.isEmpty()).isTrue();
    }

    @DisplayName("PUT /api/tables/{orderTableId}/number-of-guests")
    @Test
    void changeNumberOfGuests() {
        // given
        long orderTableId = POST_SAMPLE_ORDER_TABLE(1, false);
        TableRequest tableRequest = TableRequest.guests(100);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableRequest)
                .when().put("/api/tables/" + orderTableId + "/number-of-guests")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body()).isNotNull();
        TableResponse tableResponse = response.as(TableResponse.class);
        assertThat(tableResponse.getNumberOfGuests()).isEqualTo(100);
    }
}
