package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
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
    public MenuDto create(final CreateMenuDto createMenuDto) {
        final Menu menu = Menu.create(
                createMenuDto.getName(),
                createMenuDto.getPrice(),
                createMenuDto.getMenuGroupId(),
                new ArrayList<>()
        );
        createMenuDto.getMenuProducts().forEach(createMenuProductDto ->
                menu.addProduct(createMenuProductDto.getProductId(), createMenuProductDto.getQuantity())
        );
        final BigDecimal price = menu.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenu.addProduct(savedMenuProduct);
        }

        return MenuDto.of(savedMenu);
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menu.getId());
            menuProducts.forEach(menu::addProduct);
        }

        return menus.stream()
                .map(MenuDto::of)
                .collect(Collectors.toList());
    }
}
