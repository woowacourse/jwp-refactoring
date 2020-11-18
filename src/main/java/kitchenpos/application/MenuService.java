package kitchenpos.application;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
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
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final Menu menu) {
        // todo: Price를 Money 객체
        final Money price = menu.getPrice();

        validPriceIsNullOrMinus(price);
        validMenuGroupIsNotExist(menu.getMenuGroup());

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        validSumIsLowerThanPrice(price, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();

        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(savedMenu.getId());
            savedMenuProducts.add(menuProductRepository.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    private void validSumIsLowerThanPrice(Money price, List<MenuProduct> menuProducts) {
        Money sum = Money.ZERO;

        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            // todo: product Money 객체로 변경 후 리팩토링 필
            sum = sum.plus(new Money(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())).longValue()));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private void validMenuGroupIsNotExist(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validPriceIsNullOrMinus(Money price) {
        if (Objects.isNull(price) || price.compareTo(Money.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
