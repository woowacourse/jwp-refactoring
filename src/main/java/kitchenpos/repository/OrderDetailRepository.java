package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findAllByOrderTableTableGroupId(Long tableGroupId);
}
