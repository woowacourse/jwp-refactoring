package kitchenpos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import kitchenpos.domain.MenuGroup;

public interface MenuGroupRepository extends Repository<MenuGroup, Long> {

    MenuGroup save(final MenuGroup entity);

    Optional<MenuGroup> findById(final Long id);

    List<MenuGroup> findAll();

}
