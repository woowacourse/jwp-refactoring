package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
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

    @Transactional
    public Long create(
            final String name,
            final BigDecimal price,
            final Long menuGroupId,
            final List<Long> productIds,
            final List<Integer> counts
    ) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        List<Product> products = new ArrayList<>();
        for (int index = 0; index < productIds.size(); index++) {
            final Product product = productRepository.findById(productIds.get(index))
                    .orElseThrow(IllegalArgumentException::new);
            products.add(product);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(counts.get(index))));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = menuRepository.save(new Menu(name, price, menuGroupId));
        for (int index = 0; index < products.size(); index++) {
            final MenuProduct menuProduct = new MenuProduct(menu.getId(), products.get(index).getId(), counts.get(index));
            menuProductRepository.save(menuProduct);
        }
        return menu.getId();
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
