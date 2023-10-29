package repository;

import domain.MenuGroup;
import exception.MenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(final Long id) {
        return findById(id).orElseThrow(() -> new MenuGroupException.NotFoundException(id));
    }
}
