package kitchenpos.dao;

import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {
//    MenuGroup save(MenuGroup entity);
//
//    Optional<MenuGroup> findById(Long id);
//
//    List<MenuGroup> findAll();
//
//    boolean existsById(Long id);
}
