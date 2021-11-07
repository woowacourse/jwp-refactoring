package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT COUNT(m) FROM Menu m WHERE m.id IN :menuIds")
    int countByIdIn(List<Long> menuIds);
}
