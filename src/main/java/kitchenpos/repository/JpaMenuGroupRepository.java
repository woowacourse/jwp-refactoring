package kitchenpos.repository;

import static kitchenpos.exception.MenuGroupExceptionType.NOT_EXIST_MENU_GROUP_EXCEPTION;

import kitchenpos.domain.MenuGroup;
import kitchenpos.exception.MenuGroupException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getById(Long id) {
        return findById(id).orElseThrow(() -> new MenuGroupException(NOT_EXIST_MENU_GROUP_EXCEPTION));
    }

    @Override
    MenuGroup getReferenceById(Long aLong);
}
