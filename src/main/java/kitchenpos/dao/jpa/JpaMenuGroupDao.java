package kitchenpos.dao.jpa;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupDao extends MenuGroupDao, JpaRepository<MenuGroup, Long> {
}
