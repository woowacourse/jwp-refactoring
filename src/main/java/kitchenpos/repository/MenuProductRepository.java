package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MenuProductRepository extends JpaRepository<MenuProduct, Long> {

    default List<MenuProduct> getAllById(List<Long> ids) {
        final List<MenuProduct> menuProducts = findAllById(ids);

        if (menuProducts.size() != ids.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 상품이 포함되어 있습니다.");
        }

        return menuProducts;
    }
}
