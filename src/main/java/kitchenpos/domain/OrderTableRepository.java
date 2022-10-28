package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    @Query("select ot from OrderTable ot where ot.id in (:ids)")
    List<OrderTable> findAllByIn(@Param("ids") List<Long> ids);
}
