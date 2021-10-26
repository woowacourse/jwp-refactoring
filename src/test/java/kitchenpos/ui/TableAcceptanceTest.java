package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Table 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {

    @DisplayName("Table 생성")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        assertThat(created.getId()).isNotNull();
    }

    @DisplayName("Table 리스트를 불러온다.")
    @Test
    void list() {
        OrderTable orderTable1 = new OrderTable(1, false);
        OrderTable orderTable2 = new OrderTable(1, false);
        makeResponse("/api/tables", TestMethod.POST, orderTable1);
        makeResponse("/api/tables", TestMethod.POST, orderTable2);

        List<OrderTable> orderTables = makeResponse("/api/tables", TestMethod.GET).jsonPath()
            .getList(".", OrderTable.class);

        assertThat(orderTables.size()).isEqualTo(2);
    }

    @DisplayName("빈 테이블 유무를 변경한다.")
    @Test
    void changeEmpty() {
        Order order = makeResponse("/api/orders", TestMethod.POST, order()).as(Order.class);
        order.changeStatus(OrderStatus.COMPLETION);
        makeResponse("/api/orders/" + order.getId() + "/order-status",
            TestMethod.PUT, order).as(Order.class);

        OrderTable changed = makeResponse("/api/tables/" + order.getOrderTable().getId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE).as(OrderTable.class);

        assertThat(changed.isEmpty()).isTrue();
        makeResponse("/api/tables/" + order.getOrderTable().getId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE);
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 테이블이 존재하지 않습니다")
    @Test
    void change_empty_fail_table_non_exist() {
        Order order = makeResponse("/api/orders", TestMethod.POST, order()).as(Order.class);
        order.setOrderTable(new OrderTable(999L, null, 1, false));
        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + order.getOrderTable().getId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 테이블이 속한 단체가 존재합니다.")
    @Test
    void change_empty_fail_group_exist() {
        TableGroup tableGroup = tableGroup();
        TableGroup createdTableGroup = makeResponse("/api/table-groups", TestMethod.POST,
            tableGroup)
            .as(TableGroup.class);
        OrderTable orderTable = createdTableGroup.getOrderTables().get(0);
        Order order = order();
        order.setOrderTable(orderTable);
        Order createdOrder = makeResponse("/api/orders", TestMethod.POST, order).as(Order.class);

        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + createdOrder.getOrderTable().getId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("빈 테이블 유무 변경 실패 - 식사가 완료되지 않은 테이블입니다.")
    @Test
    void change_empty_fail_status_completed() {
        Order order = makeResponse("/api/orders", TestMethod.POST, order()).as(Order.class);
        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + order.getOrderTable().getId() + "/empty",
            TestMethod.PUT, OrderTable.EMPTY_TABLE);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        created.setNumberOfGuests(3);
        OrderTable changed = makeResponse(
            "/api/tables/" + created.getId() + "/number-of-guests", TestMethod.PUT,
            created)
            .as(OrderTable.class);

        assertThat(changed.getNumberOfGuests()).isEqualTo(created.getNumberOfGuests());
    }

    @DisplayName("손님 수 변경 실패 - 손님 수는 0명 이상이어야 한다.")
    @Test
    void change_guest_fail_less_than_zero() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        created.setNumberOfGuests(-1);
        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + created.getId() + "/number-of-guests", TestMethod.PUT,
            created);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수 변경 실패 - 테이블이 존재하지 않는다.")
    @Test
    void change_guest_fail_table_non_exist() {
        OrderTable orderTable = new OrderTable(1, false);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        created.setId(999L);
        created.setNumberOfGuests(3);
        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + created.getId() + "/number-of-guests", TestMethod.PUT,
            created);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("손님 수 변경 실패 - 손님이 있는 테이블은 비어있을 수 없습니다.")
    @Test
    void change_guest_fail_unable_empty_table() {
        OrderTable orderTable = new OrderTable(0, true);
        OrderTable created = makeResponse("/api/tables", TestMethod.POST, orderTable)
            .as(OrderTable.class);

        created.setNumberOfGuests(3);
        ExtractableResponse<Response> response = makeResponse(
            "/api/tables/" + created.getId() + "/number-of-guests", TestMethod.PUT,
            created);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
