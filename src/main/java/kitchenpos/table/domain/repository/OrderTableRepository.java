package kitchenpos.table.domain.repository;

import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {

    default OrderTable getByIdOrThrow(final long id) {
        return findById(id)
                .orElseThrow(() -> new NoEntityException("존재하지 않는 테이블입니다."));
    }

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);

    class NoEntityException extends RuntimeException {

        public NoEntityException(final String message) {
            super(message);
        }
    }
}
