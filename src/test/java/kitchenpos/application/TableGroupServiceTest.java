package kitchenpos.application;

import static kitchenpos.fixture.TableFixture.빈_테이블_1번;
import static kitchenpos.fixture.TableFixture.빈_테이블_2번;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableIdRequest;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
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
        final TableGroupResponse actual = tableGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getOrderTables()).hasSize(2),
            () -> assertThat(tableGroupDao.findAll()).hasSize(1),
            () -> assertThat(orderTableDao.findAll()).hasSize(2)
        );
    }

    @DisplayName("그룹화된 주문 테이블 그룹을 개별 주문 테이블로 설정한다.")
    @Test
    void ungroup() {
        // given
        final OrderTable orderTable1 = orderTableDao.save(빈_테이블_1번);
        final OrderTable orderTable2 = orderTableDao.save(빈_테이블_2번);

        final TableGroup tableGroup = tableGroupDao.save(new TableGroup(
            null, LocalDateTime.now()));
        updateOrderTable(orderTable1, tableGroup);
        updateOrderTable(orderTable2, tableGroup);

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

    private void updateOrderTable(final OrderTable orderTable1, final TableGroup tableGroup) {
        orderTableDao.save(new OrderTable(
            orderTable1.getId(),
            tableGroup.getId(),
            orderTable1.getNumberOfGuests(),
            orderTable1.isEmpty())
        );
    }
}
