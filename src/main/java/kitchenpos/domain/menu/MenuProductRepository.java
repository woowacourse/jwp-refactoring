package kitchenpos.domain.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {
    public List<MenuProduct> findAllByMenu(Menu menu);
}
