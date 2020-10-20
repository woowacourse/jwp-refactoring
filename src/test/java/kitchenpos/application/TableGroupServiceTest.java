package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
@Transactional
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create: 2개 이상의 중복 없이 존재, 비어있고, 그룹 지정 되지 않은 테이블들의 그룹 지정시, 그룹 지정 후 , 해당 객체를 반환한다.")
    @Test
    void create() {
        final OrderTable firstTable = new OrderTable();
        firstTable.setEmpty(true);
        firstTable.setNumberOfGuests(0);
        firstTable.setTableGroupId(null);
        final OrderTable firstSavedTable = orderTableDao.save(firstTable);

        final OrderTable secondTable = new OrderTable();
        secondTable.setEmpty(true);
        secondTable.setNumberOfGuests(0);
        secondTable.setTableGroupId(null);
        final OrderTable secondSavedTable = orderTableDao.save(secondTable);

        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.list(firstSavedTable, secondSavedTable));
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).hasSize(2)
        );
    }
}