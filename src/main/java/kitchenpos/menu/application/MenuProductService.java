package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.product.application.ProductService;
import kitchenpos.menu.repository.MenuProductDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;

@Service
public class MenuProductService {

    private final MenuProductDao menuProductDao;
    private final ProductService productService;

    public MenuProductService(MenuProductDao menuProductDao, ProductService productService) {
        this.menuProductDao = menuProductDao;
        this.productService = productService;
    }

    public List<MenuProduct> saveAll(Menu menu, List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productService.findById(menuProductRequest.getProductId());
            menuProducts.add(new MenuProduct(null, menu, product, menuProductRequest.getQuantity()));
        }
        return menuProductDao.saveAll(menuProducts);
    }
}
