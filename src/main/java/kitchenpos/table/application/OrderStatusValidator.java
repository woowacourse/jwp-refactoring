package kitchenpos.table.application;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface OrderStatusValidator {

    boolean existsByOrderTableIdInAndOrderStatusNotCompletion(final List<Long> orderTableIds);

    boolean existsByOrderTableIdAndOrderStatusNotCompletion(final Long orderTableId);
}
