package kitchenpos.repository;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupDao {
}
