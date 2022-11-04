package kitchenpos.tablegroup.service;

public interface TableValidator {

    void validatePossibleChangeToEmpty(final Long orderTableId);
}
