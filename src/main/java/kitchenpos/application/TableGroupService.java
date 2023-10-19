package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableGroupRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.TableGroupCreateRequest;
import kitchenpos.ui.dto.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;

@Transactional(readOnly = true)
@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableService orderTableService;

    public TableGroupService(final OrderRepository orderRepository,
                             final TableGroupRepository tableGroupRepository,
                             final OrderTableService orderTableService) {
        this.orderRepository = orderRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableService = orderTableService;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<Long> orderTableIds = request.getOrderTableIds();
        final List<OrderTable> orderTables = orderTableService.findAllByIdsOrThrow(orderTableIds);

        final TableGroup tableGroup = tableGroupRepository.save(new TableGroup(orderTables));
        tableGroup.group();

        return TableGroupResponse.from(tableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 단체 지정입니다."));

        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                tableGroup.getOrderTableIds(), List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
        tableGroup.ungroup();
    }
}
