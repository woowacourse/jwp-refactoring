package kitchenpos.tablegroup.application;

import java.util.List;

public interface OrderTablesStatusValidator {

    void validateIsComplete(List<Long> orderTableId);
}
