package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("TableGroup 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("TableGroup 생성")
    @Test
    void create() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse1 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableResponse tableResponse2 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableResponse1.getId(), tableResponse2.getId()));

        TableGroupResponse response = makeResponse("/api/table-groups", TestMethod.POST, request)
            .as(TableGroupResponse.class);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getOrderTables()).isNotEmpty()
        );
    }

    @DisplayName("TableGroup 생성 실패 - 테이블이 2개 미만이다.")
    @Test
    void create_fail_table_less_than_two() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Collections.singletonList(tableResponse.getId()));

        int actual = makeResponse("/api/table-groups", TestMethod.POST,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 생성 실패 - 존재하지 않는 테이블이 있다.")
    @Test
    void create_fail_table_non_exist() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableResponse.getId(), 999L));

        int actual = makeResponse("/api/table-groups", TestMethod.POST,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 생성 실패 - 비지 않은 테이블이 있다.")
    @Test
    void create_fail_table_not_empty() {
        TableCreateRequest tableCreateRequest1 = new TableCreateRequest(0, true);
        TableCreateRequest tableCreateRequest2 = new TableCreateRequest(1, false);
        TableResponse tableResponse1 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest1)
            .as(TableResponse.class);
        TableResponse tableResponse2 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest2)
            .as(TableResponse.class);
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableResponse1.getId(), tableResponse2.getId()));

        int actual = makeResponse("/api/table-groups", TestMethod.POST,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("TableGroup 해제")
    @Test
    void ungroup() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse1 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableResponse tableResponse2 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Arrays.asList(tableResponse1.getId(), tableResponse2.getId()));
        TableGroupResponse response = makeResponse("/api/table-groups", TestMethod.POST, request)
            .as(TableGroupResponse.class);

        makeResponse("/api/table-groups/" + response.getId(), TestMethod.DELETE);

        List<TableResponse> ungroupedTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", TableResponse.class);
        boolean actual = ungroupedTables.stream()
            .noneMatch(table -> response.getId().equals(table.getTableGroupId()));
        assertThat(actual).isTrue();
    }

    @DisplayName("TableGroup 해제 실패 - 테이블 상태가 식사 완료가 아니다.")
    @Test
    void ungroup_fail_unable_order_status() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menu-group");
        MenuGroupResponse menuGroupResponse = makeResponse("/api/menu-groups/", TestMethod.POST,
            menuGroupRequest)
            .as(MenuGroupResponse.class);
        ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1000));
        ProductResponse productResponse = makeResponse("/api/products", TestMethod.POST,
            productRequest).as(
            ProductResponse.class);
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(
            productResponse.getId(), 10L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("menu",
            BigDecimal.valueOf(5000),
            menuGroupResponse.getId(), Collections.singletonList(menuProductCreateRequest));
        MenuResponse menuResponse = makeResponse("/api/menus", TestMethod.POST, menuCreateRequest)
            .as(MenuResponse.class);
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse1 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);
        TableResponse tableResponse2 = makeResponse("/api/tables", TestMethod.POST,
            tableCreateRequest)
            .as(TableResponse.class);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
            Arrays.asList(tableResponse1.getId(), tableResponse2.getId()));
        TableGroupResponse tableGroupResponse = makeResponse("/api/table-groups", TestMethod.POST,
            tableGroupCreateRequest)
            .as(TableGroupResponse.class);

        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menuResponse.getId(), 2L);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableResponse1.getId(),
            Collections.singletonList(orderLineItemCreateRequest));
        makeResponse("/api/orders", TestMethod.POST, orderCreateRequest).as(OrderResponse.class);

        int actual = makeResponse(
            "/api/table-groups/" + tableGroupResponse.getId(), TestMethod.DELETE).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
