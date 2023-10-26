package kitchenpos.menu.repository;

import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    default List<MenuProduct> getAllById(List<Long> ids) {
        final List<MenuProduct> menuProducts = findAllById(ids);

        if (menuProducts.size() != ids.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 상품이 포함되어 있습니다.");
        }

        return menuProducts;
    }
}
