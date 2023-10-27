package kitchenpos.tablegroup.domain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface OrderTableManager {

    void addOrderTables(final TableGroup tableGroup, final List<Long> OrderTableIds);

    void ungroup(final TableGroup tableGroup);
}
