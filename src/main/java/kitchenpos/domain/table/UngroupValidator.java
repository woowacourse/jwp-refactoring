package kitchenpos.domain.table;

import java.util.List;

public interface UngroupValidator {

    void validate(List<Long> orderTableIds);
}
