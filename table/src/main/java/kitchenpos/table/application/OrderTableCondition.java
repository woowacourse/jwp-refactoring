package kitchenpos.table.application;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderTableCondition {

    private final List<UnableToUngroup> unableToUngroupConditions = new ArrayList<>();
    private final List<UnableToChange> unableToChangeConditions = new ArrayList<>();

    public void addUngroupCondition(UnableToUngroup unableToUngroup) {
        unableToUngroupConditions.add(unableToUngroup);
    }

    public void addChangeCondition(UnableToChange unableToChange) {
        unableToChangeConditions.add(unableToChange);
    }

    public boolean isUnableToUngroup(Long tableGroupId) {
        return unableToUngroupConditions.stream()
            .anyMatch(unableToUngroup -> unableToUngroup.unableToUngroup(tableGroupId));
    }

    public boolean isUnableToChange(Long orderTableId) {
        return unableToChangeConditions.stream()
            .anyMatch(unableToChange -> unableToChange.unableToUngroup(orderTableId));
    }
}
