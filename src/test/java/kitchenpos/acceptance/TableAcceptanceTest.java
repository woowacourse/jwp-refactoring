package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 인수 테스트")
public class TableAcceptanceTest extends AcceptanceTest{

    @DisplayName("POST /api/tables")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", "1");
        params.put("empty", false);

        // when - then
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    public static long POST_DEFAULT_ORDER_TABLE() {
        Map<String, Object> params = new HashMap<>();
        params.put("numberOfGuests", "1");
        params.put("empty", false);

        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/tables")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        OrderTable orderTable = response.as(OrderTable.class);
        return orderTable.getId();
    }
}
