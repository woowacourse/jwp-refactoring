package domain;

import java.util.List;

public interface TableGroupValidator {

    void validate(final List<Long> orderTables);

    void validateUnGroup(final Long orderTable);
}
