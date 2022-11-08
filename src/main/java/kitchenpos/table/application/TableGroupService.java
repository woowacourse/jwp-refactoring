package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.application.request.TableGroupRequest;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(final TableGroupRequest request) {
        final List<OrderTable> savedOrderTables = getSavedOrderTables(request);

        if (request.getOrderTables().size() != savedOrderTables.size()) {
            throw new IllegalArgumentException("실제 존재하는 주문 테이블과의 정보가 일치하지 않습니다.");
        }

        final TableGroup tableGroup = TableGroup.of(LocalDateTime.now(), savedOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);
        return TableGroupResponse.from(savedTableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new);

        tableGroup.ungroup();
    }

    private List<OrderTable> getSavedOrderTables(final TableGroupRequest request) {
        final List<OrderTable> orderTables = getOrderTables(request);
        final List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    private static List<OrderTable> getOrderTables(final TableGroupRequest request) {
        final List<OrderTableRequest> orderTables = request.getOrderTables();
        return orderTables.stream()
                .map(it -> new OrderTable(it.getId(), it.getTableGroupId(), it.getNumberOfGuests(), it.isEmpty()))
                .collect(toList());
    }
}
