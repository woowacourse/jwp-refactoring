package kitchenpos.menu.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Override
    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
