package kitchenpos.application.table;

public interface TableGroupingService {

    void isAbleToGroup(final Long orderTableId);

    void isAbleToUngroup(final Long orderTableId);
}
