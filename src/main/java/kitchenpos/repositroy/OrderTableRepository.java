package kitchenpos.repositroy;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.exception.TableException;
import kitchenpos.repositroy.customRepositroy.CustomOrderTableRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long>, CustomOrderTableRepository {

    default OrderTable getById(final Long id) {
        return findById(id).orElseThrow(() -> new TableException.NotFoundException(id));
    }
}
