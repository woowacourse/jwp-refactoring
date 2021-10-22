package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@DisplayName("TableGroup 통합테스트")
class TableGroupIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/table-groups";

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(null);
        savedOrderTable1.setEmpty(true);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setTableGroupId(null);
        savedOrderTable2.setEmpty(true);
        savedOrderTable2 = orderTableDao.save(savedOrderTable2);

        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", savedOrderTables);

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.createdDate").isNotEmpty())
            .andExpect(jsonPath("$.orderTables.length()").value(savedOrderTables.size()))
            .andExpect(jsonPath("$.orderTables[0].id").value(savedOrderTable1.getId()))
            .andExpect(jsonPath("$.orderTables[0].tableGroupId").isNumber())
            .andExpect(jsonPath("$.orderTables[0].numberOfGuests").value(savedOrderTable1.getNumberOfGuests()))
            .andExpect(jsonPath("$.orderTables[0].empty").value(!savedOrderTable1.isEmpty()))
            .andExpect(jsonPath("$.orderTables[1].id").value(savedOrderTable2.getId()))
            .andExpect(jsonPath("$.orderTables[1].tableGroupId").isNumber())
            .andExpect(jsonPath("$.orderTables[1].numberOfGuests").value(savedOrderTable2.getNumberOfGuests()))
            .andExpect(jsonPath("$.orderTables[1].empty").value(!savedOrderTable2.isEmpty()))
        ;

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).hasSize(1);

        final TableGroup foundTableGroup = foundTableGroups.get(0);
        assertThat(foundTableGroup.getCreatedDate()).isNotNull();

        final List<OrderTable> foundOrderTables = orderTableDao.findAllByTableGroupId(foundTableGroup.getId());
        assertThat(foundOrderTables).hasSize(savedOrderTables.size());
        assertThat(foundOrderTables).extracting("tableGroupId").containsExactly(foundTableGroup.getId(), foundTableGroup.getId());
        assertThat(foundOrderTables).extracting("empty").containsExactly(false, false);
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 컬렉션이 null일 때")
    @Test
    void create_Fail_When_RequestOrderTablesIsNull() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", null);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 size가 2보다 작을 때")
    @Test
    void create_Fail_When_RequestOrderTablesSizeLessThanTwo() {
        // given
        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(null);
        savedOrderTable1.setEmpty(true);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        final List<OrderTable> savedOrderTables = Collections.singletonList(savedOrderTable1);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", savedOrderTables);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들이 모두 DB에 존재하지 않을 때")
    @Test
    void create_Fail_When_RequestOrderTablesNotExistsInDB() {
        // given
        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(null);
        savedOrderTable1.setEmpty(true);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setId(0L);
        savedOrderTable2.setTableGroupId(null);
        savedOrderTable2.setEmpty(true);

        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", savedOrderTables);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 empty값이 모두 true가 아닐 때")
    @Test
    void create_Fail_When_RequestOrderTablesEmptyIsNotNull() {
        // given
        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(null);
        savedOrderTable1.setEmpty(true);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setTableGroupId(null);
        savedOrderTable2.setEmpty(false);
        savedOrderTable2 = orderTableDao.save(savedOrderTable2);

        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", savedOrderTables);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).isEmpty();
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 TableGroupId값이 모두 null이 아닐 때")
    @Test
    void create_Fail_When_RequestOrderTablesTableGroupIdIsNotNull() {
        // given
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(savedTableGroup);

        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(savedTableGroup.getId());
        savedOrderTable1.setEmpty(true);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setTableGroupId(null);
        savedOrderTable2.setEmpty(true);
        savedOrderTable2 = orderTableDao.save(savedOrderTable2);

        final List<OrderTable> savedOrderTables = Arrays.asList(savedOrderTable1, savedOrderTable2);

        final Map<String, Object> params = new HashMap<>();
        params.put("orderTables", savedOrderTables);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(params)))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).hasSize(1);
    }

    @DisplayName("해제 - 성공")
    @Test
    void ungroup_Success() throws Exception {
        // given
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(savedTableGroup);

        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(savedTableGroup.getId());
        savedOrderTable1.setEmpty(false);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setTableGroupId(savedTableGroup.getId());
        savedOrderTable2.setEmpty(false);
        savedOrderTable2 = orderTableDao.save(savedOrderTable2);

        Order savedOrder1 = new Order();
        savedOrder1.setOrderTableId(savedOrderTable1.getId());
        savedOrder1.setOrderStatus(OrderStatus.COMPLETION.name());
        savedOrder1.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder1);

        Order savedOrder2 = new Order();
        savedOrder2.setOrderTableId(savedOrderTable2.getId());
        savedOrder2.setOrderStatus(OrderStatus.COMPLETION.name());
        savedOrder2.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder2);

        // when
        // then
        mockMvc.perform(delete(API_PATH + "/" + savedTableGroup.getId()))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$").doesNotExist())
        ;

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isNotEmpty();

        final OrderTable foundOrderTable1 = orderTableDao.findById(savedOrderTable1.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable1.getTableGroupId()).isNull();
        assertThat(foundOrderTable1.isEmpty()).isFalse();

        final OrderTable foundOrderTable2 = orderTableDao.findById(savedOrderTable2.getId())
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
        assertThat(foundOrderTable2.getTableGroupId()).isNull();
        assertThat(foundOrderTable2.isEmpty()).isFalse();
    }

    @DisplayName("해제 - 실패 - OrderTable에 해당하는 Order의 OrderStatus값이 COOKING또는 MEAL일 때")
    @CustomParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_Fail_When_OrderStatusIsCookingOrMeal(OrderStatus orderStatus) {
        // given
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup = tableGroupDao.save(savedTableGroup);
        final Long savedTableGroupId = savedTableGroup.getId();

        OrderTable savedOrderTable1 = new OrderTable();
        savedOrderTable1.setTableGroupId(savedTableGroup.getId());
        savedOrderTable1.setEmpty(false);
        savedOrderTable1 = orderTableDao.save(savedOrderTable1);

        OrderTable savedOrderTable2 = new OrderTable();
        savedOrderTable2.setTableGroupId(savedTableGroup.getId());
        savedOrderTable2.setEmpty(false);
        savedOrderTable2 = orderTableDao.save(savedOrderTable2);

        Order savedOrder1 = new Order();
        savedOrder1.setOrderTableId(savedOrderTable1.getId());
        savedOrder1.setOrderStatus(orderStatus.name());
        savedOrder1.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder1);

        Order savedOrder2 = new Order();
        savedOrder2.setOrderTableId(savedOrderTable2.getId());
        savedOrder2.setOrderStatus(OrderStatus.COMPLETION.name());
        savedOrder2.setOrderedTime(LocalDateTime.now());
        orderDao.save(savedOrder2);

        // when
        // then
        assertThatThrownBy(() ->
            mockMvc.perform(delete(API_PATH + "/" + savedTableGroupId))
        ).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);

        final List<TableGroup> foundTableGroups = tableGroupDao.findAll();
        assertThat(foundTableGroups).hasSize(1);

        final TableGroup foundTableGroup = foundTableGroups.get(0);
        assertThat(foundTableGroup.getCreatedDate()).isNotNull();

        final List<OrderTable> foundOrderTables = orderTableDao.findAllByTableGroupId(foundTableGroup.getId());
        assertThat(foundOrderTables).hasSize(2);
        assertThat(foundOrderTables).extracting("tableGroupId").containsExactly(foundTableGroup.getId(), foundTableGroup.getId());
        assertThat(foundOrderTables).extracting("empty").containsExactly(false, false);
    }
}
