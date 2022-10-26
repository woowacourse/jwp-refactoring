package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.빈_테이블_1번;
import static kitchenpos.fixture.TableFixture.빈_테이블_2번;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableIdRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @DisplayName("개별 주문 테이블을 그룹화한다.")
    @Test
    void create() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(빈_테이블_1번);
        final OrderTable orderTable2 = orderTableDao.save(빈_테이블_2번);
        final OrderTableIdRequest orderTableIdRequest1 = new OrderTableIdRequest(
            orderTable1.getId());
        final OrderTableIdRequest orderTableIdRequest2 = new OrderTableIdRequest(
            orderTable2.getId());

        final TableGroupCreateRequest request = createTableGroupCreateRequest(
            List.of(orderTableIdRequest1, orderTableIdRequest2));

        // when
        final TableGroup actual = tableGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTables()).hasSize(2)
        );
    }

    @DisplayName("그룹화된 주문 테이블 그룹을 개별 주문 테이블로 설정한다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(빈_테이블_1번);
        final OrderTable orderTable2 = orderTableDao.save(빈_테이블_2번);

        final TableGroup tableGroup = saveTableGroup(orderTable1, orderTable2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        final OrderTable actual1 = orderTableDao.findById(orderTable1.getId()).orElseThrow();
        final OrderTable actual2 = orderTableDao.findById(orderTable2.getId()).orElseThrow();
        assertAll(
            () -> assertThat(actual1.getTableGroupId()).isNull(),
            () -> assertThat(actual1.isEmpty()).isFalse(),
            () -> assertThat(actual2.getTableGroupId()).isNull(),
            () -> assertThat(actual2.isEmpty()).isFalse()
        );
    }

    private TableGroup saveTableGroup(final OrderTable... orderTables) {
        final TableGroup tableGroup = tableGroupDao.save(createTableGroup(1L, orderTables));
        for (final OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return tableGroup;
    }
}
