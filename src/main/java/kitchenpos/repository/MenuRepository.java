package kitchenpos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menu.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(final List<Long> ids);
}
