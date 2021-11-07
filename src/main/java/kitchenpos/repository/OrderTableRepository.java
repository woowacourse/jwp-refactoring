package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("SELECT ot FROM OrderTable ot JOIN TableGroup tg WHERE tg.id = :tableGroupId")
    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
