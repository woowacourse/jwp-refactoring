package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroup;

public interface MenuGroupDao extends JpaRepository<MenuGroup, Long> {

    boolean existsById(Long id);

}
