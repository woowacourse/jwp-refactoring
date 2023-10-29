package kitchenpos.ordertable.repository;

import kitchenpos.ordertable.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import kitchenpos.tablegroup.TableGroup;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    List<OrderTable> findByTableGroup(final TableGroup tableGroup);

    default OrderTable findMandatoryById(final Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
