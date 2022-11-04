package kitchenpos.table.application;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface UngroupValidator {

    void validateUngroup(List<Long> orderTableIds);
}
