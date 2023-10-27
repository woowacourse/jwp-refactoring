package kitchenpos.ordertable.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderManager {

    public void validateOrdersToUngroup(final List<Long> orderTableIds);
}
