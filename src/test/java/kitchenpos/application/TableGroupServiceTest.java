package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 2개 이상의 중복 없이 존재, 비어있고, 그룹 지정 되지 않은 테이블 목록 그룹 지정시, 그룹 지정 후, 해당 객체를 반환한다.")
    @Test
    void create() {
        OrderTable firstSavedTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondSavedTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup tableGroupWithMultipleTable = createTableGroup(null, Lists.list(firstSavedTable, secondSavedTable));

        final TableGroup savedTableGroup = tableGroupService.create(tableGroupWithMultipleTable);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("create: 1개 테이블 그룹 지정시, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_count_is_smaller_than_minimum_length() {
        OrderTable firstSavedTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup tableGroupWithOneTable = createTableGroup(null, Lists.list(firstSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithOneTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블이 없는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_does_not_exist() {
        TableGroup tableGroupWithOutAnyTable = createTableGroup(null, Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithOutAnyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블이 null인 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_is_null() {
        TableGroup tableGroupWithTableIsNull = createTableGroup(null, null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithTableIsNull))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 중복인 테이블이 존재하는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_contains_duplicate_table() {
        OrderTable firstSavedTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup tableGroupContainsDuplicateTable = createTableGroup(null,
                Lists.list(firstSavedTable, firstSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupContainsDuplicateTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 존재하지 않는 테이블이 포함된 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_contains_non_exist_table() {
        OrderTable firstSavedTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable nonExistTable = createTable(null, 0, true);
        TableGroup tableGroupContainsInvalidTable = createTableGroup(null, Lists.list(firstSavedTable, nonExistTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupContainsInvalidTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 이미 점유중인 테이블이 포함된 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_contains_non_empty_table() {
        OrderTable emptyTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        TableGroup tableGroupContainsNonEmptyTable = createTableGroup(null, Lists.list(emptyTable, nonEmptyTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupContainsNonEmptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 이미 그룹 에 소속된 테이블 포함하는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_contains_already_grouped_table() {
        OrderTable firstEmptyTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondEmptyTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup firstTableGroup = createTableGroup(null, Lists.list(firstEmptyTable, secondEmptyTable));
        tableGroupService.create(firstTableGroup);

        OrderTable thirdEmptyTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup secondTableGroup = createTableGroup(null, Lists.list(firstEmptyTable, thirdEmptyTable));

        assertThatThrownBy(() -> tableGroupService.create(secondTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup: 입력 받은 테이블 그룹 id에 대해 그룹 해제 대상 테이블 모두 주문 완료 상태인 경우, 대상 테이블들의 그룹화를 해제한다.")
    @Test
    void ungroup() {
        OrderTable firstEmptyTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondEmptyTable = orderTableDao.save(createTable(null, 0, true));

        orderDao.save(
                createOrder(firstEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 20, 40), OrderStatus.COMPLETION,
                        null));
        orderDao.save(
                createOrder(secondEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 21, 44), OrderStatus.COMPLETION,
                        null));

        TableGroup createdTableGroup = tableGroupService.create(
                createTableGroup(null, Lists.list(firstEmptyTable, secondEmptyTable)));

        tableGroupService.ungroup(createdTableGroup.getId());
        assertAll(
                () -> assertThat(orderTableDao.findById(firstEmptyTable.getId())
                        .orElseThrow(IllegalArgumentException::new)
                        .getTableGroupId()).isNull(),
                () -> assertThat(orderTableDao.findById(secondEmptyTable.getId())
                        .orElseThrow(IllegalArgumentException::new)
                        .getTableGroupId()).isNull()
        );
    }

    @DisplayName("ungroup: 그룹 해제 대상 테이블 중 하나라도 주문 완료 상태가 아닌 경우, 그룹 해제 실패 및 IllegalArgumentException 발생.")
    @Test
    void ungroup_fail_if_contains_not_completion_order_status_table() {
        OrderTable firstEmptyTable = orderTableDao.save(createTable(null, 0, true));
        OrderTable secondEmptyTable = orderTableDao.save(createTable(null, 0, true));
        TableGroup createdTableGroup = tableGroupService.create(
                createTableGroup(null, Lists.list(firstEmptyTable, secondEmptyTable)));

        orderDao.save(
                createOrder(firstEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 20, 40), OrderStatus.MEAL, null));
        orderDao.save(
                createOrder(secondEmptyTable.getId(), LocalDateTime.of(2020, 10, 20, 21, 44), OrderStatus.COMPLETION,
                        null));

        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}