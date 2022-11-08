package kitchenpos.table.application;

public interface TableValidator {
    void validateTableNotExists(Long orderTableId);

    void validateTableEmpty(final Long orderTableId);
}
