package kitchenpos.domain.repository;

import kitchenpos.domain.table.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select ot from OrderTable ot join fetch ot.tableGroup t where ot.id = :id")
    Optional<OrderTable> findByIdUsingFetchJoin(Long id);

    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
