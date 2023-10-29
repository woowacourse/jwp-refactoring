package repository;

import domain.OrderTable;
import exception.TableException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getById(final Long id) {
        return findById(id).orElseThrow(() -> new TableException.NotFoundException(id));
    }

    List<OrderTable> findByTableGroupId(final Long tableGroupId);

    Long countByIdIn(final List<Long> id);
}
