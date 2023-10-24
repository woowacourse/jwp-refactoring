package kitchenpos.domain.table;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTableRepository extends CrudRepository<OrderTable, Long> {

    List<OrderTable> findAll();
}
