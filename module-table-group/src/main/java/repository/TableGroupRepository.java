package repository;

import domain.TableGroup;
import exception.TableGroupException.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableGroupRepository extends JpaRepository<TableGroup, Long> {

    default TableGroup getById(final Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(id));
    }
}
