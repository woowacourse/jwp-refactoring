package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.menu.domain.dao.MenuProductDao;
import kitchenpos.menu.exception.InvalidMenuPriceCreateException;
import kitchenpos.menu.application.dto.MenuSaveRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.dao.ProductDao;
import kitchenpos.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao, MenuProductDao menuProductDao, ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    public MenuResponse create(MenuSaveRequest request) {
        Menu menu = menuDao.save(request.toEntity());
        List<MenuProduct> menuProducts = request.toMenuProductEntities(menu.getId());
        menu.validateMenuPrice(sumMenuProductsTotalPrice(menuProducts));
        for (MenuProduct menuProduct : menuProducts) {
            menuProductDao.save(menuProduct);
        }
        return MenuResponse.toResponse(menu);
    }

    private Price sumMenuProductsTotalPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(this::sumMenuProductPrice)
            .reduce(Price::sum)
            .orElseThrow(InvalidMenuPriceCreateException::new);
    }

    private Price sumMenuProductPrice(MenuProduct menuProduct) {
        Product product = findProduct(menuProduct.getProductId());
        return menuProduct.calculateTotalPrice(product.getPrice());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
       return menuDao.findAll().stream()
            .map(MenuResponse::toResponse)
            .collect(Collectors.toUnmodifiableList());
    }

    private Product findProduct(Long productId) {
        return productDao.findById(productId)
            .orElseThrow(ProductNotFoundException::new);
    }
}
