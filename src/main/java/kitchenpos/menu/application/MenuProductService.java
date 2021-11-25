package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.repository.MenuProductDao;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

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
            menuProducts.add(new MenuProduct(null, menu, product.getId(), menuProductRequest.getQuantity()));
        }
        return menuProductDao.saveAll(menuProducts);
    }

    public BigDecimal calculateSavedPrice(List<MenuProductRequest> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProducts) {
            final Product product = productService.findById(menuProductRequest.getProductId());
            sum = sum.add(product.calculatePrice(menuProductRequest.getQuantity()));
        }
        return sum;
    }
}
