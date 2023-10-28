package table.service;

import exception.InvalidOrderStateException;
import exception.NoSuchDataException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;
import table.domain.OrderTable;
import table.domain.TableGroup;
import table.dto.OrderStatusValidateByIdsEvent;
import table.dto.request.CreateTableGroupRequest;
import table.dto.request.OrderTableRequest;
import table.dto.response.TableGroupResponse;
import table.repository.OrderTableRepository;
import table.repository.TableGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    public TableGroupResponse create(final CreateTableGroupRequest request) {

        final List<OrderTableRequest> orderTables = request.getOrderTables();

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderStateException("테이블 2개 이상부터 그룹을 형성할 수 있습니다.");
        }

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);

        if (orderTables.size() != savedOrderTables.size()) {
            throw new NoSuchDataException("입력한 테이블들이 존재하지 않습니다.");
        }

        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.validateEmptyAndGroup();
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now());

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        for (final OrderTable orderTable : savedOrderTables) {
            orderTable.group(savedTableGroup.getId());
        }

        return TableGroupResponse.from(savedTableGroup, savedOrderTables);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        publisher.publishEvent(new OrderStatusValidateByIdsEvent(orderTableIds));

        for (final OrderTable orderTable : orderTables) {
            orderTable.unGroup();
        }
    }
}
