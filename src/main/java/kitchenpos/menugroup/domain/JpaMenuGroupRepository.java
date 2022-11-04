package kitchenpos.menugroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long>, MenuGroupRepository {
}
