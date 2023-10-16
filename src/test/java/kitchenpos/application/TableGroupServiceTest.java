package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.Fixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TableGroupServiceTest extends ServiceBaseTest {

    @Autowired
    protected TableGroupService tableGroupService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;
    private OrderTable nonEmptyOrderTable1;
    private OrderTable nonEmptyOrderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = Fixture.orderTable(null, 10, true);
        orderTable2 = Fixture.orderTable(null, 5, true);

        nonEmptyOrderTable1 = Fixture.orderTable(null, 10, false);
        nonEmptyOrderTable2 = Fixture.orderTable(null, 5, false);
    }

    @Test
    @DisplayName("테이블 단체 지정을 할 수 있다.")
    void create() {
        //given
        final OrderTable savedOrderTable = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTabel2 = orderTableDao.save(orderTable2);
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable, savedOrderTabel2));

        //when
        TableGroup createdTableGroup = tableGroupService.create(tableGroup);
        final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(savedOrderTable.getId(), savedOrderTabel2.getId()));

        //then
        assertAll(
                () -> assertThat(createdTableGroup.getId()).isNotNull(),
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 지정은 2개 이상이어야 한다.")
    void createValidTableNum() {
        //given
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1));

        //whdn&then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정 테이블은 모두 존재하여야 한다.")
    void createValidTable() {
        //given
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

        //whnen&then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("지정 테이블은 비어있어야 한다.")
    void createValidTableEmpty() {
        //given
        final OrderTable savedOrderTable1 = orderTableDao.save(nonEmptyOrderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(nonEmptyOrderTable2);
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2));

        //when&then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 지정된 테이블은 단체 지정할 수 없다.")
    void createValidAlreadyGroupTable() {
        //given
        final OrderTable savedOrderTable1 = orderTableDao.save(Fixture.orderTable(null, 10, true));
        final OrderTable savedOrderTable2 = orderTableDao.save(Fixture.orderTable(null, 5, true));
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2));
        tableGroupService.create(tableGroup);

        //when&then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 단체 지정을 해제할수 있다.")
    void ungroup() {
        //given
        final OrderTable savedOrderTable1 = orderTableDao.save(Fixture.orderTable(null, 10, true));
        final OrderTable savedOrderTable2 = orderTableDao.save(Fixture.orderTable(null, 5, true));
        final TableGroup tableGroup = Fixture.orderTableGroup(LocalDateTime.now(), List.of(savedOrderTable1, savedOrderTable2));
        final TableGroup createdTableGroup = tableGroupService.create(tableGroup);

        //when&then
        assertAll(
                () -> assertDoesNotThrow(() -> tableGroupService.ungroup(createdTableGroup.getId())),
                () -> assertThat(orderTableDao.findAllByTableGroupId(createdTableGroup.getId())).isEmpty()
        );
    }

    @ParameterizedTest(name = "테이블 단체 지정을 해제시 상태가 요리, 식사이면 안된다.")
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void ungroupValidTable(final OrderStatus orderStatus) {
        //given
        final OrderTable savedOrderTable1 = orderTableDao.save(orderTable1);
        final OrderTable savedOrderTable2 = orderTableDao.save(orderTable2);
        final TableGroup savedTableGroup = tableGroupService.create(Fixture.orderTableGroup(LocalDateTime.now(),
                List.of(savedOrderTable1, savedOrderTable2)));
        final Order order = Fixture.order(null, savedOrderTable1.getId(), LocalDateTime.now(), null);
        order.setOrderStatus(orderStatus.name());
        orderDao.save(order);

        //when&then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
