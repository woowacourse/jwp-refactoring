package kitchenpos.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
public class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, true);
        OrderTable orderTable2 = new OrderTable(1L, 2, true);
        OrderTable savedOrderTable1 = tableService.create(orderTable1);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);
        TableGroup tableGroup = new TableGroup(List.of(savedOrderTable1, savedOrderTable2));

        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup).usingRecursiveComparison()
            .ignoringFields("id", "createdDate")
            .isEqualTo(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹에 속한 OrderTable 을 삭제한다.")
    void ungroup() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 1, true);
        OrderTable orderTable2 = new OrderTable(1L, 2, true);
        OrderTable savedOrderTable1 = tableService.create(orderTable1);
        OrderTable savedOrderTable2 = tableService.create(orderTable2);
        TableGroup tableGroup = new TableGroup(List.of(savedOrderTable1, savedOrderTable2));

        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        Long tableGroupId = savedTableGroup.getId();
        tableGroupService.ungroup(tableGroupId);
        TableGroup foundTableGroup = tableGroupDao.findById(tableGroupId).orElseThrow();

        // then
        assertThat(foundTableGroup.getOrderTables()).isNull();
    }
}
