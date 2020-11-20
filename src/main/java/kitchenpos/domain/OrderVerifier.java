package kitchenpos.domain;

import java.util.List;

public interface OrderVerifier {
    OrderTable verifyOrderStatusByTableId(Long orderTableId);

    List<OrderTable> verifyOrderStatusByTableGroup(Long tableGroupId);
}
