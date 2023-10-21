package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.ui.request.TableGroupCreateRequest;
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
    public TableGroup create(final TableGroupCreateRequest request) {
        final List<OrderTable> savedOrderTables = findAllByIds(request.getOrderTableIds());

        final TableGroup tableGroup = new TableGroup(savedOrderTables);

        return tableGroupDao.save(tableGroup);
    }

    private List<OrderTable> findAllByIds(final List<Long> ids) {
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
