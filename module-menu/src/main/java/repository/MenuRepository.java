package repository;

import domain.Menu;
import exception.MenuException;
import org.springframework.data.jpa.repository.JpaRepository;
import repository.customRepositroy.CustomMenuRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, CustomMenuRepository {

    default Menu getById(final Long id) {
        return findById(id).orElseThrow(() -> new MenuException.NotFoundException(id));
    }
}
