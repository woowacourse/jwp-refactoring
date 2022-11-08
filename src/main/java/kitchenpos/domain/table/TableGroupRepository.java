package kitchenpos.domain.table;

public interface TableGroupRepository {

    TableGroup add(TableGroup tableGroup);

    TableGroup get(Long id);
}
