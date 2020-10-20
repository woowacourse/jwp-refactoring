package kitchenpos.application;

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
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("create: 2개 이상의 중복 없이 존재, 비어있고, 그룹 지정 되지 않은 테이블 목록 그룹 지정시, 그룹 지정 후, 해당 객체를 반환한다.")
    @Test
    void create() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        final OrderTable secondSavedTable = orderTableDao.save(secondTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, secondSavedTable));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("create: 1개 테이블 그룹 지정시, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_count_is_smaller_than_minimum_length() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블이 없는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_does_not_exist() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블이 null인 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_is_null() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 중복인 테이블이 존재하는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_contains_duplicate_table() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, firstSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 존재하지 않는 테이블이 포함된 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_target_table_contains_non_exist_table() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final OrderTable nonExistTable = new OrderTable();
        nonExistTable.setEmpty(true);
        nonExistTable.setNumberOfGuests(0);
        nonExistTable.setTableGroupId(null);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, nonExistTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 이미 점유중인 테이블이 포함된 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_contains_non_empty_table() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(false);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        final OrderTable secondSavedTable = orderTableDao.save(secondTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, secondSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 그룹 지정 대상 테이블 들 중 이미 그룹 에 소속된 테이블 포함하는 경우, 그룹 지정 실패, IllegalArgumentException 발생한다.")
    @Test
    void create_fail_if_contains_already_grouped_table() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        final OrderTable secondSavedTable = orderTableDao.save(secondTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, secondSavedTable));
        tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 10, 20, 40));
        tableGroupDao.save(tableGroup);

        final OrderTable thirdTable = new OrderTable();
        thirdTable.setEmpty(false);
        thirdTable.setNumberOfGuests(0);
        thirdTable.setTableGroupId(null);
        final OrderTable thirdSavedTable = orderTableDao.save(thirdTable);

        final TableGroup secondTableGroup = new TableGroup();
        secondTableGroup.setOrderTables(Lists.list(secondSavedTable, thirdSavedTable));

        assertThatThrownBy(() -> tableGroupService.create(secondTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup: 입력 받은 테이블 그룹 id에 대해 그룹 해제 대상 테이블 모두 주문 완료 상태인 경우, 대상 테이블들의 그룹화를 해제한다.")
    @Test
    void ungroup() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable orderTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        final OrderTable orderTable2 = orderTableDao.save(secondTable);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderedTime(LocalDateTime.of(2020, 10, 20, 20, 40));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        final Order order2 = new Order();
        order2.setOrderTableId(orderTable2.getId());
        order2.setOrderLineItems(Collections.singletonList(orderLineItem));
        order2.setOrderedTime(LocalDateTime.of(2020, 10, 20, 21, 44));
        order2.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order2);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(orderTable, orderTable2));
        tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 10, 20, 40));
        final TableGroup createdTableGroup = tableGroupDao.save(tableGroup);
        final Long savedTableGroupId = createdTableGroup.getId();

        tableGroupService.ungroup(savedTableGroupId);
        assertAll(
                () -> assertThat(orderTableDao.findById(orderTable.getId()).orElseThrow(IllegalArgumentException::new).getTableGroupId()).isNull(),
                () -> assertThat(orderTableDao.findById(orderTable2.getId()).orElseThrow(IllegalArgumentException::new).getTableGroupId()).isNull()
        );
    }

    @DisplayName("ungroup: 그룹 해제 대상 테이블 중 하나라도 주문 완료 상태가 아닌 경우, 그룹 해제 실패 및 IllegalArgumentException 발생.")
    @Test
    void ungroup_fail_if_contains_not_completion_order_status_table() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.of(2020, 10, 10, 20, 40));
        final TableGroup createdTableGroup = tableGroupDao.save(tableGroup);
        final Long savedTableGroupId = createdTableGroup.getId();

        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        firstTable.setTableGroupId(savedTableGroupId);
        final OrderTable orderTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        secondTable.setTableGroupId(savedTableGroupId);
        final OrderTable orderTable2 = orderTableDao.save(secondTable);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(3);

        final Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        order.setOrderedTime(LocalDateTime.of(2020, 10, 20, 20, 40));
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        orderDao.save(order);

        final Order order2 = new Order();
        order2.setOrderTableId(orderTable2.getId());
        order2.setOrderLineItems(Collections.singletonList(orderLineItem));
        order2.setOrderedTime(LocalDateTime.of(2020, 10, 20, 21, 44));
        order2.setOrderStatus(OrderStatus.COOKING.name());
        orderDao.save(order2);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}