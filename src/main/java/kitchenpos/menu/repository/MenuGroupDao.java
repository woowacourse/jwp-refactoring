package kitchenpos.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.MenuGroup;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {

    boolean existsById(Long id);

}
