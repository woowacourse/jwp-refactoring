package kitchenpos.domain.verifier;

import java.util.List;

import kitchenpos.domain.OrderTable;

public interface OrderTableVerifier {
    List<OrderTable> verifyOrderTables(List<Long> orderTableIds);
}
