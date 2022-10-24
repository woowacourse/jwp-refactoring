package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블그룹을_생성한다() {
        TableGroup tableGroup = new TableGroup();
        OrderTable saved = orderTableDao.save(createOrderTable(true));
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        tableGroup.setOrderTables(Arrays.asList(saved, saved1));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 테이블그룹을_생성할때_주문테이블_수가_2개미만이면_예외를_발생한다() {
        TableGroup tableGroup = new TableGroup();
        OrderTable saved = orderTableDao.save(createOrderTable(true));
        tableGroup.setOrderTables(Collections.singletonList(saved));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_저장된_orderTables와_일치하지않으면_예외를_발생한다() {
        TableGroup tableGroup = new TableGroup();

        tableGroup.setOrderTables(Arrays.asList(
                createOrderTable(true),
                createOrderTable(true)
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_해제한다() {
        TableGroup tableGroup = new TableGroup();
        OrderTable saved = orderTableDao.save(createOrderTable(true));
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        tableGroup.setOrderTables(Arrays.asList(saved, saved1));

        tableGroupService.create(tableGroup);
        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTableDao.findById(saved.getId()).get().isEmpty()).isFalse();
    }

    private OrderTable createOrderTable(boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(isEmpty);
        return orderTable;
    }
}
