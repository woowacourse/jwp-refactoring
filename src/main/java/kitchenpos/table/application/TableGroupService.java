package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupDao;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupDao tableGroupDao;
    private final TableValidator tableValidator;

    public TableGroupService(
        OrderTableRepository orderTableRepository,
        TableGroupDao tableGroupDao,
        TableValidator tableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupDao = tableGroupDao;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(TableGroupRequest tableGroupRequest) {
        List<OrderTable> orderTables = getOrderTables(tableGroupRequest);
        TableGroup tableGroup = TableGroup.group(orderTables, LocalDateTime.now());
        tableGroupDao.save(tableGroup);
        return TableGroupResponse.from(tableGroup);
    }

    private List<OrderTable> getOrderTables(TableGroupRequest tableGroupRequest) {
        List<Long> orderTableIds = tableGroupRequest.getOrderTableIds();
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateNotExistTables(orderTableIds, orderTables);
        return orderTables;
    }

    private void validateNotExistTables(List<Long> orderTableIds, List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 테이블로 지정할 수 없습니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = getGroupById(tableGroupId);
        tableValidator.validateUngroup(tableGroup);
        tableGroup.ungroup();
    }

    private TableGroup getGroupById(Long tableGroupId) {
        return tableGroupDao.findById(tableGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
    }
}
