package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.GroupedTableCreateRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    protected TableGroupService tableGroupService;
    @Autowired
    protected OrderRepository orderRepository;
    @Autowired
    protected OrderTableRepository orderTableRepository;
    @Autowired
    protected TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("주문 테이블을 단체 지정한다")
    void create() {
        // given
        OrderTable createdOrderTable1 = createEmptyOrderTable();
        createdOrderTable1.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();
        createdOrderTable2.setEmpty(false);

        GroupedTableCreateRequest tableCreateRequest1 = new GroupedTableCreateRequest(createdOrderTable1.getId());
        GroupedTableCreateRequest tableCreateRequest2 = new GroupedTableCreateRequest(createdOrderTable2.getId());

        List<GroupedTableCreateRequest> orderTables = List.of(tableCreateRequest1, tableCreateRequest2);

        // when
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(orderTables);
        TableGroup createdTableGroup = tableGroupService.create(createRequest);
        OrderTable firstTable = createdTableGroup.getOrderTables().get(0);

        // then
        assertAll(
            () -> assertThat(createdTableGroup.getOrderTables()).hasSameSizeAs(orderTables),
            () -> assertThat(firstTable.getTableGroup()).isEqualTo(createdTableGroup),
            () -> assertThat(firstTable.getNumberOfGuests()).isEqualTo(createdOrderTable1.getNumberOfGuests())
        );
    }

    @Test
    @DisplayName("테이블의 목록을 설정하지 않고 단체 지정할 수 없다")
    void nonRegisteredOrderTables() {
        // given

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(new TableGroupCreateRequest(List.of())))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("2개 미만의 테이블로 단체 지정할 수 없다")
    void lessThan2Tables() {
        // given
        OrderTable createdOrderTable = createEmptyOrderTable();
        createdOrderTable.setEmpty(false);
        GroupedTableCreateRequest tableCreateRequest = new GroupedTableCreateRequest(createdOrderTable.getId());

        // when
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(List.of(tableCreateRequest));

        // then
        assertThatThrownBy(() -> tableGroupService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되지 않은 테이블을 포함해서 단체 지정할 수 없다")
    void includeNonRegisteredTable() {
        // given
        OrderTable orderTable = createEmptyOrderTable();
        orderTable.setEmpty(false);

        Long fakeOrderTableId = 999L;

        GroupedTableCreateRequest tableCreateRequest1 = new GroupedTableCreateRequest(fakeOrderTableId);
        GroupedTableCreateRequest tableCreateRequest2 = new GroupedTableCreateRequest(orderTable.getId());

        List<GroupedTableCreateRequest> orderTables = List.of(tableCreateRequest1, tableCreateRequest2);

        // when
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블을 포함해서 단체 지정할 수 없다")
    void includeEmptyTable() {
        // given
        OrderTable createdOrderTable1 = createEmptyOrderTable();
        createdOrderTable1.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();

        GroupedTableCreateRequest tableCreateRequest1 = new GroupedTableCreateRequest(createdOrderTable1.getId());
        GroupedTableCreateRequest tableCreateRequest2 = new GroupedTableCreateRequest(createdOrderTable2.getId());

        List<GroupedTableCreateRequest> orderTables = List.of(tableCreateRequest1, tableCreateRequest2);

        // when
        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(orderTables);

        // then
        assertThatThrownBy(() -> tableGroupService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 단체 지정된 테이블을 포함해서 단체 지정할 수 없다")
    void includeAlreadyGroupedTable() {
        // given
        OrderTable createdOrderTable1 = createEmptyOrderTable();
        createdOrderTable1.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();
        createdOrderTable2.setEmpty(false);

        GroupedTableCreateRequest tableCreateRequest1 = new GroupedTableCreateRequest(createdOrderTable1.getId());
        GroupedTableCreateRequest tableCreateRequest2 = new GroupedTableCreateRequest(createdOrderTable2.getId());

        List<GroupedTableCreateRequest> orderTables = List.of(tableCreateRequest1, tableCreateRequest2);

        // when
        tableGroupRepository.save(new TableGroup(List.of(createdOrderTable1, createdOrderTable2)));

        TableGroupCreateRequest createRequest = new TableGroupCreateRequest(orderTables);

        // then
        assertThatThrownBy(() -> tableGroupService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다")
    void ungroup() {
        // given
        OrderTable createdOrderTable1 = createEmptyOrderTable();
        createdOrderTable1.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();
        createdOrderTable2.setEmpty(false);

        TableGroup createdTableGroup = tableGroupRepository.save(
            new TableGroup(new ArrayList<>(List.of(createdOrderTable1, createdOrderTable2))));

        // when
        tableGroupService.ungroup(createdTableGroup.getId());

        // then
        TableGroup tableGroup = tableGroupRepository.findById(createdTableGroup.getId()).get();

        assertAll(
            () -> assertThat(tableGroup.getId()).isEqualTo(createdTableGroup.getId()),
            () -> assertThat(tableGroup.getOrderTables()).isEmpty(),
            () -> assertThat(
                tableGroup.getOrderTables().stream()
                    .map(OrderTable::getTableGroup)
            ).allMatch(Objects::isNull)
        );
    }

    @Test
    @DisplayName("완료되지 않은 상태의 테이블이 있으면 단체 지정을 해제할 수 없다")
    void includeInCompletedTable() {
        // given
        OrderTable createdOrderTable1 = createEmptyOrderTable();
        createdOrderTable1.setEmpty(false);
        OrderTable createdOrderTable2 = createEmptyOrderTable();
        createdOrderTable2.setEmpty(false);

        Order order = new Order(createdOrderTable1, List.of());
        order.updateStatus(OrderStatus.COOKING);
        orderRepository.save(order);

        TableGroup createdTableGroup = tableGroupRepository.save(
            new TableGroup(List.of(createdOrderTable1, createdOrderTable2)));

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(createdTableGroup.getId()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createEmptyOrderTable() {
        OrderTable orderTable = new OrderTable(99, true);
        return orderTableRepository.save(orderTable);
    }
}
