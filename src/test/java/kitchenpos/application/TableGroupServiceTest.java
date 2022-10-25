package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createTableGroup;
import static kitchenpos.fixture.TableFixture.빈_테이블_1번;
import static kitchenpos.fixture.TableFixture.빈_테이블_2번;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    @DisplayName("개별 주문 테이블을 그룹화한다.")
    @Test
    void create() {
        // given
        OrderTable orderTable1 = orderTableDao.save(빈_테이블_1번);
        OrderTable orderTable2 = orderTableDao.save(빈_테이블_2번);

        TableGroup request = createTableGroup(null, List.of(orderTable1, orderTable2));

        // when
        TableGroup actual = tableGroupService.create(request);

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
        OrderTable orderTable1 = orderTableDao.save(빈_테이블_1번);
        OrderTable orderTable2 = orderTableDao.save(빈_테이블_2번);

        TableGroup tableGroup = saveTableGroup(orderTable1, orderTable2);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        OrderTable actual1 = orderTableDao.findById(orderTable1.getId()).orElseThrow();
        OrderTable actual2 = orderTableDao.findById(orderTable2.getId()).orElseThrow();
        assertAll(
                () -> assertThat(actual1.getTableGroupId()).isNull(),
                () -> assertThat(actual1.isEmpty()).isFalse(),
                () -> assertThat(actual2.getTableGroupId()).isNull(),
                () -> assertThat(actual2.isEmpty()).isFalse()
        );
    }

    private TableGroup saveTableGroup(OrderTable... orderTables) {
        TableGroup tableGroup = tableGroupDao.save(createTableGroup(1L, orderTables));
        for (OrderTable orderTable : orderTables) {
            orderTable.setTableGroupId(tableGroup.getId());
            orderTableDao.save(orderTable);
        }

        return tableGroup;
    }
}
