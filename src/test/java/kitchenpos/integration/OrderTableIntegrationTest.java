package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.exception.InvalidStateException;
import kitchenpos.exception.NotFoundException;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;

@DisplayName("OrderTable 통합테스트")
class OrderTableIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/tables";

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final OrderTableRequest orderTableRequest = OrderTableRequest를_생성한다();

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.tableGroupId").value(IsNull.nullValue()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTableRequest.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTableRequest.getEmpty()))
        ;

        final List<OrderTable> foundOrderTables = orderTableRepository.findAll();
        assertThat(foundOrderTables).hasSize(1);

        final OrderTable foundOrderTable = foundOrderTables.get(0);
        assertThat(foundOrderTable.getTableGroup()).isNull();
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(foundOrderTable.isEmpty()).isEqualTo(orderTableRequest.getEmpty());
    }

    @DisplayName("모든 OrderTable들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final OrderTable orderTable1 = OrderTable을_저장한다(null, 1, true);
        final OrderTable orderTable2 = OrderTable을_저장한다(null, 2, false);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").value(orderTable1.getId()))
            .andExpect(jsonPath("$[0].tableGroupId").value(IsNull.nullValue()))
            .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
            .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()))
            .andExpect(jsonPath("$[1].id").value(orderTable2.getId()))
            .andExpect(jsonPath("$[1].tableGroupId").value(IsNull.nullValue()))
            .andExpect(jsonPath("$[1].numberOfGuests").value(orderTable2.getNumberOfGuests()))
            .andExpect(jsonPath("$[1].empty").value(orderTable2.isEmpty()))
        ;
    }

    static Stream<Arguments> changeEmptyStatus_Success() {
        return Stream.of(
            Arguments.of(OrderStatus.COMPLETION.name(), false, true),
            Arguments.of(OrderStatus.COMPLETION.name(), false, false),
            Arguments.of(OrderStatus.COMPLETION.name(), true, false),
            Arguments.of(OrderStatus.COMPLETION.name(), true, true)
        );
    }

    @DisplayName("empty 상태를 변경한다. - 성공")
    @CustomParameterizedTest
    @MethodSource
    void changeEmptyStatus_Success(OrderStatus orderStatus, boolean beforeEmpty, boolean afterEmpty) throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 0, beforeEmpty);
        Order를_저장한다(orderTable, orderStatus);

        final OrderTableRequest orderTableRequest = new OrderTableRequest(afterEmpty);

        // when
        // then
        mockMvc.perform(put(API_PATH + "/" + orderTable.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableRequest)))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath("$.tableGroupId").value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(afterEmpty))
        ;

        DB에_저장되어있는_OrderTable의_empty값_검증(orderTable, afterEmpty);
    }

    @DisplayName("empty 상태를 변경한다. - 실패 - OrderTable이 존재하지 않을 때")
    @Test
    void changeEmptyStatus_Fail_When_OrderTableNotExists() throws Exception {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(true);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/0/empty", orderTableRequest);
    }

    @DisplayName("empty 상태를 변경한다. - 실패 - TableGroup이 null이 아닐 때")
    @Test
    void changeEmptyStatus_Fail_When_TableGroupIdIsNotNull() throws Exception {
        // given
        final TableGroup tableGroup = TableGroup을_저장한다();
        final boolean beforeEmpty = true;
        final OrderTable orderTable = OrderTable을_저장한다(tableGroup, 0, beforeEmpty);

        final OrderTableRequest orderTableRequest = new OrderTableRequest(false);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/0/empty", orderTableRequest);
        DB에_저장되어있는_OrderTable의_empty값_검증(orderTable, beforeEmpty);
    }

    static Stream<Arguments> changeEmptyStatus_Fail_When_OrderStatusIsCooKingOrMeal() {
        return Stream.of(
            Arguments.of(OrderStatus.COOKING.name(), false, true),
            Arguments.of(OrderStatus.COOKING.name(), false, false),
            Arguments.of(OrderStatus.COOKING.name(), true, false),
            Arguments.of(OrderStatus.COOKING.name(), true, true),
            Arguments.of(OrderStatus.MEAL.name(), false, true),
            Arguments.of(OrderStatus.MEAL.name(), false, false),
            Arguments.of(OrderStatus.MEAL.name(), true, false),
            Arguments.of(OrderStatus.MEAL.name(), true, true)
        );
    }

    @DisplayName("OrderTable의 empty 상태를 변경한다. - 실패 - OrderTable의 Order의 OrderStatus가 COOKING 또는 MEAL일 때")
    @CustomParameterizedTest
    @MethodSource
    void changeEmptyStatus_Fail_When_OrderStatusIsCooKingOrMeal(OrderStatus orderStatus, boolean beforeEmpty, boolean newEmpty) throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 0, beforeEmpty);
        Order를_저장한다(orderTable, orderStatus);

        final OrderTableRequest orderTableRequest = new OrderTableRequest(newEmpty);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/" + orderTable.getId() + "/empty", orderTableRequest);

        DB에_저장되어있는_OrderTable의_empty값_검증(orderTable, beforeEmpty);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 성공")
    @Test
    void changeNumberOfGuests_Success() throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 0, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1);

        // when
        // then
        mockMvc.perform(put(API_PATH + "/" + orderTable.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(orderTableRequest)))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(orderTable.getId()))
            .andExpect(jsonPath("$.tableGroupId").value(orderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTableRequest.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(orderTable.isEmpty()))
        ;

        final OrderTable foundOrderTable = findOrderTableById(orderTable.getId());
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - 새로운 numberOfGuests값이 0보다 작을 때")
    @Test
    void changeNumberOfGuests_Fail_When_NewNumberOfGuestsIsLessThanZero() throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 0, false);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(-1);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/" + orderTable.getId() + "/number-of-guests", orderTableRequest);

        DB에_있는_OrderTable의_numberOfGuests값_검증(orderTable);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - OrderTable이 존재하지 않을 때")
    @Test
    void changeNumberOfGuests_Fail_When_OrderTableNotExists() throws Exception {
        // given
        final OrderTableRequest orderTableRequest = new OrderTableRequest(2);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/0/number-of-guests", orderTableRequest);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - DB에서 조회한 OrderTable의 empty값이 true일 때")
    @Test
    void changeNumberOfGuests_Fail_When_OrderTableEmptyIsTrue() throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 0, true);
        final OrderTableRequest orderTableRequest = new OrderTableRequest(1);

        // when
        // then
        PUT_API를_요청하면_BadRequest를_응답한다(API_PATH + "/" + orderTable.getId() + "/number-of-guests", orderTableRequest);

        DB에_있는_OrderTable의_numberOfGuests값_검증(orderTable);
    }

    @DisplayName("OrderTable에 TableGroup이 없을 때 예외발생하지 않음")
    @Test
    void validateTableGroupIsNull() {
        // given
        final OrderTable orderTable = OrderTable을_저장한다(null, 1, false);
        resetEntityManager();

        // when
        final OrderTable foundOrderTable = findOrderTableById(orderTable.getId());

        // then
        assertThatCode(foundOrderTable::validateTableGroupIsNull)
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderTable에 TableGroup이 있을 때 예외발생")
    @Test
    void validateTableGroupIsNull_ThrowException_When_TableGroupExists() {
        // given
        final TableGroup tableGroup = TableGroup을_저장한다();
        final OrderTable orderTable = OrderTable을_저장한다(tableGroup, 1, false);
        resetEntityManager();

        // when
        final OrderTable foundOrderTable = findOrderTableById(orderTable.getId());

        // then
        assertThatThrownBy(foundOrderTable::validateTableGroupIsNull)
            .isInstanceOf(InvalidStateException.class);
    }

    private OrderTableRequest OrderTableRequest를_생성한다() {
        return new OrderTableRequest(0, true);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
    }

    private void DB에_저장되어있는_OrderTable의_empty값_검증(OrderTable orderTable, boolean empty) {
        final OrderTable foundOrderTable = findOrderTableById(orderTable.getId());
        assertThat(foundOrderTable.isEmpty()).isEqualTo(empty);
    }

    private void DB에_있는_OrderTable의_numberOfGuests값_검증(OrderTable orderTable) {
        final OrderTable foundOrderTable = findOrderTableById(orderTable.getId());
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
