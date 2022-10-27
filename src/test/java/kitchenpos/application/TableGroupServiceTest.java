package kitchenpos.application;

import static kitchenpos.support.DomainFixture.givenEmptyTable;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ApplicationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("테이블의 그룹을 해제한다.")
    @Test
    void ungroup() {
        OrderTable table1 = 주문테이블_생성(givenEmptyTable());
        OrderTable table2 = 주문테이블_생성(givenEmptyTable());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(table1, table2));

        tableGroupService.ungroup(tableGroup.getId());

        List<OrderTable> tables = orderTableDao.findAllByIdIn(List.of(table1.getId(), table2.getId()));

        assertThat(tables).extracting("tableGroupId")
                .containsOnlyNulls();
    }
}
