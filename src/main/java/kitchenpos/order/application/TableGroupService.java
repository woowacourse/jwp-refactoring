package kitchenpos.order.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.request.TableGroupRequest;
import kitchenpos.order.application.response.TableGroupResponse;
import kitchenpos.order.dao.TableGroupDao;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.application.validator.TableGroupValidator;
import kitchenpos.order.application.validator.TableUngroupValidator;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final TableGroupDao tableGroupDao;
    private final TableGroupValidator tableGroupValidator;
    private final TableUngroupValidator tableUngroupValidator;

    public TableGroupService(final TableGroupDao tableGroupDao,
        final TableGroupValidator tableGroupValidator,
        final TableUngroupValidator tableUngroupValidator) {
        this.tableGroupDao = tableGroupDao;
        this.tableGroupValidator = tableGroupValidator;
        this.tableUngroupValidator = tableUngroupValidator;
    }

    @Transactional
    public TableGroupResponse create(final TableGroupRequest request) {
        TableGroup tableGroup = new TableGroup(request.toOrderTables());
        tableGroupValidator.validate(tableGroup);

        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);
        savedTableGroup.groupOrderTables();
        return TableGroupResponse.from(savedTableGroup);
    }

    @Transactional
    public void ungroup(final Long tableGroupId) {
        TableGroup tableGroup = tableGroupDao.getById(tableGroupId);
        tableUngroupValidator.validate(tableGroup);
        tableGroup.ungroupOrderTables();
    }
}
