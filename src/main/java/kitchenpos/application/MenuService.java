package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.dto.response.MenusResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.ProductDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final ProductDao productDao;
    private final MenuGroupDao menuGroupDao;

    public MenuService(final MenuDao menuDao,
                       final ProductDao productDao,
                       final MenuGroupDao menuGroupDao) {
        this.menuDao = menuDao;
        this.productDao = productDao;
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        List<MenuProduct> menuProducts = menuProducts(request.getMenuProducts());
        Menu menu = request.toEntity(menuProducts);
        validateInMenuGroup(menu);
        menuDao.save(menu);
        return MenuResponse.from(menu);
    }

    private List<MenuProduct> menuProducts(final List<MenuProductCreateRequest> requests) {
        return requests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(final MenuProductCreateRequest request) {
        Product product = productDao.findById(request.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        return new MenuProduct(product, request.getQuantity());
    }

    private void validateInMenuGroup(final Menu menu) {
        Long menuGroupId = menu.getMenuGroupId();
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new MenuGroupNotFoundException("등록되지 않은 메뉴그룹 입니다.");
        }
    }

    public MenusResponse list() {
        final List<Menu> menus = menuDao.findAll();
        return MenusResponse.from(menus);
    }
}
