package kitchenpos.application.concrete;

import java.util.Objects;
import kitchenpos.application.TableGroupService;
import kitchenpos.domain.TableGroupMapper;
import kitchenpos.domain.validator.TableGroupValidator;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.request.TableGroupCreateRequest;
import kitchenpos.ui.dto.response.TableGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class JpaTableGroupService implements TableGroupService {
    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupMapper tableGroupMapper;

    public JpaTableGroupService(final TableGroupValidator tableGroupValidator,
                                final TableGroupRepository tableGroupRepository,
                                final OrderTableRepository orderTableRepository,
                                final TableGroupMapper tableGroupMapper) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupMapper = tableGroupMapper;
    }

    @Transactional
    @Override
    public TableGroupResponse create(final TableGroupCreateRequest request) {
        final var entity = tableGroupMapper.mapFrom(request);
        final var tableGroup = tableGroupRepository.save(entity);
        final var orderTables = orderTableRepository.findAllByTableGroupId(entity.getId());

        return TableGroupResponse.of(tableGroup, orderTables);
    }

    @Transactional
    @Override
    public void unGroup(final Long tableGroupId) {
        validateTableGroupId(tableGroupId);

        final var tableGroup = tableGroupRepository.getById(tableGroupId)
                .unGroup(tableGroupValidator);

        tableGroupRepository.save(tableGroup);
    }

    private void validateTableGroupId(final Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
