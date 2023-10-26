package kitchenpos.application;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.domain.*;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuProductRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public Menu create(final MenuCreateRequest request) {
        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> menuProducts = makeMenuProducts(request, menu);
        menuProductRepository.saveAll(menuProducts);

        return savedMenu;
    }

    private MenuGroup findMenuGroupById(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> makeMenuProducts(final MenuCreateRequest request, final Menu menu) {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(menu, product, menuProductDto.getQuantity()));
        }

        checkMenuProductsPrice(menuProducts, menu.getPrice());
        return menuProducts;
    }

    private void checkMenuProductsPrice(final List<MenuProduct> menuProducts, final BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
