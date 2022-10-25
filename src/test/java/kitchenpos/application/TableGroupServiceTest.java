package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("빈 테이블이 아닐 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void not_empty_table_exception() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("두 개 미만의 테이블일 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void not_enough_count_of_table_exception() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않을 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void order_table_not_found_exception() {
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(new OrderTable(), new OrderTable()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이미 단체로 지정되었을 때 단체로 지정하면, 예외를 발생한다")
    @Test
    void already_set_group_exception() {
        // given
        final TableGroup tableGroup1 = new TableGroup();
        tableGroup1.setCreatedDate(LocalDateTime.now());
        final TableGroup firstSavedTableGroup = tableGroupDao.save(tableGroup1);

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(firstSavedTableGroup.getId());
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup2 = new TableGroup();
        tableGroup2.setCreatedDate(LocalDateTime.now());
        tableGroup2.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 단체로 등록할 수 있다")
    @Test
    void create() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));

        // when
        final TableGroup createdGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(createdGroup.getId()).isNotNull();
    }

    @DisplayName("테이블 주문 상태가 COOKING 또는 MEAL일 때 단체를 해제하면, 예외를 발생한다")
    @ValueSource(strings = {"COOKING", "MEAL"})
    @ParameterizedTest
    void not_completion_exception(final String status) {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final Order order = new Order();
        order.setOrderStatus(status);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());
        orderDao.save(order);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체 지정을 해제한다")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);

        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable1.getId());
        orderDao.save(order);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(savedOrderTable1, savedOrderTable2));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }
}
