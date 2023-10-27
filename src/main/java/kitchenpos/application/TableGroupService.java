package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.request.TableGroupCreateRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table_group.TableGroup;
import kitchenpos.domain.table_group.TableGroupValidator;
import kitchenpos.support.AggregateReference;
import kitchenpos.repositroy.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupRepository tableGroupRepository;
    private final TableGroupValidator tableGroupValidator;

    public TableGroupService(
            final TableGroupRepository tableGroupRepository,
            final TableGroupValidator tableGroupValidator
    ) {
        this.tableGroupRepository = tableGroupRepository;
        this.tableGroupValidator = tableGroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final List<AggregateReference<OrderTable>> orderTables = request.getOrderTables().stream()
                .map(it -> new AggregateReference<OrderTable>(it))
                .collect(Collectors.toUnmodifiableList());
        final LocalDateTime createdTime = LocalDateTime.now();
        final TableGroup tableGroup = new TableGroup(orderTables, createdTime, tableGroupValidator);

        return TableGroupResponse.from(
                tableGroupRepository.save(tableGroup),
                orderTables.stream()
                        .map(AggregateReference::getId)
                        .collect(Collectors.toUnmodifiableList())
        );
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        final TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
        tableGroup.ungroup();
        tableGroupRepository.delete(tableGroup);
    }
}
