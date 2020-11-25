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
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.IllegalOrderStatusException;
import kitchenpos.table.domain.Table;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableRepository;
import kitchenpos.table.dto.TableGroupCreateRequest;
import kitchenpos.table.exception.IllegalTableStateException;
import kitchenpos.table.exception.InvalidTableQuantityException;
import kitchenpos.table.service.TableGroupService;

@SpringBootTest
@Sql(value = "/truncate.sql")
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableRepository tableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderRepository orderRepository;

    @DisplayName("주문 테이블이 비어있거나 1개일 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsEmptyOrOne() {
        TableGroupCreateRequest request1 = new TableGroupCreateRequest(Collections.emptySet());
        TableGroupCreateRequest request2 = new TableGroupCreateRequest(Sets.newSet(1L));

        assertAll(
            () -> assertThatThrownBy(() -> tableGroupService.create(request1))
                .isInstanceOf(InvalidTableQuantityException.class),

            () -> assertThatThrownBy(() -> tableGroupService.create(request2))
                .isInstanceOf(InvalidTableQuantityException.class)
        );
    }

    @DisplayName("주문 테이블이 중복되거나 없을 시 단체지정을 하면 예외를 발생한다.")
    @Test
    void createWhenOrderTableIsNullOrDuplicated() {
        Table table = createTable(null, true, null, 3);
        TableGroup tableGroup = createTableGroup(1L, LocalDateTime.now(), Arrays.asList(table, table));
        Table savedTable = tableRepository.save(table);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable.getId(), savedTable.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(InvalidTableQuantityException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹이 손님이 있는 테이블이면 Exception이 발생한다.")
    @Test
    void test() {
        Table table1 = createTable(null, true, null, 3);
        Table table2 = createTable(null, false, null, 3);

        Table savedTable1 = tableRepository.save(table1);
        Table savedTable2 = tableRepository.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(IllegalTableStateException.class);
    }

    @DisplayName("인자로 넘겨준 테이블그룹이 그룹이 있는 테이블이면 Exception이 발생한다.")
    @Test
    void test2() {
        Table saved1 = tableRepository.save(createTable(null, true, null, 3));
        Table saved2 = tableRepository.save(createTable(null, true, null, 3));
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(Arrays.asList(saved1, saved2)));

        Table table1 = createTable(null, true, savedTableGroup, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableRepository.save(table1);
        Table savedTable2 = tableRepository.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        assertThatThrownBy(
            () -> tableGroupService.create(request)
        ).isInstanceOf(IllegalTableStateException.class);
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void create() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableRepository.save(table);
        Table savedTable2 = tableRepository.save(table2);

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

        Table savedTable1 = tableRepository.save(table);
        Table savedTable2 = tableRepository.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        Long groupId = tableGroupService.create(request);

        Order order = createOrder(null, OrderStatus.MEAL, savedTable1);
        orderRepository.save(order);

        assertThatThrownBy(
            () -> tableGroupService.ungroup(groupId)
        ).isInstanceOf(IllegalOrderStatusException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        Table table = createTable(null, true, null, 3);
        Table table2 = createTable(null, true, null, 3);

        Table savedTable1 = tableRepository.save(table);
        Table savedTable2 = tableRepository.save(table2);

        TableGroupCreateRequest request = new TableGroupCreateRequest(
            Sets.newSet(savedTable1.getId(), savedTable2.getId()));

        Long groupId = tableGroupService.create(request);

        tableGroupService.ungroup(groupId);

        Table actual = tableRepository.findById(savedTable1.getId()).get();

        assertThat(actual.getTableGroup()).isNull();
    }
}
