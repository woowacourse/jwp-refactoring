package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.config.CustomParameterizedTest;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.MediaType;

@DisplayName("TableGroup 통합테스트")
class TableGroupIntegrationTest extends IntegrationTest {

    private static final String API_PATH = "/api/table-groups";

    @DisplayName("생성 - 성공")
    @Test
    void create_Success() throws Exception {
        // given
        final OrderTable orderTable1 = OrderTable을_저장한다();
        final OrderTable orderTable2 = OrderTable을_저장한다();

        final TableGroupRequest tableGroupRequest = TableGroupRequest를_생성한다(
            orderTable1, orderTable2
        );

        // when
        // then
        mockMvc.perform(post(API_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(tableGroupRequest)))
            .andExpect(status().isCreated())
            .andExpect(header().exists(LOCATION))
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
            .andExpect(jsonPath("$.id").isNumber())
            .andExpect(jsonPath("$.createdDate").isNotEmpty())
            .andExpect(jsonPath("$.orderTables.length()").value(tableGroupRequest.getOrderTables().size()))
            .andExpect(jsonPath("$.orderTables[0].id").value(orderTable1.getId()))
            .andExpect(jsonPath("$.orderTables[0].tableGroupId").isNumber())
            .andExpect(jsonPath("$.orderTables[0].numberOfGuests").value(orderTable1.getNumberOfGuests()))
            .andExpect(jsonPath("$.orderTables[0].empty").value(orderTable1.isEmpty()))
            .andExpect(jsonPath("$.orderTables[1].id").value(orderTable2.getId()))
            .andExpect(jsonPath("$.orderTables[1].tableGroupId").isNumber())
            .andExpect(jsonPath("$.orderTables[1].numberOfGuests").value(orderTable2.getNumberOfGuests()))
            .andExpect(jsonPath("$.orderTables[1].empty").value(orderTable2.isEmpty()))
        ;

        final List<TableGroup> foundTableGroups = tableGroupRepository.findAll();
        assertThat(foundTableGroups).hasSize(1);

        final TableGroup foundTableGroup = foundTableGroups.get(0);
        assertThat(foundTableGroup.getCreatedDate()).isNotNull();

        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByTableGroupId(foundTableGroup.getId());
        assertThat(foundOrderTables).hasSize(tableGroupRequest.getOrderTables().size());
        assertThat(foundOrderTables).extracting("tableGroupId").doesNotContainNull();
        assertThat(foundOrderTables).extracting("empty").containsOnly(false);
    }

    @DisplayName("생성 - 실패 - TableGroupRequest의 OrderTablesRequests가 empty일 때")
    @Test
    void create_Fail_When_RequestOrderTablesIsEmpty() throws Exception {
        // given
        final TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.emptyList());

        // when
        // then
        POST_API를_요청하면_BadRequest를_응답한다(API_PATH, tableGroupRequest);

        Repository가_비어있다(tableGroupRepository);
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 size가 2보다 작을 때")
    @Test
    void create_Fail_When_RequestOrderTablesSizeLessThanTwo() throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다();
        final TableGroupRequest tableGroupRequest = TableGroupRequest를_생성한다(orderTable);

        // when
        // then
        POST_API를_요청하면_BadRequest를_응답한다(API_PATH, tableGroupRequest);

        Repository가_비어있다(tableGroupRepository);
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들중에 DB에 없는것이 있을 경우")
    @Test
    void create_Fail_When_RequestOrderTablesNotExistsInDB() throws Exception {
        // given
        final OrderTable savedOrderTable = OrderTable을_저장한다();
        final OrderTable notSavedOrderTable = new OrderTable(0L, null, 0, true);

        final TableGroupRequest tableGroupRequest = TableGroupRequest를_생성한다(savedOrderTable, notSavedOrderTable);

        // when
        // then
        POST_API를_요청하면_BadRequest를_응답한다(API_PATH, tableGroupRequest);

        Repository가_비어있다(tableGroupRepository);
    }

    @DisplayName("생성 - 실패 - 요청 매개변수 TableGroup의 OrderTable들의 empty값이 모두 true가 아닐 때")
    @Test
    void create_Fail_When_RequestOrderTablesEmptyIsNotNull() throws Exception {
        // given
        final OrderTable orderTable = OrderTable을_저장한다();
        final OrderTable emptyFalseOrderTable = OrderTable을_저장한다(null, 1, false);

        final TableGroupRequest tableGroupRequest = TableGroupRequest를_생성한다(orderTable, emptyFalseOrderTable);

        // when
        // then
        POST_API를_요청하면_BadRequest를_응답한다(API_PATH, tableGroupRequest);

        Repository가_비어있다(tableGroupRepository);
    }

