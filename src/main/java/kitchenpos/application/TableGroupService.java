package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest tableGroupCreateRequest) {
        List<OrderTable> orderTables = tableGroupCreateRequest.getOrderTables()
                .stream()
                .map(orderTableRepository::getById)
                .collect(Collectors.toList());

        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        validateUngroupPossible(tableGroup);
        tableGroup.ungroupOrderTables();
    }

    private void validateUngroupPossible(TableGroup tableGroup) {
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTables(), List.of(COOKING.name(), MEAL.name()))) {
            throw new IllegalArgumentException();
        }
    }
}
