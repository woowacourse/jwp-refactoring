package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setTableGroupId(null);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setTableGroupId(null);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTableDao.save(orderTable2));

        tableGroup.setOrderTables(orderTables);
        // when
        TableGroup nweTableGroup = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(nweTableGroup.getId()).isNotNull(),
                () -> assertThat(nweTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(nweTableGroup.getOrderTables().size()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 비어있다면 예외를 반환한다.")
    void create_WhenEmptyOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 주문테이블이 1개 이하라면 예외를 반환한다.")
    void create_WhenUnderOneOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setTableGroupId(null);

        orderTables.add(orderTableDao.save(orderTable1));

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성 시 입력된 주문 테이블 목록과 저장된 주문 테이블 목록이 다르다면 예외를 반환한다.")
    void create_WhenNotExistOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setTableGroupId(null);
        OrderTable orderTable2 = new OrderTable();
        orderTable1.setId(10L);
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setTableGroupId(null);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTable2);

        tableGroup.setOrderTables(orderTables);
        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 목록과 테이블 그룹을 분리한다.")
    void ungroup() {
        // when
        TableGroup tableGroup = new TableGroup();
        List<OrderTable> orderTables = new ArrayList<>();

        OrderTable orderTable1 = new OrderTable();
        orderTable1.setEmpty(true);
        orderTable1.setNumberOfGuests(0);
        orderTable1.setTableGroupId(null);
        OrderTable orderTable2 = new OrderTable();
        orderTable2.setEmpty(true);
        orderTable2.setNumberOfGuests(0);
        orderTable2.setTableGroupId(null);

        orderTables.add(orderTableDao.save(orderTable1));
        orderTables.add(orderTableDao.save(orderTable2));

        tableGroup.setOrderTables(orderTables);

        TableGroup newTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(newTableGroup.getId());
        // then
        List<OrderTable> orderTablesInTableGroup = orderTableDao.findAllByTableGroupId(newTableGroup.getId());
        assertThat(orderTablesInTableGroup.size()).isEqualTo(0);
    }
}
