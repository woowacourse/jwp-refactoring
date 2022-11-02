package kitchenpos.order.repository;

import kitchenpos.order.domain.OrderTable;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends TableRepositoryCustom, JpaRepository<OrderTable, Long> {

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
