package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.domain.entity.Order;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.entity.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.ui.dto.tablegroup.TableGroupCreateRequest;
import kitchenpos.table.ui.dto.tablegroup.TableGroupCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable emptyOrderTable1;
    private OrderTable emptyOrderTable2;
    private OrderTable notEmptyOrderTable;

    @BeforeEach
    void setUp() {
        emptyOrderTable1 = new OrderTable(10, true);
        orderTableRepository.save(emptyOrderTable1);

        emptyOrderTable2 = new OrderTable(10, true);
        orderTableRepository.save(emptyOrderTable2);

        notEmptyOrderTable = new OrderTable(10, false);
        orderTableRepository.save(notEmptyOrderTable);
    }

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(emptyOrderTable1.getId(),
                emptyOrderTable2.getId()));

        TableGroupCreateResponse tableGroupCreateResponse = tableGroupService.create(tableGroupCreateRequest);

        assertThat(tableGroupCreateResponse.getId()).isNotNull();
    }

    @DisplayName("테이블 그룹엔 테이블이 1개 이하일 수 없다.")
    @Test
    void create_Exception_Lack_Table() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(emptyOrderTable1.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("empty가 아닌 테이블이 포함되어 있으면 그룹을 만들 수 없다.")
    @Test
    void create_Exception_NotEmpty_Table() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(
                List.of(emptyOrderTable1.getId(), notEmptyOrderTable.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("다른 테이블 그룹에 이미 포함된 테이블로 새로운 테이블 그룹을 만들 수 없다.")
    @Test
    void create_Exception_Already_Join_TableGroup() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(emptyOrderTable1.getId(),
                emptyOrderTable2.getId()));
        tableGroupService.create(tableGroupCreateRequest);

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void ungroup() {
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(emptyOrderTable1.getId(),
                emptyOrderTable2.getId()));
        TableGroupCreateResponse tableGroupCreateResponse = tableGroupService.create(tableGroupCreateRequest);

        tableGroupService.ungroup(tableGroupCreateResponse.getId());

        List<OrderTable> savedOrderTables = orderTableRepository.findAllByTableGroupId(
                tableGroupCreateResponse.getId());

        for (OrderTable orderTable : savedOrderTables) {
            assertThat(orderTable.getTableGroup()).isNull();
            assertThat(orderTable.isEmpty()).isFalse();
        }
    }

    @DisplayName("완료되지 않은 주문이 포함된 테이블 그룹은 해제할 수 없다.")
    @Test
    void ungroup_Exception_Not_Completion_Order() {
        Order order = new Order(emptyOrderTable1);
        orderRepository.save(order);
        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(emptyOrderTable1.getId(),
                emptyOrderTable2.getId()));

        TableGroupCreateResponse tableGroupCreateResponse = tableGroupService.create(tableGroupCreateRequest);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupCreateResponse.getId()))
                .isInstanceOf(IllegalStateException.class);
    }
}
