package kitchenpos.application.tablegroup;

import java.util.List;

public interface OrderTablesStatusValidator {

    void validateIsComplete(List<Long> orderTableId);
}
