package kitchenpos.table.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.exception.TableGroupException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final ApplicationEventPublisher publisher;

    public TableGroupService(
            OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository,
            ApplicationEventPublisher publisher
    ) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public TableGroupResponse create(TableGroupCreateRequest request) {
        List<Long> orderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
        List<OrderTable> orderTables = orderTableRepository.getAllByIdIn(orderTableIds);

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new TableGroupException("주문 테이블의 수가 유효하지 않습니다.");
        }

        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);

        orderTables.forEach(orderTable -> orderTable.assignTableGroup(tableGroup.getId()));

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTablesOfTableGroup = orderTableRepository.findAllByTableGroupId(tableGroupId);
        orderTablesOfTableGroup.forEach(OrderTable::ungroup);
        orderTableRepository.saveAll(
                orderTablesOfTableGroup.stream()
                        .map(OrderTable::publish)
                        .collect(Collectors.toList())
        );
    }
}
