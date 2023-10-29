package menu.domain.repository;

import menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    default Menu getByIdOrThrow(final long id) {
        return findById(id)
                .orElseThrow(() -> new NoEntityException("존재하지 않는 메뉴입니다."));
    }

    class NoEntityException extends RuntimeException {

        public NoEntityException(final String message) {
            super(message);
        }
    }
}
