package kitchenpos.table.application;

import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.presentation.dto.TableGroupCreateRequest;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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

    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTables();
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new IllegalArgumentException();
        }

        final TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup());

        for (final OrderTable savedOrderTable : savedOrderTables) {
            publisher.publishEvent(new TableGroupCreateEvent(savedOrderTable.getId()));
            savedOrderTable.group(savedTableGroup);
        }

        return savedTableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (final OrderTable orderTable : orderTables) {
            publisher.publishEvent(new TableGroupCreateEvent(orderTable.getId()));
            orderTable.unGroup();
        }
    }
}
