package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableIdDto;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void 테이블그룹을_생성한다() {
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        OrderTable saved2 = orderTableDao.save(createOrderTable(true));

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Arrays.asList(
                new OrderTableIdDto(saved1.getId()),
                new OrderTableIdDto(saved2.getId())
        ));

        TableGroup savedTableGroup = tableGroupService.create(tableGroupCreateRequest);

        assertThat(tableGroupDao.findById(savedTableGroup.getId())).isPresent();
    }

    @Test
    void 테이블그룹을_생성할때_주문테이블_수가_2개미만이면_예외를_발생한다() {
        OrderTable saved = orderTableDao.save(createOrderTable(true));

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.singletonList(
                new OrderTableIdDto(saved.getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블그룹을_생성할때_저장된_orderTables와_일치하지않으면_예외를_발생한다() {
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        orderTableDao.save(createOrderTable(true));

        TableGroupCreateRequest tableGroupCreateRequest = new TableGroupCreateRequest(Collections.singletonList(
                new OrderTableIdDto(saved1.getId())
        ));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_해제한다() {
        OrderTable saved1 = orderTableDao.save(createOrderTable(true));
        OrderTable saved2 = orderTableDao.save(createOrderTable(true));

        TableGroup tableGroup = new TableGroup(LocalDateTime.now(), Arrays.asList(saved1, saved2));
        tableGroupDao.save(tableGroup);

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTableDao.findById(saved1.getId()).get().getTableGroupId()).isNull();
    }

    private OrderTable createOrderTable(boolean isEmpty) {
        return new OrderTable(0, isEmpty);
    }
}
