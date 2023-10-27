package kitchenpos.table.domain;

public interface OrderTableValidator {

    void validateChangeEmpty(Long orderTableId, TableGroup tableGroup);

}
