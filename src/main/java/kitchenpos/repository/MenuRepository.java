package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("select m " +
           "from Menu m " +
           "join fetch MenuGroup mg on mg.id = m.menuGroup.id ")
    List<Menu> joinMenuGroupAll();

    long countByIdIn(final List<Long> ids);
}
