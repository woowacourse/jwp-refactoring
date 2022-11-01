package kitchenpos.table.application;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.application.dto.request.TableGroupCommand;
import kitchenpos.table.application.dto.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderUngroupValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderUngroupValidator orderUngroupValidator;

    public TableGroupService(final OrderRepository orderRepository,
                             final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderUngroupValidator orderUngroupValidator) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderUngroupValidator = orderUngroupValidator;
    }

    public TableGroupResponse create(final TableGroupCommand tableGroupCommand) {
        List<Long> orderTableIds = tableGroupCommand.orderTableIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateExistOrderTable(orderTableIds, orderTables);

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now(), orderTables));
        return TableGroupResponse.from(tableGroup);
    }

    private void validateExistOrderTable(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다.");
        }
    }

    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("주문 테이블을 찾을 수 없습니다."));
        tableGroup.ungroup(orderUngroupValidator);
    }
}
