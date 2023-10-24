package kitchenpos.application;

import static kitchenpos.domain.Price.ZERO_PRICE;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.MenuCreateRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.mapper.MenuMapper;
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
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(
            final MenuCreateRequest request
    ) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 menu group 입니다."));

        final List<MenuProduct> menuProducts = convertToMenuProducts(request);
        final Menu menu = MenuMapper.toMenu(request, menuGroup);
        validateMenuPrice(menu, calculateSumByMenuProducts(menuProducts));
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProduct> savedMenuProducts = saveMenuProducts(savedMenu, menuProducts);
        return MenuMapper.toMenuResponse(savedMenu, savedMenuProducts);
    }

    private List<MenuProduct> convertToMenuProducts(
            final MenuCreateRequest menuRequest
    ) {
        return menuRequest.getMenuProducts().stream()
                .map(request -> {
                    final Product product = productRepository.findById(request.getProductId())
                            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 product 입니다."));
                    return new MenuProduct(null, product, request.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private Price calculateSumByMenuProducts(
            final List<MenuProduct> menuProducts
    ) {
        return menuProducts.stream()
                .map(MenuProduct::getTotalPrice)
                .reduce(ZERO_PRICE, Price::add);
    }

    private void validateMenuPrice(
            final Menu menu,
            final Price price
    ) {
        if (menu.isGreaterThan(price)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    private List<MenuProduct> saveMenuProducts(
            final Menu menu,
            final List<MenuProduct> menuProducts
    ) {
        return menuProducts.stream()
                .map(menuProduct -> menuProductRepository.save(
                        new MenuProduct(menu, menuProduct.getProduct(), menuProduct.getQuantity())
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> readAll() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuMapper.toMenuResponses(menus);
    }
}
