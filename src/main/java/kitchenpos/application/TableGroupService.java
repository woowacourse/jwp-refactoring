package kitchenpos.application;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.UnGroupEvent;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.TableGroupRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher publisher) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroup create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(request.getOrderTableIds());
        validateOrderTablesIsExist(request, savedOrderTables);
        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        tableGroup.bindTablesToGroup();
        return tableGroup;
    }

    private static void validateOrderTablesIsExist(final TableGroupRequest request,
                                                   final List<OrderTable> savedOrderTables) {
        if (savedOrderTables.size() != request.getOrderTableIds().size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 포함되어 있습니다.");
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹입니다."));
        publisher.publishEvent(new UnGroupEvent(tableGroup.getGroupedTables()));
        tableGroupRepository.save(tableGroup);
    }
}
