package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.persistence.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.persistence.TableGroupRepository;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.TableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderTableRepository orderTableRepository, TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest tableGroup) {
        TableGroup newTableGroup = new TableGroup(toOrderTables(tableGroup.getOrderTables()));

        return tableGroupRepository.save(newTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("그런 테이블 그룹은 없습니다"));
        tableGroup.ungroup();
    }

    private List<OrderTable> toOrderTables(List<OrderTableRequest> orderTableRequests) {
        final List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }
}
