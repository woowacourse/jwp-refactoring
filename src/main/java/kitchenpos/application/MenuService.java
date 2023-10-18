package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        if (!menuGroupRepository.existsById(menu.getMenuGroup().getId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity().getValue())).getValue());
        }

        if (menu.isGreaterThan(sum)) {
            throw new IllegalArgumentException();
        }

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.joinMenuGroupAll();
    }
}
