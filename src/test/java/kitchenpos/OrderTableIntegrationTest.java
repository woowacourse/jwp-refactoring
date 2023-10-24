package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.dto.TableGroupDto;
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
        OrderTableDto table = fixture.toDto();

        // when
        steps.createTable(table);
        OrderTableDto actual = sharedContext.getResponse().as(OrderTableDto.class);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @ValueSource(ints = {0, 1})
    @DisplayName("주문 테이블의 수용 인원은 0명 이상이어야 한다.")
    void change_number_of_guests(int changedNumber) {
        // given
        OrderTableDto table = OrderTableFixture.OCCUPIED_TABLE.toDto();

        // when
        steps.createTable(table);
        OrderTableDto created = sharedContext.getResponse().as(OrderTableDto.class);
        steps.changeNumberOfGuests(created, changedNumber);
        OrderTableDto actual = sharedContext.getResponse().as(OrderTableDto.class);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(changedNumber);
    }

    @ParameterizedTest(name = "{displayName} - {arguments}")
    @ValueSource(ints = {-1, -100})
    @DisplayName("주문 테이블의 수용 인원은 0 미만이면 예외가 발생한다.")
    void change_number_of_guests_failure(int changedNumber) {
        // given
        OrderTableDto table = OrderTableFixture.OCCUPIED_TABLE.toDto();

        // when
        steps.createTable(table);
        OrderTableDto created = sharedContext.getResponse().as(OrderTableDto.class);
        steps.changeNumberOfGuests(created, changedNumber);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }

    @Test
    @DisplayName("주문 테이블을 빈 상태로 변경할 수 있다.")
    void change_empty_success() {
        // given
        OrderTableDto table = OrderTableFixture.computeDefaultOrderTableDto(arg -> {
            arg.setEmpty(true);
        });

        steps.createTable(table);
        OrderTableDto savedOrderTableDto = sharedContext.getResponse().as(OrderTableDto.class);
        savedOrderTableDto.setEmpty(false);

        // when
        OrderTableDto result = steps.changeEmpty(savedOrderTableDto.getId(), table);

        // then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("주문 테이블이 테이블 그룹에 속한 경우 빈 상태로 변경할 수 없다.")
    void change_empty_failure_table_group_exists() {
        // given
        OrderTableDto table1 = OrderTableFixture.computeDefaultOrderTableDto(arg -> {
            arg.setEmpty(false);
            arg.setId(1L);
        });
        OrderTableDto table2 = OrderTableFixture.computeDefaultOrderTableDto(arg -> {
            arg.setEmpty(false);
            arg.setId(2L);
        });
        TableGroupDto tableGroupDto = TableGroupFixture.computeDefaultTableGroupDto(arg -> {
            arg.setOrderTables(List.of(table1, table2));
        });

        steps.createTable(table1);
        steps.createTable(table2);
        steps.createTableGroup(tableGroupDto);

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
        OrderTableDto table = OrderTableFixture.computeDefaultOrderTableDto(arg -> {
            arg.setId(1L);
        });
        OrderDto orderDto = OrderFixture.computeDefaultOrder(arg -> {
            arg.setOrderStatus(orderStatus.name());
            arg.setOrderTableId(table.getId());
        });
        steps.createTable(table);
        steps.createOrder(orderDto);

        // when
        table.setEmpty(true);
        steps.changeEmpty(table.getId(), table);
        ExtractableResponse<Response> response = sharedContext.getResponse();

        // then
        assertThat(response.statusCode()).isEqualTo(500);
    }
}
