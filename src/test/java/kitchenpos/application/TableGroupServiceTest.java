package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest(classes = {
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateTableGroupDao.class,
        TableGroupService.class
})
class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;

    private OrderTable orderTable2;

    private OrderTable notEmptyOrderTable;

    private OrderTable orderTableHasTableGroupId;

    @BeforeEach
    void setUp() {
        orderTable1 = orderTableDao.save(createOrderTable(null, 0, true));
        orderTable2 = orderTableDao.save(createOrderTable(null, 0, true));
        notEmptyOrderTable = orderTableDao.save(createOrderTable(null, 2, false));

        final TableGroup tableGroup = tableGroupDao.save(createTableGroup(Arrays.asList(orderTable1, orderTable2)));
        orderTableHasTableGroupId = orderTableDao.save(createOrderTable(tableGroup.getId(), 0, true));
    }

    @DisplayName("create: 테이블 그룹 생성")
    @Test
    void create() {
        final TableGroup tableGroup = createTableGroup(Arrays.asList(orderTable1, orderTable2));
        final TableGroup actual = tableGroupService.create(tableGroup);

        assertThat(actual).isNotNull();
        assertThat(actual.getOrderTables()).isNotEmpty();
        assertThat(actual.getOrderTables()).hasSize(2);
    }

    @DisplayName("create: 주문 테이블이 빈 목록일 때 예외 처리")
    @Test
    void create_IfOrderTablesIsEmpty_Exception() {
        final TableGroup tableGroup = createTableGroup(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 2개 미만일 때 예외 처리")
    @Test
    void create_IfOrderTablesIsLessThanTwo_Exception() {
        final TableGroup tableGroup = createTableGroup(Collections.singletonList(orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 사이즈와 조회 사이즈가 같지 않을 때 예외 처리")
    @Test
    void create_IfOrderTablesNotSameFindOrderTables_Exception() {
        orderTable1.setId(0L);
        final TableGroup tableGroup = createTableGroup(Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블이 비어 있을 때(empty=true) 예외 처리")
    @Test
    void create_IfOrderTableIsEmpty_Exception() {
        final TableGroup tableGroup = createTableGroup(Arrays.asList(notEmptyOrderTable, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 주문 테이블에 주문 그룹이 존재할 때 예외 처리")
    @Test
    void create_IfTableGroupExistInOrderTable_Exception() {
        final TableGroup tableGroup = createTableGroup(Arrays.asList(orderTableHasTableGroupId, orderTable2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("ungroup: 그룹 해제")
    @Test
    void ungroup() {
        final TableGroup tableGroup = tableGroupService.create(createTableGroup(Arrays.asList(orderTable1, orderTable2)));

        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @DisplayName("ungroup: 주문이 완료되지 않았을 때 예외 처리")
    @Test
    void ungroup_IfOrderStatusIsNotCompletion_Exception() {
        final TableGroup tableGroup = tableGroupService.create(createTableGroup(Arrays.asList(orderTable1, orderTable2)));
        orderDao.save(createOrder(orderTable1.getId(), "MEAL", Collections.emptyList()));

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}