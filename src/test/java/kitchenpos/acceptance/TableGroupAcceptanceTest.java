package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.POST_DEFAULT_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("POST /api/table-groups")
    @Test
    void create() {
        // given
        long orderTableId1 = POST_DEFAULT_ORDER_TABLE(1, true);
        long orderTableId2 = POST_DEFAULT_ORDER_TABLE(1, true);

        TableGroupRequest tableGroupRequest =
                TableGroupRequest.from(Arrays.asList(orderTableId1, orderTableId2));

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

    public static long POST_DEFAULT_TABLE_GROUPS() {
        // given
        long orderTableId1 = POST_DEFAULT_ORDER_TABLE(1, true);
        long orderTableId2 = POST_DEFAULT_ORDER_TABLE(1, true);

        TableGroupRequest tableGroupRequest =
                TableGroupRequest.from(Arrays.asList(orderTableId1, orderTableId2));

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);
        return tableGroupResponse.getId();
    }
}
