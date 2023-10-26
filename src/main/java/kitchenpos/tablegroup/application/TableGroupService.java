package kitchenpos.tablegroup.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupOrderStatusValidator;
import kitchenpos.tablegroup.domain.repository.TableGroupRepository;
import kitchenpos.tablegroup.presentation.dto.request.CreateTableGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;

    private final TableGroupRepository tableGroupRepository;

    private final TableGroupOrderStatusValidator tableGroupOrderStatusValidator;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final TableGroupOrderStatusValidator tableGroupOrderStatusValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupOrderStatusValidator = tableGroupOrderStatusValidator;
    }

    public TableGroup create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = request.getOrderTables().stream()
                                                    .map(orderTableRequest -> orderTableRepository.findById(orderTableRequest.getId())
                                                                                                  .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다.")))
                                                    .collect(Collectors.toList());

        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException("테이블 그룹을 만들기 위해선, 적어도 2개 이상의 주문 테이블이 필요합니다.");
        }

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(),
                                                     new ArrayList<>());

        tableGroup.addOrderTables(orderTables);
        return tableGroupRepository.save(tableGroup);
    }

    public void ungroup(final Long tableGroupId) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);

        orderTables.forEach(orderTable -> orderTable.ungroup(tableGroupOrderStatusValidator));
    }
}
