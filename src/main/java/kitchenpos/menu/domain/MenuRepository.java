package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);

    List<Menu> findAllByIdIn(List<Long> ids);
}
