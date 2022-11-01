package kitchenpos.repository;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.repository.Repository;

public interface JpaMenuRepository extends Repository<Menu, Long>, MenuRepository {
}
