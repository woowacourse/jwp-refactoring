package kitchenpos.integration;

import static io.restassured.RestAssured.given;
import static kitchenpos.utils.TestObjectUtils.createTableGroup;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupRestControllerIntegrationTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블 그룹 생성")
    @Test
    void create() {
        Map<String, Object> data = new HashMap<>();
        data.put("orderTables", Arrays.asList(
            new HashMap<String, String>() {{
                put("id", "1");
            }},
            new HashMap<String, String>() {{
                put("id", "2");
            }})
        );

        // @formatter:off
        given().
            contentType(MediaType.APPLICATION_JSON_VALUE).
            body(data).
        when().
            post("/api/table-groups").
        then().
            assertThat().
            statusCode(HttpStatus.CREATED.value()).
            header("Location", containsString("/api/table-groups/")).
            body("id", any(Integer.class)).
            body("createdDate", any(String.class)).
            body("orderTables", hasSize(2));
        // @formatter:on
    }


    @DisplayName("테이블 그룹 해제")
    @Test
    void ungroup() {
        List<OrderTable> orderTables = tableService.list();
        TableGroup tableGroup = tableGroupService.create(createTableGroup(orderTables));
        // @formatter:off
        given().
        when().
            delete("/api/table-groups/" + tableGroup.getId()).
        then().
            assertThat().
            statusCode(HttpStatus.NO_CONTENT.value());
        // @formatter:on
    }
}
