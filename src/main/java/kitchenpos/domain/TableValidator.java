package kitchenpos.domain;

public interface TableValidator {

    void ableToChangeEmpty(OrderTable orderTableId);

    void ableToChangeNumberOfGuests(OrderTable orderTable);

    void ableToUngroup(TableGroup tableGroup);
}
