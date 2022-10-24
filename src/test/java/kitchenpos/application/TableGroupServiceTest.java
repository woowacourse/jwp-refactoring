package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create_success() {
        // given
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(4, true));
        TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable1.getId()), new OrderTable(orderTable2.getId())));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        TableGroup dbTableGroup = tableGroupDao.findById(savedTableGroup.getId())
                .orElseThrow();
        assertThat(dbTableGroup.getId()).isEqualTo(savedTableGroup.getId());
    }

    @DisplayName("단체 지정을 생성할 때 빈 주문테이블이라면 예외를 반환한다.")
    @Test
    void create_fail_if_emptyOrderTable() {
        // given
        TableGroup tableGroup = new TableGroup(List.of());

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정을 생성할 때 주문테이블의 개수가 하나라면 예외를 반환한다.")
    @Test
    void create_fail_if_orderTable_is_one() {
        // given
        OrderTable orderTable = orderTableDao.save(new OrderTable(4, true));
        TableGroup tableGroup = new TableGroup(List.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup_success() {
        // given
        OrderTable orderTable1 = orderTableDao.save(new OrderTable(4, true));
        OrderTable orderTable2 = orderTableDao.save(new OrderTable(4, true));
        TableGroup tableGroup = new TableGroup(
                List.of(new OrderTable(orderTable1.getId()), new OrderTable(orderTable2.getId())));
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        List<OrderTable> orderTables = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());
        assertThat(orderTables).isEmpty();
    }
}
