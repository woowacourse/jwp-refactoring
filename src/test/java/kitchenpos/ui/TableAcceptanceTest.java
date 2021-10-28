package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuGroupRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.request.TableCreateRequest;
import kitchenpos.dto.request.TableEmptyChangeRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.request.TableGuestChangeRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.dto.response.TableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

@DisplayName("Table 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("Table 생성")
    @Test
    void create() {
        TableCreateRequest request = new TableCreateRequest(1, false);
        TableResponse response = makeResponse("/api/tables", TestMethod.POST, request)
            .as(TableResponse.class);

        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("Table 리스트를 불러온다.")
    @Test
    void list() {
        TableCreateRequest request = new TableCreateRequest(1, false);
        makeResponse("/api/tables", TestMethod.POST, request);
        makeResponse("/api/tables", TestMethod.POST, request);

        List<TableResponse> responses = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", TableResponse.class);

        assertThat(responses.size()).isEqualTo(2);
    }

    @DisplayName("빈 테이블 유무를 변경한다.")
    @ParameterizedTest
    @CsvSource({"true,false" , "false, true"})
    void changeEmpty(boolean input, boolean expected) {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, input);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);

        TableEmptyChangeRequest request = new TableEmptyChangeRequest(expected);

        TableResponse response = makeResponse("/api/tables/" + tableResponse.getId() + "/empty",
            TestMethod.PUT, request).as(TableResponse.class);

        assertThat(response.isEmpty()).isEqualTo(expected);
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 테이블이 존재하지 않습니다")
    @Test
    void change_empty_fail_table_non_exist() {
        TableEmptyChangeRequest request = new TableEmptyChangeRequest(true);

        int actual = makeResponse("/api/tables/" + 999 + "/empty",
            TestMethod.PUT, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 테이블이 속한 단체가 존재합니다.")
    @Test
    void change_empty_fail_group_exist() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse1 = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);
        TableResponse tableResponse2 = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
            Arrays.asList(tableResponse1.getId(), tableResponse2.getId()));
        makeResponse("/api/table-groups", TestMethod.POST, tableGroupCreateRequest)
            .as(TableGroupResponse.class);

        TableEmptyChangeRequest request = new TableEmptyChangeRequest(false);

        int actual = makeResponse("/api/tables/" + tableResponse1.getId() + "/empty",
            TestMethod.PUT, request).statusCode();
        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 식사가 완료되지 않은 테이블입니다.")
    @Test
    void change_empty_fail_status_completed() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("menu-group");
        MenuGroupResponse menuGroupResponse = makeResponse("/api/menu-groups/", TestMethod.POST, menuGroupRequest)
            .as(MenuGroupResponse.class);
        ProductRequest productRequest = new ProductRequest("product", BigDecimal.valueOf(1000));
        ProductResponse productResponse = makeResponse("/api/products", TestMethod.POST, productRequest).as(
            ProductResponse.class);
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(productResponse.getId(), 10L);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("menu", BigDecimal.valueOf(5000),
            menuGroupResponse.getId(), Collections.singletonList(menuProductCreateRequest));
        MenuResponse menuResponse = makeResponse("/api/menus", TestMethod.POST, menuCreateRequest)
            .as(MenuResponse.class);
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);
        OrderLineItemCreateRequest orderLineItemCreateRequest = new OrderLineItemCreateRequest(
            menuResponse.getId(), 2L);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(tableResponse.getId(),
            Collections.singletonList(orderLineItemCreateRequest));
        makeResponse("/api/orders", TestMethod.POST, orderCreateRequest).as(OrderResponse.class);

        TableEmptyChangeRequest request = new TableEmptyChangeRequest(false);

        int actual = makeResponse("/api/tables/" + tableResponse.getId() + "/empty",
            TestMethod.PUT, request).statusCode();
        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);

        TableGuestChangeRequest request = new TableGuestChangeRequest(3);

        TableResponse response = makeResponse(
            "/api/tables/" + tableResponse.getId() + "/number-of-guests", TestMethod.PUT,
            request)
            .as(TableResponse.class);

        assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("손님 수 변경 실패 - 손님 수는 0명 이상이어야 한다.")
    @Test
    void change_guest_fail_less_than_zero() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);

        TableGuestChangeRequest request = new TableGuestChangeRequest(-3);

        int actual = makeResponse(
            "/api/tables/" + tableResponse.getId() + "/number-of-guests", TestMethod.PUT,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수 변경 실패 - 테이블이 존재하지 않는다.")
    @Test
    void change_guest_fail_table_non_exist() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(1, false);
        makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);

        TableGuestChangeRequest request = new TableGuestChangeRequest(4);

        int actual = makeResponse(
            "/api/tables/" + 999 + "/number-of-guests", TestMethod.PUT,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수 변경 실패 - 손님이 있는 테이블은 비어있을 수 없습니다.")
    @Test
    void change_guest_fail_unable_empty_table() {
        TableCreateRequest tableCreateRequest = new TableCreateRequest(0, true);
        TableResponse tableResponse = makeResponse("/api/tables", TestMethod.POST, tableCreateRequest)
            .as(TableResponse.class);

        TableGuestChangeRequest request = new TableGuestChangeRequest(4);

        int actual = makeResponse(
            "/api/tables/" + tableResponse.getId() + "/number-of-guests", TestMethod.PUT,
            request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
