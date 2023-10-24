package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.presentation.dto.CreateTableGroupRequest;
import kitchenpos.order.presentation.dto.OrderTableRequest;
import kitchenpos.support.NewTestSupporter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/truncate.sql")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private NewTestSupporter newTestSupporter;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void 테이블_그룹을_생성한다() {
        // given
        final OrderTable orderTable1 = newTestSupporter.createOrderTable(true);
        final OrderTable orderTable2 = newTestSupporter.createOrderTable(true);
        final OrderTableRequest orderTableRequest1 = new OrderTableRequest(orderTable1.getId());
        final OrderTableRequest orderTableRequest2 = new OrderTableRequest(orderTable2.getId());
        final CreateTableGroupRequest createTableGroupRequest = new CreateTableGroupRequest(List.of(orderTableRequest1,
                                                                                                    orderTableRequest2));

        // when
        final TableGroup tableGroup = tableGroupService.create(createTableGroupRequest);

        // then
        assertThat(tableGroup).isNotNull();
    }

    @Test
    void 테이블_그룹을_해제한다() {
        // given
        final TableGroup tableGroup = newTestSupporter.createTableGroup();

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        final boolean actual = orderTables.stream()
                                          .allMatch(orderTable -> !orderTable.isGrouped() && !orderTable.isEmpty());
        assertThat(actual).isTrue();
    }
}
