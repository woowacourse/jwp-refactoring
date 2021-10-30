package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
    public Menu create(final MenuRequest menuRequest) {
        final Long priceRequested = menuRequest.getPrice();

        if (Objects.isNull(priceRequested) || priceRequested < 0L) {
            throw new IllegalArgumentException();
        }

        Long menuGroupId = menuRequest.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        Menu menu = new Menu();
        menu.setName(menuRequest.getName());
        menu.setPrice(BigDecimal.valueOf(priceRequested));
        menu.setMenuGroup(menuGroup);

        final Menu savedMenu = menuRepository.save(menu);

        BigDecimal sum = BigDecimal.ZERO;
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            Long quantity = menuProductRequest.getQuantity();

            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setMenu(savedMenu);
            menuProduct.setProduct(product);
            menuProduct.setQuantity(quantity);
        }

        if (BigDecimal.valueOf(priceRequested).compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
