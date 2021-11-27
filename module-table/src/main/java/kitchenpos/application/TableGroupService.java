package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupRequestDto;
import kitchenpos.dto.response.OrderTableResponseDto;
import kitchenpos.dto.response.TableGroupResponseDto;
import kitchenpos.exception.InvalidOrderTableException;
import kitchenpos.exception.InvalidTableGroupSizeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableUngroupDomainService tableUngroupDomainService;

    public TableGroupService(
        OrderTableRepository orderTableRepository,
        TableGroupRepository tableGroupRepository,
        TableUngroupDomainService tableUngroupDomainService
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableUngroupDomainService = tableUngroupDomainService;
    }

    @Transactional
    public TableGroupResponseDto create(final TableGroupRequestDto tableGroupRequestDto) {
        List<Long> orderTableIds = tableGroupRequestDto.getOrderTables();

        if (CollectionUtils.isEmpty(orderTableIds)) {
            throw new InvalidTableGroupSizeException();
        }

        final List<OrderTable> orderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new InvalidOrderTableException();
        }

        TableGroup created = tableGroupRepository.save(new TableGroup(orderTables));
        created.setTables();

        return new TableGroupResponseDto(
            created.getId(),
            created.getCreatedDate(),
            toOrderTableResponseDto(created.getOrderTables())
        );
    }

    private List<OrderTableResponseDto> toOrderTableResponseDto(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(orderTable ->
                new OrderTableResponseDto(
                    orderTable.getId(),
                    orderTable.getTableGroupId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty()
                )
            ).collect(toList());
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableUngroupDomainService.ungroup(tableGroupId);
    }
}
