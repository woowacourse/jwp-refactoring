package kitchenpos.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Override
    default Menu getById(Long id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }

    long countByIdIn(List<Long> menuIds);
}
