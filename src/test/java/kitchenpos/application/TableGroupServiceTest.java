package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.TableGroupRepository;
import kitchenpos.domain.TableRepository;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.dto.TableResponse;

@ServiceTest
class TableGroupServiceTest {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 설정한다.")
    @Test
    void create() {
        OrderTable savedTable1 = tableRepository.save(new OrderTable(3, true));
        OrderTable savedTable2 = tableRepository.save(new OrderTable(2, true));

        TableGroupCreateRequest tableGroupCreateRequest = TableGroupCreateRequest.of(
            Arrays.asList(savedTable1.getId(), savedTable2.getId()));

        TableGroupResponse actual = tableGroupService.create(tableGroupCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(TableGroupResponse::getId).isNotNull(),
            () -> assertThat(actual).extracting(TableGroupResponse::getCreateDate).isNotNull(),
            () -> assertThat(actual).extracting(TableGroupResponse::getOrderTables,
                InstanceOfAssertFactories.list(TableResponse.class))
                .extracting(TableResponse::getTableGroupId)
                .containsOnly(actual.getId()),
            () -> assertThat(actual).extracting(TableGroupResponse::getOrderTables,
                InstanceOfAssertFactories.list(TableResponse.class))
                .extracting(TableResponse::isEmpty)
                .containsOnly(false)
        );
    }

    @DisplayName("테이블 그룹을 생성할 시 TableId가 존재하지 않을 경우 예외 처리한다.")
    @Test
    void createWithNotExistingTableId() {
        TableGroupCreateRequest tableGroupCreateRequest = TableGroupCreateRequest.of(Arrays.asList(1L, 2L));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroup tableGroup = tableGroupRepository.save(TableGroup.create());

        OrderTable savedTable1 = tableRepository.save(new OrderTable(1L, tableGroup.getId(), 2, false));
        OrderTable savedTable2 = tableRepository.save(new OrderTable(2L, tableGroup.getId(), 2, false));
        List<OrderTable> savedTables = Arrays.asList(savedTable1, savedTable2);

        tableGroupService.ungroup(tableGroup.getId());

        assertAll(
            () -> assertThat(tableRepository.findAllById(
                savedTables.stream().map(OrderTable::getId).collect(Collectors.toList()))).hasSize(savedTables.size()),
            () -> assertThat(
                tableRepository.findAllById(savedTables.stream().map(OrderTable::getId).collect(Collectors.toList())))
                .extracting(OrderTable::getTableGroupId).containsOnlyNulls()
        );
    }
}