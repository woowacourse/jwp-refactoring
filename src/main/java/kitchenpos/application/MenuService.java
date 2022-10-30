package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
    private final MenuProductDao menuProductDao;
    private final ProductDao productRepository;

    public MenuService(final MenuDao menuDao,
                       final MenuGroupDao menuGroupDao,
                       final MenuProductDao menuProductDao,
                       final ProductDao productRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        final List<MenuProduct> menuProducts = toMenuProducts(menuRequest.getMenuProducts());
        validateDiscount(menuRequest.getPrice(), menuProducts);

        final Menu savedMenu = menuDao.save(toMenu(menuRequest));

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.updateMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.updateMenuProducts(savedMenuProducts);

        return toMenuResponse(savedMenu, toMenuProductResponses(menuId, savedMenuProducts));
    }

    private void validateDiscount(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getPrice());
        }

        if (price.compareTo(sum) > 0) {
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
                menuRequest.getMenuGroupId());
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

        for (final Menu menu : menus) {
            menu.updateMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
                .map(menu -> toMenuResponse(menu, toMenuProductResponses(menu.getId(), menu.getMenuProducts())))
                .collect(Collectors.toList());
    }
}
