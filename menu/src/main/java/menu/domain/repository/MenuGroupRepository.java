package menu.domain.repository;

import menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    default MenuGroup getByIdOrThrow(final long id) {
        return findById(id)
                .orElseThrow(() -> new NoEntityException("존재하지 않는 메뉴 그룹입니다."));
    }

    class NoEntityException extends RuntimeException {

        public NoEntityException(final String message) {
            super(message);
        }
    }
}
