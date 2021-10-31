package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.request.OrderTableIdRequest;
import kitchenpos.ui.request.TableGroupRequest;
import kitchenpos.ui.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest tableGroupRequest) {
        final List<OrderTable> orderTables = findOrderTables(tableGroupRequest.getOrderTables());
        final TableGroup tableGroup = new TableGroup.Builder()
                .createdDate(LocalDateTime.now())
                .orderTables(orderTables)
                .build();

        tableGroupRepository.save(tableGroup);
        return TableGroupResponse.of(tableGroup);
    }

    private List<OrderTable> findOrderTables(List<OrderTableIdRequest> orderTableIdRequests) {
        if (CollectionUtils.isEmpty(orderTableIdRequests) || orderTableIdRequests.size() < 2) {
            throw new IllegalArgumentException();
        }

        final List<Long> orderTableIds = orderTableIdRequests.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());

        final List<OrderTable> foundOrderTables = orderTableRepository.findAllById(orderTableIds);
        if (orderTableIdRequests.size() != foundOrderTables.size()) {
            throw new IllegalArgumentException();
        }
        return foundOrderTables;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        for (OrderTable orderTable : orderTables) {
            orderTable.ungroupFromTableGroup();
        }
    }
}
