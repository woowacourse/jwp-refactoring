package kitchenpos.repository;

import kitchenpos.domain.OrderTable;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
