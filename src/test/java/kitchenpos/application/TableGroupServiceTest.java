package kitchenpos.application;

import static kitchenpos.helper.EntityCreateHelper.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.service.TableGroupService;

@SpringBootTest
@Sql(value = "/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableDao tableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블이 비어있거나 1개일 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsEmptyOrOne() {
        TableGroupCreateRequest request1 = new TableGroupCreateRequest(Collections.emptySet());
        TableGroupCreateRequest request2 = new TableGroupCreateRequest(Sets.newSet(1L));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(request1))
                .isInstanceOf(IllegalArgumentException.class),

            () -> assertThatThrownBy(() -> tableGroupService.create(request2))
                .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("주문 테이블이 중복되거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated() {
        Table table = createTable(null, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(table, table));
        Table savedTable = tableDao.save(table);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable.getId(), savedTable.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹이 손님이 있는 테이블이면 Exception이 발생한다.")
    @Test
    void test() {
        Table table1 = createTable(null, true, null, 3);
        Table table2 = createTable(null, false, null, 3);

        Table savedTable1 = tableDao.save(table1);
        Table savedTable2 = tableDao.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹이 그룹이 있는 테이블이면 Exception이 발생한다.")
    @Test
    void test2() {
        TableGroup savedTableGroup = tableGroupDao.save(new TableGroup(1L, Collections.emptyList()));
        Table table1 = createTable(null, true, savedTableGroup.getId(), 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableDao.save(table1);
        Table savedTable2 = tableDao.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void create() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableDao.save(table);
        Table savedTable2 = tableDao.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        Long id = tableGroupService.create(request);

        assertThat(id).isNotNull();
    }

    @DisplayName("주문 테이블 중 주문이 안 들어가거나 계산 완료되지 않은 테이블이 있을 경우 단체지정 해제할 때 예외를 발생한다.")
    @Test
    void ungroupWhenNotStatusOrderCompletion() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableDao.save(table);
        Table savedTable2 = tableDao.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        Long groupId = tableGroupService.create(request);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.MEAL,
            savedTable1.getId());
        orderDao.save(order);

        assertThatThrownBy(
            () -> tableGroupService.ungroup(groupId)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableDao.save(table);
        Table savedTable2 = tableDao.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        Long groupId = tableGroupService.create(request);

        Order order = createOrder(null, LocalDateTime.now(), Collections.emptyList(), OrderStatus.COMPLETION,
            savedTable1.getId());
        orderDao.save(order);

        tableGroupService.ungroup(groupId);

        Table actual = tableDao.findById(savedTable1.getId()).get();

        assertThat(actual.getTableGroupId()).isNull();
    }
}
