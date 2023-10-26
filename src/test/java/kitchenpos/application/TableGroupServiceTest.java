package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Objects;
import kitchenpos.IntegrationTest;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.fixture.RequestParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends IntegrationTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("단체 테이블 등록 시 지정된 빈 테이블들을 주문 테이블로 변경해 저장한다.")
    void 단체_테이블_지정_성공_주문_테이블로_변경() {
        // given
        final List<OrderTable> existingTables = List.of(
                tableService.create(new OrderTableCreateRequest(0, true)),
                tableService.create(new OrderTableCreateRequest(0, true)),
                tableService.create(new OrderTableCreateRequest(0, true))
        );
        final long emptyCountBefore = countEmpty(existingTables);

        // when
        final TableGroup saved = tableGroupService.create(RequestParser.from(existingTables));

        // then
        final long emptyCountAfter = countEmpty(saved.getOrderTables());
        assertThat(emptyCountBefore).isEqualTo(existingTables.size());
        assertThat(emptyCountAfter).isZero();
    }

    private long countEmpty(final List<OrderTable> tables) {
        return tables.stream()
                .filter(OrderTable::isEmpty)
                .count();
    }

    @Test
    @DisplayName("기존에 존재하는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_존재하지_않는_테이블() {
        // given
        final OrderTable existingTable = tableService.list().get(0);
        final OrderTable newTable = new OrderTable(0, true);

        // when
        final List<OrderTable> tablesInGroup = List.of(existingTable, newTable);

        // then
        assertThatThrownBy(() -> tableGroupService.create(RequestParser.from(tablesInGroup)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블을 개별의 주문 테이블로 분할할 수 있다.")
    void 단체_테이블_분할_성공_저장() {
        // given
        final TableGroup tableGroup = tableGroupService.create(RequestParser.from(tableService.list()));

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final List<OrderTable> tablesAfterUngroup = tableService.list();
        assertAll(() -> assertThat(tablesAfterUngroup)
                        .map(OrderTable::getTableGroupId)
                        .allMatch(Objects::isNull),
                () -> assertThat(tablesAfterUngroup)
                        .noneMatch(OrderTable::isEmpty));
    }
}
