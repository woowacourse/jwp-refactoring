package kitchenpos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableIntegrationTest extends IntegrationTest {

    @ParameterizedTest
    @EnumSource(OrderTableFixture.class)
    void create_success(OrderTableFixture fixture) {
        // given
        OrderTable table = fixture.toEntity();

        // when
        steps.createTable(table);
        OrderTable actual = sharedContext.getResponse().as(OrderTable.class);

        // then
        assertThat(actual.getId()).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
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

    @ParameterizedTest
    @ValueSource(ints = {-1, -100})
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

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L})
    void change_empty_table_group_exists(Long orderTableId) {
        // TODO: 10/23/23 테이블 그룹이 존재하는 경우를 위한 api 개설 후 테스트 작성
        // given
    }

    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL"})
    void change_empty_order_status_in(OrderStatus orderStatus) {
        // TODO: 10/23/23 주문 상태가 COOKING, MEAL 인 경우를 위한 api 개설 후 테스트 작성
        // given
    }
}
