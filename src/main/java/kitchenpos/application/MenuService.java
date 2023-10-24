package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.entity.MenuGroup;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.domain.entity.Product;
import kitchenpos.domain.value.Price;
import kitchenpos.dto.request.menu.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;


    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final CreateMenuRequest request) {
        final Price price = new Price(request.getPrice());

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(MenuProduct::from)
                .collect(Collectors.toList());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productRepository.findById(menuProduct.getProduct())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(menuProduct.getQuantity()));
        }

        price.isValidPrice(sum);

        final Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .menuGroup(menuGroup)
                .menuProducts(menuProducts)
                .build();

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct newMenuProduct = new MenuProduct(
                    menuProduct.getMenu(),
                    menuId,
                    menuProduct.getProduct(),
                    menuProduct.getQuantity()
            );
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }

        return MenuResponse.from(Menu.builder()
                .id(savedMenu.getId())
                .name(savedMenu.getName())
                .price(savedMenu.getPrice())
                .menuGroup(menuGroup)
                .menuProducts(savedMenuProducts)
                .build()
        );
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
