package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableValidator;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableValidator orderTableValidator;

    public TableGroupService(
            final OrderTableRepository orderTableRepository,
            final TableGroupRepository tableGroupRepository,
            final OrderTableValidator orderTableValidator
    ) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableValidator = orderTableValidator;
    }

    public TableGroup create(final TableGroupCreateRequest request) {
        final List<Long> orderTablesIds = request.getOrderTablesIds();
        final List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTablesIds);
        if (orderTablesIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("존재하지 않는 주문 테이블이 존재합니다.");
        }

        TableGroup tableGroup = TableGroup.createWithoutGrouping();
        tableGroup.group(orderTables);
        tableGroupRepository.save(tableGroup);
        return tableGroup;
    }

    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블 그룹 ID 입니다."));
        tableGroup.ungroup(orderTableValidator);
    }
}
