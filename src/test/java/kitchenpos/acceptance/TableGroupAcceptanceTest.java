package kitchenpos.acceptance;

import static kitchenpos.acceptance.OrderTableAcceptanceTest.테이블을_생성한다;
import static kitchenpos.acceptance.support.RequestUtil.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.acceptance.support.RequestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    static ExtractableResponse<Response> 단체_지정을_생성한다(final List<Long> tableIds) {
        List<Map<String, Long>> orderTables = tableIds.stream()
                .map(id -> Map.of("id", id))
                .collect(Collectors.toList());

        return post("/api/table-groups", orderTables);
    }

    static ExtractableResponse<Response> 단체_지정을_제거한다(final Long id) {
        return RequestUtil.delete("/api/table-group/" + id);
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        long 테이블1_ID = 테이블을_생성한다(10, false).jsonPath().getLong("id");
        long 테이블2_ID = 테이블을_생성한다(20, false).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 단체_지정을_생성한다(List.of(테이블1_ID, 테이블2_ID));

        // then
        상태코드를_검증한다(response, HttpStatus.CREATED);
        리스트_길이를_검증한다(response, "orderTables", 2);
    }

    @DisplayName("단체 지정을 제거한다.")
    @Test
    void delete() {
        // given
        long 테이블1_ID = 테이블을_생성한다(10, false).jsonPath().getLong("id");
        long 테이블2_ID = 테이블을_생성한다(20, false).jsonPath().getLong("id");
        long 생성된_단체_지정_ID = 단체_지정을_생성한다(List.of(테이블1_ID, 테이블2_ID))
                .jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = 단체_지정을_제거한다(생성된_단체_지정_ID);

        // then
        상태코드를_검증한다(response, HttpStatus.CREATED);
        리스트_길이를_검증한다(response, "orderTables", 2);
    }
}
