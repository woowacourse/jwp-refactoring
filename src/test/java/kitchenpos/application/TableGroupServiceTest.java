package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.NotFoundOrderTableException;
import kitchenpos.exception.OrderTableGroupingSizeException;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends ServiceTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(0, true);
    }

    @Test
    void 테이블그룹을_생성한다() {
        OrderTable saved1 = orderTableDao.save(orderTable);
        OrderTable saved2 = orderTableDao.save(orderTable);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Arrays.asList(
                new OrderTableIdDto(saved1.getId()),
                new OrderTableIdDto(saved2.getId())
        ));

        TableGroup savedTableGroup = tableGroupService.create(tableGroupCreateRequest);

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 테이블그룹을_생성할때_주문테이블_수가_2개미만이면_예외를_발생한다() {
        OrderTable saved = orderTableDao.save(orderTable);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(List.of(
                new OrderTableIdDto(saved.getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(OrderTableGroupingSizeException.class);
    }

    @Test
    void 테이블그룹을_생성할때_저장된_orderTables와_일치하지않으면_예외를_발생한다() {
        OrderTable saved1 = orderTableDao.save(orderTable);
        orderTableDao.save(orderTable);

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Arrays.asList(
                new OrderTableIdDto(saved1.getId()),
                new OrderTableIdDto(0L)
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    void 그룹을_해제한다() {
        OrderTable saved1 = orderTableDao.save(orderTable);
        OrderTable saved2 = orderTableDao.save(orderTable);

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(saved1, saved2));
        tableGroupDao.save(tableGroup);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTableDao.findById(saved1.getId()).get().getTableGroupId()).isNull();
    }
}
