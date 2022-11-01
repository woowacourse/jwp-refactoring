package kitchenpos.application.table;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.mapper.TableGroupDtoMapper;
import kitchenpos.dto.table.request.OrderTableIdRequest;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.TableGroupResponse;
import kitchenpos.exception.badrequest.OrderTableNotExistsException;
import kitchenpos.exception.badrequest.TableGroupNotExistsException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupDtoMapper tableGroupDtoMapper;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    public TableGroupService(final TableGroupDtoMapper tableGroupDtoMapper,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.tableGroupDtoMapper = tableGroupDtoMapper;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<OrderTable> savedOrderTables = findSavedOrderTables(tableGroupCreateRequest);
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(savedOrderTables));
        return tableGroupDtoMapper.toTableGroupResponse(savedTableGroup);
    }

    private List<OrderTable> findSavedOrderTables(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<Long> orderTableIds = toOrderTableIds(tableGroupCreateRequest.getOrderTables());
        List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTablesExists(orderTableIds, savedOrderTables);
        return savedOrderTables;
    }

    private void validateOrderTablesExists(final List<Long> orderTableIds, final List<OrderTable> savedOrderTables) {
        if (orderTableIds.size() != savedOrderTables.size()) {
            throw new OrderTableNotExistsException();
        }
    }

    private List<Long> toOrderTableIds(final List<OrderTableIdRequest> orderTableIdRequests) {
        return orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotExistsException::new);
        List<Long> orderTableIds = getOrderTableIds(tableGroup.getOrderTables());
        applicationEventPublisher.publishEvent(new UngroupEvent(orderTableIds));
        tableGroup.ungroup();
    }

    private List<Long> getOrderTableIds(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }
}
