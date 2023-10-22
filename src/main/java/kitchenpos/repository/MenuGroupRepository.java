package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupDao {

    @Override
    MenuGroup save(MenuGroup entity);

    @Override
    Optional<MenuGroup> findById(Long id);

    @Override
    List<MenuGroup> findAll();

    @Override
    boolean existsById(Long id);
}
