package kitchenpos.table_group.application;

import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.ORDER_TABLE_IS_NOT_PRESENT_ALL;
import static kitchenpos.table_group.domain.exception.TableGroupExceptionType.TABLE_GROUP_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableDto;
import kitchenpos.table_group.application.dto.TableGroupDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table_group.domain.TableGroup;
import kitchenpos.table_group.domain.TableGroupRepository;
import kitchenpos.table_group.domain.exception.TableGroupException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {

    private final ApplicationEventPublisher eventPublisher;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(
        final ApplicationEventPublisher eventPublisher,
        final OrderTableRepository orderTableRepository,
        final TableGroupRepository tableGroupRepository
    ) {
        this.eventPublisher = eventPublisher;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupDto create(final TableGroupDto tableGroupDto) {
        final List<OrderTableDto> orderTableDtos = tableGroupDto.getOrderTables();
        final List<OrderTable> savedOrderTables = findOrderTables(orderTableDtos);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), savedOrderTables);
        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupDto.from(savedTableGroup);
    }

    private List<OrderTable> findOrderTables(final List<OrderTableDto> orderTableDtos) {
        final List<Long> orderTableIds = orderTableDtos.stream()
            .map(OrderTableDto::getId)
            .collect(Collectors.toList());
        final List<OrderTable> savedOrderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateRequestTableAlreadySaved(orderTableDtos, savedOrderTables);
        return savedOrderTables;
    }

    private static void validateRequestTableAlreadySaved(
        final List<OrderTableDto> orderTableDtos,
        final List<OrderTable> savedOrderTables
    ) {
        if (orderTableDtos.size() != savedOrderTables.size()) {
            throw new TableGroupException(ORDER_TABLE_IS_NOT_PRESENT_ALL);
        }
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
            .orElseThrow(() -> new TableGroupException(TABLE_GROUP_NOT_FOUND));
        validateContainedTablesOrderStatusIsNotCompletion(tableGroup);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }

    private void validateContainedTablesOrderStatusIsNotCompletion(final TableGroup tableGroup) {
        final List<Long> orderTableIds = tableGroup.getOrderTables()
            .stream()
            .map(OrderTable::getId)
            .collect(Collectors.toList());

        eventPublisher.publishEvent(new TableGroupUnGroupValidationEvent(orderTableIds));
    }
}
