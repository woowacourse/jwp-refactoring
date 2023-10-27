package kitchenpos.tablegroup.application;

import exception.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableDao;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupDao;
import kitchenpos.tablegroup.ui.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final OrderTableDao orderTableDao;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(final OrderTableDao orderTableDao, final TableGroupDao tableGroupDao) {
        this.orderTableDao = orderTableDao;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = findAllOrderTablesByIds(request.getOrderTableIds());

        final TableGroup tableGroup = TableGroup.of(savedOrderTables, LocalDateTime.now());

        return TableGroupResponse.from(tableGroupDao.save(tableGroup));
    }

    private List<OrderTable> findAllOrderTablesByIds(final List<Long> ids) {
        final var orderTables = orderTableDao.findAllByIdIn(ids);

        if (orderTables.size() != ids.size()) {
            throw new EntityNotFoundException("모든 아이디에 해당하는 주문 테이블을 찾을 수 없습니다.");
        }
        return orderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final var tableGroup = tableGroupDao.findById(tableGroupId)
                                            .orElseThrow(() -> new EntityNotFoundException("해당하는 아이디의 테이블 그룹을 찾을 수 없습니다."));
        tableGroup.unGroup();
    }
}
