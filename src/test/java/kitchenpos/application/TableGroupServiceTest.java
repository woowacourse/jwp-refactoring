package kitchenpos.application;

import static kitchenpos.db.TestDataFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableGroupServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("TableGroup 생성 실패 - OrderTable이 비어있거나 1개인 경우")
    @Test
    void createFail_When_OrderTable_IsEmpty() {
        OrderTable orderTable = createOrderTable(1L, 1L, 3, true);
        TableGroup emptyTableGroup = createTableGroup(1L, LocalDateTime.now(), Collections.emptyList());
        TableGroup singleTableGroup = createTableGroup(1L, LocalDateTime.now(), Collections.singletonList(orderTable));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> tableGroupService.create(singleTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("TableGroup 생성 실패 - OrderTable이 중복되거나 주문 내역이 없는 테이블인 경우")
    @Test
    void createFail_When_OrderTable_Invalid_Size() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable invalidOrderTable = createOrderTable(-1L, 1L, 3, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        TableGroup duplicateTableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedOrderTable, savedOrderTable));
        TableGroup invalidTableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(invalidOrderTable, orderTable));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(duplicateTableGroup))
                .isInstanceOf(IllegalArgumentException.class),
            () -> assertThatThrownBy(() -> tableGroupService.create(invalidTableGroup))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("TableGroup 생성")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable orderTable2 = createOrderTable(null, null, 4, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedOrderTable, savedOrderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2)
        );

    }

    @DisplayName("TableGroup 해제 실패 - 요리 중이거나 식사 중인 경우")
    @Test
    void ungroupFail_When_OrderStatus_CookingOrMeal() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable orderTable2 = createOrderTable(null, null, 4, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedOrderTable, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            Collections.emptyList());
        orderDao.save(order);

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("TableGroup 해제")
    @Test
    void upgroup() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable orderTable2 = createOrderTable(null, null, 4, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);
        OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedOrderTable, savedOrderTable2));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
            Collections.emptyList());
        Order savedOrder = orderDao.save(order);

        tableGroupService.ungroup(savedTableGroup.getId());

        assertThat(orderTableDao.findById(savedOrder.getId()).get().getTableGroupId()).isNull();
    }
}

