package kitchenpos.table_group.application;

import java.util.List;
import kitchenpos.table_group.domain.GroupTableValidator;
import kitchenpos.table_group.domain.UngroupTableValidator;
import org.springframework.stereotype.Component;

@Component
public class FrontTableGroupValidator {

    private final List<GroupTableValidator> groupTableValidators;
    private final List<UngroupTableValidator> ungroupTableValidators;

    public FrontTableGroupValidator(
        final List<GroupTableValidator> groupTableValidators,
        final List<UngroupTableValidator> ungroupTableValidators
    ) {
        this.groupTableValidators = groupTableValidators;
        this.ungroupTableValidators = ungroupTableValidators;
    }

    public void validateGroup(final List<Long> tableIds) {
        groupTableValidators
            .forEach(groupTableValidator -> groupTableValidator.validateGroupTable(tableIds));
    }

    public void validateUngroup(final Long tableGroupId) {
        ungroupTableValidators
            .forEach(ungroupTableValidator -> ungroupTableValidator.validateUngroup(tableGroupId));
    }
}
