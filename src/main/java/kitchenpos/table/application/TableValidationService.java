package kitchenpos.table.application;

public interface TableValidationService {

    void validateChangeEmptyAvailable(final Long orderTableId);

    void validateUngroupAvailable(final Long orderTableId);
}
