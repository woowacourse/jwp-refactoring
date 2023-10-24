package kitchenpos.repository;

import kitchenpos.domain.Order;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();

    @Query("select o from Order o where o.orderTable.id = :orderTableId")
    Order findByOrderTableId(Long orderTableId);
}
