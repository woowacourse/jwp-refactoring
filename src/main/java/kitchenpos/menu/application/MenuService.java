package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.request.MenuProductRequest;
import kitchenpos.menu.application.dto.request.MenuRequest;
import kitchenpos.menu.application.dto.response.MenuProductResponse;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.dao.MenuDao;
import kitchenpos.menu.dao.MenuGroupDao;
import kitchenpos.menu.dao.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.exception.InvalidMenuGroupException;
import kitchenpos.product.dao.ProductDao;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.InvalidProductException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao, MenuGroupDao menuGroupDao, MenuProductDao menuProductDao, ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public Long create(MenuRequest menuRequest) {
        validateMenuGroup(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = mapToMenuProducts(menuRequest.getMenuProducts());
        Menu menu = Menu.create(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuProducts);
        Long menuId = menuDao.save(menu);
        saveMenuProducts(menuId, menuProducts);
        return menuId;
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new InvalidMenuGroupException("메뉴 그룹이 존재하지 않습니다.");
        }
    }

    private void saveMenuProducts(Long menuId, List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProductDao.save(new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity(),
                    menuProduct.getPrice()));
        }
    }

    private List<MenuProduct> mapToMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(menuProductRequest -> {
                    Product product = getProduct(menuProductRequest.getProductId());
                    return new MenuProduct(menuProductRequest.getProductId(),
                            menuProductRequest.getQuantity(), product.getPrice());
                }).collect(Collectors.toList());
    }

    private Product getProduct(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(() -> new InvalidProductException("상품이 존재하지 않습니다."));
    }

    private MenuResponse mapToMenuResponse(Menu menu, List<MenuProduct> menuProducts) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), mapToMenuProductResponses(menuProducts));
    }

    private List<MenuProductResponse> mapToMenuProductResponses(List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(),
                        menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> {
                    List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
                    return mapToMenuResponse(menu, menuProducts);
                })
                .collect(Collectors.toList());
    }
}
