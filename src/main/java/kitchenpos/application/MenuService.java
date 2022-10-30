package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productRepository;

    public MenuService(final MenuDao menuDao,
                       final MenuGroupDao menuGroupDao,
                       final ProductDao productRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(toMenuProducts(menuRequest.getMenuProducts()));
        validateDiscount(menuRequest.getPrice(), menuProducts);

        final Menu savedMenu = menuDao.save(toMenu(menuRequest));

        menuProducts.updateMenuId(savedMenu.getId());

        return toMenuResponse(savedMenu, toMenuProductResponses(savedMenu.getId(), savedMenu.getMenuProducts()));
    }

    private void validateDiscount(BigDecimal price, MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.getSum()) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 내부 모든 상품가격보다 낮아야 한다.");
        }
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹은 DB에 등록되어야 한다.");
        }
    }

    private Menu toMenu(MenuRequest menuRequest) {
        return new Menu(null, menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                toMenuProducts(menuRequest.getMenuProducts()));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest mp) {
        Long productId = mp.getProductId();
        return new MenuProduct(productId, mp.getQuantity(), productRepository.findById(productId).getPrice());
    }

    private List<MenuProductResponse> toMenuProductResponses(Long menuId, List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(mp -> new MenuProductResponse(mp.getSeq(), menuId, mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
    }

    private MenuResponse toMenuResponse(Menu menu, List<MenuProductResponse> menuProductResponses) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(menu -> toMenuResponse(menu, toMenuProductResponses(menu.getId(), menu.getMenuProducts())))
                .collect(Collectors.toList());
    }
}
