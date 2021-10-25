package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundException;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("OrderTable 통합테스트")
class OrderTableIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/tables";

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final Map<String, Object> params = new HashMap<>();
        final int orderTableNumberOfGuests = 0;
        params.put("numberOfGuests", orderTableNumberOfGuests);
        final boolean isOrderTableEmpty = true;
        params.put("empty", isOrderTableEmpty);

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.tableGroupId").value(IsNull.nullValue()))
            .andExpect(jsonPath("$.numberOfGuests").value(orderTableNumberOfGuests))
            .andExpect(jsonPath("$.empty").value(isOrderTableEmpty))
        ;

        final List<OrderTable> foundOrderTables = orderTableDao.findAll();
        assertThat(foundOrderTables).hasSize(1);

        final OrderTable foundOrderTable = foundOrderTables.get(0);
        assertThat(foundOrderTable.getId()).isPositive();
        assertThat(foundOrderTable.getTableGroupId()).isNull();
        assertThat(foundOrderTable.getNumberOfGuests()).isZero();
        assertThat(foundOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("모든 OrderTable들 조회 - 성공")
    @Test
    void findAll_Success() throws Exception {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setTableGroupId(null);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setEmpty(true);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setTableGroupId(null);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(false);

        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        // when
        // then
        mockMvc.perform(get(API_PATH))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$[0].id").value(savedOrderTable1.getId()))
            .andExpect(jsonPath("$[0].tableGroupId").value(IsNull.nullValue()))
            .andExpect(jsonPath("$[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
            .andExpect(jsonPath("$[0].empty").value(orderTable1.isEmpty()))
            .andExpect(jsonPath("$[1].id").value(savedOrderTable2.getId()))
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
    void changeEmptyStatus_Success(OrderStatus orderStatus, boolean emptyBefore, boolean emptyToChange) throws Exception {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(emptyBefore);
        savedOrderTable.setTableGroupId(null);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        Order savedOrder = new Order();
        savedOrder.setOrderTableId(savedOrderTable.getId());
        savedOrder.setOrderStatus(orderStatus.name());
        savedOrder.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder);

        final Map<String, Object> params = new HashMap<>();
        params.put("empty", emptyToChange);

        // when
        // then
        mockMvc.perform(put(API_PATH + "/" + savedOrderTable.getId() + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(savedOrderTable.getId()))
            .andExpect(jsonPath("$.tableGroupId").value(savedOrderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests").value(savedOrderTable.getNumberOfGuests()))
            .andExpect(jsonPath("$.empty").value(emptyToChange))
        ;

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable.isEmpty()).isEqualTo(emptyToChange);
    }

    @DisplayName("empty 상태를 변경한다. - 실패 - OrderTable이 존재하지 않을 때")
    @Test
    void changeEmptyStatus_Fail_When_OrderTableNotExists() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("empty", true);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/0/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("empty 상태를 변경한다. - 실패 - TableGroupId가 null이 아닐 때")
    @Test
    void changeEmptyStatus_Fail_When_TableGroupIdIsNotNull() {
        // given
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(savedTableGroup);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setTableGroupId(savedTableGroup.getId());
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(true);
        savedOrderTable = orderTableDao.save(savedOrderTable);
        final Long savedOrderTableId = savedOrderTable.getId();

        final Map<String, Object> params = new HashMap<>();
        params.put("empty", true);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/" + savedOrderTableId + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTableId)
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));

        assertThat(foundOrderTable.isEmpty()).isEqualTo(savedOrderTable.isEmpty());
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
    void changeEmptyStatus_Fail_When_OrderStatusIsCooKingOrMeal(OrderStatus orderStatus, boolean beforeEmpty, boolean newEmpty) {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(beforeEmpty);
        savedOrderTable.setTableGroupId(null);
        savedOrderTable = orderTableDao.save(savedOrderTable);
        final Long savedOrderTableId = savedOrderTable.getId();

        Order savedOrder = new Order();
        savedOrder.setOrderTableId(savedOrderTable.getId());
        savedOrder.setOrderStatus(orderStatus.name());
        savedOrder.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder);

        final Map<String, Object> params = new HashMap<>();
        params.put("empty", newEmpty);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/" + savedOrderTableId + "/empty")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable.isEmpty()).isEqualTo(beforeEmpty);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 성공")
    @Test
    void changeNumberOfGuests_Success() throws Exception {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);

        final Map<String, Object> params = new HashMap<>();
        final int newNumberOfGuests = 1;
        params.put("numberOfGuests", newNumberOfGuests);

        // when
        // then
        mockMvc.perform(put(API_PATH + "/" + savedOrderTable.getId() + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isOk())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").value(savedOrderTable.getId()))
            .andExpect(jsonPath("$.tableGroupId").value(savedOrderTable.getTableGroupId()))
            .andExpect(jsonPath("$.numberOfGuests").value(newNumberOfGuests))
            .andExpect(jsonPath("$.empty").value(savedOrderTable.isEmpty()))
        ;

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - 새로운 numberOfGuests값이 0보다 작을 때")
    @Test
    void changeNumberOfGuests_Fail_When_NewNumberOfGuestsIsLessThanZero() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);
        savedOrderTable = orderTableDao.save(savedOrderTable);
        final Long savedOrderTableId = savedOrderTable.getId();

        final Map<String, Object> params = new HashMap<>();
        final int newNumberOfGuests = -1;
        params.put("numberOfGuests", newNumberOfGuests);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/" + savedOrderTableId + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - OrderTable이 존재하지 않을 때")
    @Test
    void changeNumberOfGuests_Fail_When_OrderTableNotExists() {
        // given
        final Map<String, Object> params = new HashMap<>();
        final int newNumberOfGuests = 1;
        params.put("numberOfGuests", newNumberOfGuests);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/0/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("numberOfGuests 값을 변경한다. - 실패 - DB에서 조회한 OrderTable의 empty값이 true일 때")
    @Test
    void changeNumberOfGuests_Fail_When_OrderTableEmptyIsTrue() {
        // given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(true);
        savedOrderTable = orderTableDao.save(savedOrderTable);
        final Long savedOrderTableId = savedOrderTable.getId();

        final Map<String, Object> params = new HashMap<>();
        final int newNumberOfGuests = 1;
        params.put("numberOfGuests", newNumberOfGuests);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(put(API_PATH + "/" + savedOrderTableId + "/number-of-guests")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final OrderTable foundOrderTable = orderTableDao.findById(savedOrderTable.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
    }
}
