package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static kitchenpos.fixture.FixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("단체 지정")
    @Test
    void create() {
        OrderTable savedOrderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable savedOrderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, asList(savedOrderTable1, savedOrderTable2), null);

        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(tableGroupRequest.getOrderTables().size()),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull()
        );
    }

    @DisplayName("주문 테이블이 2개 이상이 아닐 경우 예외 발생")
    @Test
    void create_exception1() {
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, singletonList(savedOrderTable), null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 예외 발생")
    @Test
    void create_exception2() {
        OrderTable notSavedOrderTable = createOrderTable(null, null, 0, true);
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, asList(savedOrderTable, notSavedOrderTable), null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 예외 발생")
    @Test
    void create_exception3() {
        OrderTable savedOrderTable = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable savedNotEmptyOrderTable = orderTableDao.save(createOrderTable(null, null, 0, false));
        TableGroup tableGroupRequest = createTableGroup(null, asList(savedOrderTable, savedNotEmptyOrderTable), null);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() {
        OrderTable savedOrderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable savedOrderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, asList(savedOrderTable1, savedOrderTable2), null);
        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        tableGroupService.ungroup(savedTableGroup.getId());

        List<OrderTable> ungroupedOrderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(ungroupedOrderTables).isEmpty();
    }

    @DisplayName("조리 또는 식사 상태인 테이블을 단체 지정 해제할 경우 예외 발생")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroup_exception1(OrderStatus status) {
        OrderTable savedOrderTable1 = orderTableDao.save(createOrderTable(null, null, 0, true));
        OrderTable savedOrderTable2 = orderTableDao.save(createOrderTable(null, null, 0, true));
        TableGroup tableGroupRequest = createTableGroup(null, asList(savedOrderTable1, savedOrderTable2), null);
        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);
        orderDao.save(createOrder(null, savedOrderTable1.getId(), status.name(), LocalDateTime.now(), null));
        orderDao.save(createOrder(null, savedOrderTable2.getId(), status.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}