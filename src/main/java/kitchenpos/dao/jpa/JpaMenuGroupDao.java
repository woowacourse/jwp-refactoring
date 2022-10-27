package kitchenpos.dao.jpa;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaMenuGroupDao extends MenuGroupDao, JpaRepository<MenuGroup, Long> {
}
