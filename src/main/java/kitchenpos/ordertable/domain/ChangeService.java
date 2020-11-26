package kitchenpos.ordertable.domain;

public interface ChangeService {
    void ungroup(OrderTables orderTables);

    boolean isCookingOrMeal(Long orderTableId);
}
