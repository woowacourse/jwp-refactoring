package kitchenpos.table.application;

import kitchenpos.table.validator.TableGroupValidator;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.dto.TableGroupSaveRequest;
import kitchenpos.table.domain.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupValidator tableGroupValidator;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final TableGroupValidator tableGroupValidator,
                             final TableGroupRepository tableGroupRepository) {
        this.tableGroupValidator = tableGroupValidator;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupSaveRequest request) {
        TableGroup savedTableGroup = tableGroupRepository.save(new TableGroup(request.getOrderTables()));
        return new TableGroupResponse(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup savedTableGroup = tableGroupRepository.getById(tableGroupId);
        savedTableGroup.ungroup(tableGroupValidator.validate(savedTableGroup));
    }
}
