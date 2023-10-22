package kitchenpos.application;

import java.time.LocalDateTime;
import kitchenpos.application.dto.TableGroupCreateRequest;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class TableGroupService {

    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderTableRepository orderTableRepository,
                             final TableGroupRepository tableGroupRepository) {
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroup create(final TableGroupCreateRequest request) {
        final OrderTables orderTables = orderTableRepository.getAllById(request.getOrderTableIds());
        final TableGroup tableGroup = tableGroupRepository.save(TableGroup.forSave(orderTables.getOrderTables()));

        orderTables.registerTableGroup(tableGroup);
        orderTableRepository.saveAll(orderTables.getOrderTables());

        return tableGroup;
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        final OrderTables orderTables = orderTableRepository.getByTableGroup(tableGroup);

        orderTables.ungroup();
    }
}
