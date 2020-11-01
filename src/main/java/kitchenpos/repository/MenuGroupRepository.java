package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    @Override
    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
