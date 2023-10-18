package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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
            final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public Menu create(final MenuCreateRequest request) {
        final Menu savedMenu = saveMenu(request);
        List<MenuProduct> menuProducts = saveMenuProducts(savedMenu, request.getMenuProducts());
        savedMenu.updateMenuProducts(menuProducts);
        return savedMenu;
    }

    private Menu saveMenu(MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 menu group 입니다."));
        Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup);
        BigDecimal sum = calculateSumByMenuProducts(request.getMenuProducts());
        validatePrice(menu, sum);
        return menuRepository.save(menu);
    }

    private List<MenuProduct> saveMenuProducts(final Menu menu,
                                               final List<MenuProductCreateRequest> requests) {
        final ArrayList<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductCreateRequest request : requests) {
            final Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 product 입니다."));
            final MenuProduct menuProduct = MenuProduct.of(menu, product, request.getQuantity());
            final MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            menuProducts.add(savedMenuProduct);
        }
        return menuProducts;
    }

    private void validatePrice(final Menu menu, final BigDecimal sum) {
        if (menu.isGreaterThanByPrice(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 메뉴 상품 가격 합산 금액보다 클 수 없습니다.");
        }
    }

    private BigDecimal calculateSumByMenuProducts(final List<MenuProductCreateRequest> requests) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest request : requests) {
            final Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        }
        return sum;
    }

    @Transactional(readOnly = true)
    public List<Menu> readAll() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductRepository.findAllByMenu(menu));
        }

        return menus;
    }
}
