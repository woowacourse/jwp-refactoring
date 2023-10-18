package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.Products;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
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
    private final MenuProductRepository menuProductRepository;

    public MenuService(
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository,
            MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public Long create(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<Long> productIds,
            final List<Integer> counts
    ) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        final List<Product> findProducts = findProducts(productIds);
        final Products products = new Products(findProducts);
        final BigDecimal sum = products.calculateSum(counts);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = menuRepository.save(new Menu(name, price, menuGroupId));
        for (int index = 0; index < findProducts.size(); index++) {
            final MenuProduct menuProduct = new MenuProduct(menu.getId(), findProducts.get(index).getId(), counts.get(index));
            menuProductRepository.save(menuProduct);
        }
        return menu.getId();
    }

    private List<Product> findProducts(List<Long> productIds) {
        return productIds.stream()
                .map(productRepository::getById)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
