package kitchenpos.domain.table;

import java.util.List;

public interface OrderTableRepository {

    OrderTable get(Long id);

    OrderTable add(OrderTable orderTable);

    List<OrderTable> addAll(List<OrderTable> orderTables);

    List<OrderTable> getAll();
}
