package kitchenpos.table_group.domain;

import java.util.List;

public interface GroupTableValidator {

    void validateGroupTable(List<Long> tableIds);
}
