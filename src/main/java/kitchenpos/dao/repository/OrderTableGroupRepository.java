package kitchenpos.dao.repository;

import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.OrderTableGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface OrderTableGroupRepository extends JpaRepository<OrderTableGroup, Long>, OrderTableGroupDao {

    boolean existsById(Long orderTableGroupId);
}
