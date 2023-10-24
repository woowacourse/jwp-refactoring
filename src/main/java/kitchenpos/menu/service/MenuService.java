package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.dto.MenuProductDto;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.value.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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

    public MenuResponse create(final CreateMenuRequest request) {
        final Price price = new Price(request.getPrice());

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchDataException("해당하는 id의 메뉴 그룹이 없습니다."));

        final List<Long> productIds = request.getMenuProducts().stream()
                .map(MenuProductDto::getProductId)
                .collect(Collectors.toList());

        final List<Product> products = productRepository.findAllById(productIds);

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(dto -> MenuProduct.of(dto, products))
                .collect(Collectors.toList());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculateTotal());
        }

        price.isValidPrice(sum);

        final Menu menu = new Menu(
                request.getName(),
                new Price(request.getPrice()),
                menuGroup,
                menuProducts
        );

        final Menu savedMenu = menuRepository.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            final MenuProduct newMenuProduct = new MenuProduct(
                    menuProduct.getMenuId(),
                    menuId,
                    menuProduct.getProduct(),
                    menuProduct.getQuantity()
            );
            savedMenuProducts.add(menuProductRepository.save(newMenuProduct));
        }

        return MenuResponse.from(Menu.of(savedMenu, savedMenuProducts));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
