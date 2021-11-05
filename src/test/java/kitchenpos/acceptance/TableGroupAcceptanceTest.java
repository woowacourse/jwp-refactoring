package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.order.ui.dto.OrderTableIdRequest;
import kitchenpos.order.ui.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends DomainAcceptanceTest {
    @DisplayName("POST /api/table-groups")
    @Test
    void create() {
        // given
        long orderTableId1 = POST_SAMPLE_ORDER_TABLE(1, true);
        long orderTableId2 = POST_SAMPLE_ORDER_TABLE(1, true);

        TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                Arrays.asList(
                        OrderTableIdRequest.from(orderTableId1),
                        OrderTableIdRequest.from(orderTableId2)
                ));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.body()).isNotNull();
    }

    @DisplayName("DELETE /api/table-groups/{tableGroupId}")
    @Test
    void ungroup() {
        // given
        long tableGroupId = POST_DEFAULT_TABLE_GROUPS();

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/" + tableGroupId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
