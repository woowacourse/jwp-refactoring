package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.Table;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블이 비어있거나 1개일 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsEmptyOrOne() {
        Table table = createTable(1L, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Collections.emptyList());
        TableGroup tableGroup2 = createTableGroup(1L, LocalDateTime.now(), Collections.singletonList(table));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("주문 테이블이 중복되거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated() {
        //todo
        Table table = createTable(null, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(table, table));

        orderTableDao.save(table);

        assertThatThrownBy(
            () -> tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹의 주문 테이블이 비어있거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated2() {
        Table tableWhichIsEmpty = createTable(1L, true, null, 3);
        TableGroup tableGroupWithTableWhichIsEmpty = createTableGroup(1L, LocalDateTime.now(),
            Arrays.asList(tableWhichIsEmpty));
        Table tableWithNoGroupId = createTable(1L, true, null, 3);
        TableGroup tableGroupWithTableWithoutTableGroupId = createTableGroup(1L, LocalDateTime.now(),
            Arrays.asList(tableWithNoGroupId));
        assertAll(
            () -> assertThatThrownBy(
                () -> tableGroupService.create(tableGroupWithTableWhichIsEmpty)
            ).isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(
                () -> tableGroupService.create(tableGroupWithTableWithoutTableGroupId)
            ).isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void create() {
        //todo
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = orderTableDao.save(table);
        Table savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
            () -> assertThat(savedTableGroup.getId()).isNotNull(),
            () -> assertThat(savedTableGroup.getTables()).hasSize(2)
        );
    }

    @DisplayName("주문 테이블 중 주문이 안 들어가거나 계산 완료되지 않은 테이블이 있을 경우 단체지정 해제할 때 예외를 발생한다.")
    @Test
    void ungroupWhenNotStatusOrderCompletion() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = orderTableDao.save(table);
        Table savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.MEAL,
            savedTable1.getId());
        orderDao.save(order);

        assertThatThrownBy(
            () -> tableGroupService.ungroup(savedTableGroup.getId())
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = orderTableDao.save(table);
        Table savedTable2 = orderTableDao.save(table2);

        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(),
            Arrays.asList(savedTable1, savedTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COMPLETION,
            savedTable1.getId());
        orderDao.save(order);

        tableGroupService.ungroup(savedTableGroup.getId());

        Table actual = orderTableDao.findById(savedTable1.getId()).get();

        assertThat(actual.getTableGroupId()).isNull();
    }
}
