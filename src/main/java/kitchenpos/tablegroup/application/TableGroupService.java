package kitchenpos.tablegroup.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupDao;
import kitchenpos.tablegroup.dto.TableGroupCreateResponseDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher publisher;
    private final TableGroupDao tableGroupDao;

    public TableGroupService(ApplicationEventPublisher publisher, TableGroupDao tableGroupDao) {
        this.publisher = publisher;
        this.tableGroupDao = tableGroupDao;
    }

    @Transactional
    public TableGroupCreateResponseDto create(final TableGroup tableGroup) {
        final List<OrderTable> orderTables = tableGroup.getOrderTables();

        validateOrderTableSize(orderTables);

        List<Long> orderTableIds = parseOrderTableIds(orderTables);
        publisher.publishEvent(new TableGroupStartedToCreateEvent(orderTables, orderTableIds));

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        return new TableGroupCreateResponseDto(savedTableGroup);
    }

    private List<Long> parseOrderTableIds(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());
    }

    private void validateOrderTableSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        publisher.publishEvent(new TableGroupStartedToUngroupEvent(tableGroupId));
    }
}
