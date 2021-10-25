package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class TableGroupServiceIntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블_그룹_등록_완료() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L),
                new OrderTable(2L)
        );

        TableGroup tableGroup = 테이블_그룹_등록(orderTables);

        List<OrderTable> created = getTablesInTableGroup(tableGroup);
        assertThat(created.size()).isEqualTo(2);
    }

    @Test
    void 테이블_그룹에_포함되는_테이블이_존재하지_않는_경우_예외_발생() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L),
                new OrderTable(200L)
        );

        assertThatThrownBy(() -> 테이블_그룹_등록(orderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_삭제_완료() {
        List<OrderTable> orderTables = Arrays.asList(
                new OrderTable(1L),
                new OrderTable(2L)
        );
        TableGroup tableGroup = 테이블_그룹_등록(orderTables);
        List<OrderTable> created = getTablesInTableGroup(tableGroup);

        tableGroupService.ungroup(tableGroup.getId());
        List<OrderTable> deleted = getTablesInTableGroup(tableGroup);

        assertThat(created.size()).isEqualTo(2);
        assertThat(deleted.size()).isEqualTo(0);
    }

    private TableGroup 테이블_그룹_등록(List<OrderTable> orderTables) {
        return tableGroupService.create(new TableGroup(orderTables));
    }

    private List<OrderTable> getTablesInTableGroup(TableGroup tableGroup) {
        return tableService.list()
                           .stream()
                           .filter(table -> tableGroup.getId().equals(table.getTableGroupId()))
                           .collect(Collectors.toList());
    }
}
