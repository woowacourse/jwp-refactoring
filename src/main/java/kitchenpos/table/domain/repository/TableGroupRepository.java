package kitchenpos.table.domain.repository;

import kitchenpos.table.domain.TableGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getByIdOrThrow(final Long id) {
        return findById(id)
                .orElseThrow(() -> new NoEntityException("존재하지 않는 단체 지정 번호입니다."));
    }

    class NoEntityException extends RuntimeException {

        public NoEntityException(final String message) {
            super(message);
        }
    }
}
