package kitchenpos.menugroup.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
    @Override
    List<MenuGroup> findAll();

    boolean existsById(Long id);
}
