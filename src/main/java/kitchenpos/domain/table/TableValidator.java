package kitchenpos.domain.table;

public interface TableValidator {

    void ableToChangeEmpty(OrderTable orderTableId);

    void ableToChangeNumberOfGuests(OrderTable orderTable);

    void ableToUngroup(TableGroup tableGroup);
}
