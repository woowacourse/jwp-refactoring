package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Long create(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<Long> productIds,
            final List<Integer> counts
    ) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        final List<Product> findProducts = findProducts(productIds);
        final Products products = new Products(findProducts);
        products.validateSum(counts, price);

        final Menu menu = menuRepository.save(new Menu(name, new MenuPrice(price), menuGroupId));
        saveMenuProduct(counts, findProducts, menu);
        return menu.getId();
    }

    private void saveMenuProduct(final List<Integer> counts, final List<Product> findProducts, Menu menu) {
        for (int index = 0; index < findProducts.size(); index++) {
            final MenuProduct menuProduct = new MenuProduct(menu, findProducts.get(index).getId(), counts.get(index));
            menu.addMenuProduct(menuProduct);
        }
    }

    private List<Product> findProducts(final List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getById)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
