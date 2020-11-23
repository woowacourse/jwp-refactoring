package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.TableGroupResponse;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;

class TableGroupServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("TableGroup 생성 실패 - OrderTable이 없을 경우")
    @Test
    void createFail_When_OrderTable_IsNotExist() {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 생성 실패 - OrderTable이 1개 이하인 경우")
    @Test
    void createFail_When_OrderTable_IsSingle() {
        OrderTable savedOrderTable = orderTableRepository.save(new OrderTable(3, true));
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.singletonList(savedOrderTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 생성 실패 - OrderTable이 중복되거나 주문 내역이 없는 테이블인 경우")
    @Test
    void createFail_When_OrderTable_Invalid_Size() {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable savedOrderTable1 = orderTableRepository.save(new OrderTable(3, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(new OrderTable(3, true));
        savedOrderTable1.groupBy(savedTableGroup);
        orderTableRepository.save(savedOrderTable1);

        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Lists.newArrayList(savedOrderTable1.getId(), savedOrderTable2.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 생성")
    @Test
    void create() {
        OrderTable savedOrderTable1 = orderTableRepository.save(new OrderTable(3, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(new OrderTable(3, true));

        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Lists.newArrayList(savedOrderTable1.getId(), savedOrderTable2.getId()));
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        assertAll(
            () -> assertThat(tableGroupResponse.getId()).isNotNull(),
            () -> assertThat(tableGroupResponse.getOrderTables()).hasSize(2)
        );

    }

    @DisplayName("TableGroup 해제 실패 - 요리 중이거나 식사 중인 경우")
    @ParameterizedTest
    @CsvSource(value = {"MEAL", "COOKING"})
    void ungroupFail_When_OrderStatus_CookingOrMeal(OrderStatus orderStatus) {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable savedOrderTable1 = orderTableRepository.save(new OrderTable(3, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(new OrderTable(3, true));
        savedOrderTable1.groupBy(savedTableGroup);
        savedOrderTable2.groupBy(savedTableGroup);
        orderTableRepository.saveAll(Arrays.asList(savedOrderTable1, savedOrderTable2));

        orderRepository.save(new Order(savedOrderTable1, orderStatus, LocalDateTime.now()));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 해제")
    @Test
    void upgroup() {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        OrderTable savedOrderTable1 = orderTableRepository.save(new OrderTable(3, true));
        OrderTable savedOrderTable2 = orderTableRepository.save(new OrderTable(3, true));
        savedOrderTable1.groupBy(savedTableGroup);
        savedOrderTable2.groupBy(savedTableGroup);
        orderTableRepository.saveAll(Arrays.asList(savedOrderTable1, savedOrderTable2));
        orderRepository.save(new Order(savedOrderTable1, OrderStatus.COMPLETION, LocalDateTime.now()));

        tableGroupService.ungroup(savedTableGroup.getId());

        List<OrderTable> foundOrderTables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(foundOrderTables).isEmpty();

    }
}

