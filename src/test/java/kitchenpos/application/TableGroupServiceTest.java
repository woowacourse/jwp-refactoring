package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    TableGroupService tableGroupService;

    @Test
    void 테이블_그룹을_생성한다() {
        //given
        OrderTable orderTable = ORDER_TABLE(true, 1);
        OrderTable orderTable1 = orderTableDao.save(orderTable);

        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable save = orderTableDao.save(orderTable2);

        TableGroup tableGroup = TABLE_GROUP(List.of(orderTable1,save));

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //then
        for(OrderTable orderTableIn : savedTableGroup.getOrderTables()) {
            assertThat(orderTableIn.isEmpty()).isFalse();
        }

    }

    @Test
    void 테이블_그룹에서_개별_테이블들을_해제한다() {
        //given
        OrderTable orderTable = ORDER_TABLE(true, 1);
        OrderTable orderTable1 = orderTableDao.save(orderTable);

        OrderTable orderTable2 = ORDER_TABLE(true, 1);
        OrderTable save = orderTableDao.save(orderTable2);

        List<OrderTable> orderTables = List.of(orderTable1, save);
        TableGroup tableGroup = TABLE_GROUP(orderTables);
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        //when
        tableGroupService.ungroup(savedTableGroup.getId());
        List<OrderTable> allByTableGroupId = orderTableDao.findAllByTableGroupId(savedTableGroup.getId());

        //then
        for(OrderTable table : allByTableGroupId) {
            assertThat(table.isEmpty()).isFalse();
            assertThat(table.getTableGroupId()).isNull();
        }
    }
}
