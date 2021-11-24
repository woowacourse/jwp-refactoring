package kitchenpos.domain;

public interface TableGroupValidator {

    void validateGrouping(TableGroup tableGroup);
    void validateUngroup(TableGroup tableGroup);
}
