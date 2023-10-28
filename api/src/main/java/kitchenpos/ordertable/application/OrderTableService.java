package kitchenpos.ordertable.application;

import java.util.List;

public interface OrderTableService {

    void checkOrderStatusInCookingOrMeal(Long orderTableId);

    void checkOrdersStatusInCookingOrMeal(List<Long> orderTableIds);

}
