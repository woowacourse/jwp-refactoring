package kitchenpos.application.table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.request.OrderTableRequest;
import kitchenpos.dto.table.request.TableGroupCreateRequest;
import kitchenpos.dto.table.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableGroupService(final TableGroupRepository tableGroupRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableValidator tableValidator) {
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        List<OrderTable> orderTables = getOrderTables(request);
        validate(request, orderTables);

        TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                .createdDate(LocalDateTime.now())
                .orderTables(new OrderTables(orderTables))
                .build());

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        tableValidator.checkOrderStatus(orderTables.getIds());
        orderTables.ungroup();
    }

    private List<OrderTable> getOrderTables(final TableGroupCreateRequest request) {
        List<OrderTableRequest> orderTableRequests = request.getOrderTables();
        List<Long> orderTableIds = orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        return orderTableRepository.findAllById(orderTableIds);
    }

    private void validate(final TableGroupCreateRequest request, final List<OrderTable> orderTables) {
        if (request.getOrderTables().size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
        }
    }
}
