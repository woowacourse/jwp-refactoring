package kitchenpos.repositroy.customRepositroy;

import java.util.List;
import kitchenpos.domain.table.OrderTable;

public interface CustomOrderTableRepository {

    List<OrderTable> findAllByFetch();
}