    @DisplayName("생성 - 실패 - 요청 매개변수로 조회한 OrderTable중에 TableGroup이 있는 OrderTable이 존재하는 경우")
    @Test
    void create_Fail_When_RequestOrderTablesTableGroupIdIsNotNull() throws Exception {
        // given
        final TableGroup tableGroup = TableGroup을_저장한다();
        final OrderTable orderTableWithTableGroup = OrderTable을_저장한다(tableGroup, 0, true);
        final OrderTable orderTableWithoutTableGroup = OrderTable을_저장한다();

        final TableGroupRequest tableGroupRequest = TableGroupRequest를_생성한다(orderTableWithTableGroup, orderTableWithoutTableGroup);

        // when
        // then
        POST_API를_요청하면_BadRequest를_응답한다(API_PATH, tableGroupRequest);

        final List<TableGroup> foundTableGroups = tableGroupRepository.findAll();
        assertThat(foundTableGroups).hasSize(1);
    }

    @DisplayName("해제 - 성공")
    @Test
    void ungroup_Success() throws Exception {
        // given
        final TableGroup tableGroup = TableGroup을_저장한다();
        final OrderTable orderTable1 = OrderTable을_저장한다(tableGroup, 0, true);
        final OrderTable orderTable2 = OrderTable을_저장한다(tableGroup, 0, true);
        Order를_저장한다(orderTable1, OrderStatus.COMPLETION);
        Order를_저장한다(orderTable1, OrderStatus.COMPLETION);

        // when
        // then
        mockMvc.perform(delete(API_PATH + "/" + tableGroup.getId()))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$").doesNotExist())
        ;

        assertThat(tableGroupRepository.findById(tableGroup.getId())).isNotEmpty();
        OrderTableGroup가_해제된다(orderTable1.getId());
        OrderTableGroup가_해제된다(orderTable2.getId());
    }

    @DisplayName("해제 - 실패 - OrderTable에 해당하는 Order의 OrderStatus값이 COOKING또는 MEAL일 때")
    @CustomParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_Fail_When_OrderStatusIsCookingOrMeal(OrderStatus orderStatus) throws Exception {
        // given
        final TableGroup tableGroup = TableGroup을_저장한다();
        final OrderTable orderTable1 = 비어있지_않은_OrderTable을_저장한다(tableGroup);
        final OrderTable orderTable2 = 비어있지_않은_OrderTable을_저장한다(tableGroup);
        Order를_저장한다(orderTable1, orderStatus);
        Order를_저장한다(orderTable2, OrderStatus.COMPLETION);

        // when
        // then
        mockMvc.perform(delete(API_PATH + "/" + tableGroup.getId()))
            .andExpect(status().isBadRequest())
            .andExpect(header().string(CONTENT_TYPE_NAME, RESPONSE_CONTENT_TYPE))
        ;

        assertThat(tableGroupRepository.findById(tableGroup.getId())).isNotEmpty();

        final List<OrderTable> foundOrderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        assertThat(foundOrderTables).hasSize(2);
        assertThat(foundOrderTables).containsExactly(orderTable1, orderTable2);
    }

    private OrderTable 비어있지_않은_OrderTable을_저장한다(TableGroup tableGroup) {
        return OrderTable을_저장한다(tableGroup, 1, false);
    }

    private void OrderTableGroup가_해제된다(Long orderTableId) {
        final OrderTable foundOrderTable = findOrderTableById(orderTableId);
        assertThat(foundOrderTable.getTableGroup()).isNull();
        assertThat(foundOrderTable.isEmpty()).isFalse();
    }

    private OrderTable OrderTable을_저장한다() {
        return OrderTable을_저장한다(null, 0, true);
    }

    private TableGroupRequest TableGroupRequest를_생성한다(OrderTable... orderTables) {
        final List<OrderTableRequest> orderTableRequests = Stream.of(orderTables)
            .map(orderTable -> new OrderTableRequest(orderTable.getId()))
            .collect(Collectors.toList())
            ;
        return new TableGroupRequest(orderTableRequests);
    }

    private OrderTable findOrderTableById(Long id) {
        return orderTableRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("해당 id의 OrderTable이 존재하지 않습니다."));
    }
}
