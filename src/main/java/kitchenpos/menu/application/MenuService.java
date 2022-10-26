package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuDao;
import kitchenpos.menu.repository.MenuGroupDao;
import kitchenpos.menu.repository.ProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.Quantity;
import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.ui.dto.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(final MenuDao menuDao, final MenuGroupDao menuGroupDao, final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        validateExistMenuGroup(request);
        // TODO: 2022/10/26 to pretty
        final List<MenuProduct> menuProducts = toMenuProducts(request);
        return menuDao.save(request.toMenu(menuProducts));
    }

    private void validateExistMenuGroup(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new NotFoundException(CustomErrorCode.MENU_GROUP_NOT_FOUND_ERROR);
        }
    }

    private List<MenuProduct> toMenuProducts(final MenuCreateRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(mpr -> new MenuProduct(
                        findProductById(mpr.getProductId()),
                        new Quantity(mpr.getQuantity())
                )).collect(Collectors.toList());
    }

    private Product findProductById(final Long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new NotFoundException(CustomErrorCode.PRODUCT_NOT_FOUND_ERROR));
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
