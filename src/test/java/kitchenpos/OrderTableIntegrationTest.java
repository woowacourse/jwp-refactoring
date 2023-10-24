package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableIntegrationTest extends IntegrationTest {

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @EnumSource(OrderTableFixture.class)
    @DisplayName("주문 테이블을 등록할 수 있다.")
    void create_success(OrderTableFixture fixture) {
        // given
        OrderTable table = fixture.toEntity();

        // when
        steps.createTable(table);
        OrderTable actual = sharedContext.getResponse().as(OrderTable.class);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @ValueSource(ints = {0, 1})
    @DisplayName("주문 테이블의 수용 인원은 0명 이상이어야 한다.")
    void change_number_of_guests(int changedNumber) {
        // given
        OrderTable table = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when
        steps.createTable(table);
        OrderTable created = sharedContext.getResponse().as(OrderTable.class);
        steps.changeNumberOfGuests(created, changedNumber);
        OrderTable actual = sharedContext.getResponse().as(OrderTable.class);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(changedNumber);
    }

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @ValueSource(ints = {-1, -100})
    @DisplayName("주문 테이블의 수용 인원은 0 미만이면 예외가 발생한다.")
    void change_number_of_guests_failure(int changedNumber) {
        // given
        OrderTable table = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when
        steps.createTable(table);
        OrderTable created = sharedContext.getResponse().as(OrderTable.class);
        steps.changeNumberOfGuests(created, changedNumber);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    void change_empty_success() {
        // given
        OrderTable table = OrderTableFixture.computeDefaultOrderTable(arg -> {
            arg.setEmpty(true);
        });

        steps.createTable(table);
        OrderTable savedOrderTable = sharedContext.getResponse().as(OrderTable.class);
        savedOrderTable.setEmpty(false);

        // when
        OrderTable result = steps.changeEmpty(savedOrderTable.getId(), table);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 테이블 그룹에 속한 경우 빈 상태로 변경할 수 없다.")
    void change_empty_failure_table_group_exists() {
        // given
        OrderTable table1 = OrderTableFixture.computeDefaultOrderTable(arg -> {
            arg.setEmpty(false);
            arg.setId(1L);
        });
        OrderTable table2 = OrderTableFixture.computeDefaultOrderTable(arg -> {
            arg.setEmpty(false);
            arg.setId(2L);
        });
        TableGroup tableGroup = TableGroupFixture.computeDefaultTableGroup(arg -> {
            arg.setOrderTables(List.of(table1, table2));
        });

        steps.createTable(table1);
        steps.createTable(table2);
        steps.createTableGroup(tableGroup);

        // when
        table1.setEmpty(true);
        steps.changeEmpty(table1.getId(), table1);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @CsvSource({"COOKING", "MEAL"})
    @DisplayName("주문 테이블이 주문 상태가 Cooking, Meal인 경우 빈 상태로 변경할 수 없다.")
    void change_empty_failure_order_status_in(OrderStatus orderStatus) {
        // given
        OrderTable table = OrderTableFixture.computeDefaultOrderTable(arg -> {
            arg.setId(1L);
        });
        Order order = OrderFixture.computeDefaultOrder(arg -> {
            arg.setOrderStatus(orderStatus.name());
            arg.setOrderTableId(table.getId());
        });
        steps.createTable(table);
        steps.createOrder(order);

        // when
        table.setEmpty(true);
        steps.changeEmpty(table.getId(), table);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }
}
