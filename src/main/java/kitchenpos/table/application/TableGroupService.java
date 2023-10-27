package kitchenpos.table.application;

import kitchenpos.table.application.request.OrderTableIdRequest;
import kitchenpos.table.application.request.TableGroupCreateRequest;
import kitchenpos.table.application.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.TableGroupValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupValidator tableGroupValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> requestOrderTableIds = request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
        final List<OrderTable> findOrderTables = orderTableRepository.findAllByIdIn(requestOrderTableIds);
        if (requestOrderTableIds.size() != findOrderTables.size()) {
            throw new IllegalArgumentException("단체 지정을 위해 요청하신 주문 테이블 목록이 정확하지 않습니다. 선택한 주문 테이블 목록을 다시 확인해주세요.");
        }

        final TableGroup tableGroup = new TableGroup(OrderTables.empty());
        tableGroup.addOrderTables(findOrderTables);

        final TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        tableGroupRepository.findTableGroupByTableGroupId(tableGroupId)
                .ungroup(tableGroupValidator);
    }
}
