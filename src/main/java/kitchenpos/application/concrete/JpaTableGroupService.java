package kitchenpos.application.concrete;

import java.util.Objects;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.validator.TableGroupValidator;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaTableGroupService implements TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    public JpaTableGroupService(final TableGroupValidator tableGroupValidator,
                                final TableGroupRepository tableGroupRepository,
                                final OrderTableRepository orderTableRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    @Override
    public TableGroup create(final TableGroupCreateRequest request) {
        final var tableGroup = new TableGroup(request.ids());
        tableGroup.group(tableGroupValidator);
        tableGroupRepository.save(tableGroup);
        final var groupedTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        return tableGroup.addOrderTables(groupedTables);
    }

    @Transactional
    @Override
    public void unGroup(final Long tableGroupId) {
        validateTableGroupId(tableGroupId);

        final var tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(IllegalArgumentException::new)
                .unGroup(tableGroupValidator);

        tableGroupRepository.save(tableGroup);
    }

    private void validateTableGroupId(final Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
