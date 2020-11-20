package kitchenpos.domain.verifier;

import java.util.List;

import kitchenpos.domain.OrderTable;

public interface OrderVerifier {
    OrderTable verifyOrderStatusByTableId(Long orderTableId);

    List<OrderTable> verifyOrderStatusByTableGroup(Long tableGroupId);
}
