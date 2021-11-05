package kitchenpos.menu.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.ProductQuantities;
import kitchenpos.menu.domain.ProductQuantity;
import kitchenpos.menu.ui.dto.MenuProductRequest;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuProductService {
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuProductService(MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public List<MenuProduct> create(MenuRequest menuRequest, Menu menu) {
        ProductQuantities productQuantities = combineProductAndQuantity(menuRequest.getMenuProducts());
        menu.validateTotalPrice(productQuantities.totalPrice());
        return menuProductRepository.saveAll(productQuantities.groupToMenuProduct(menu));
    }

    private ProductQuantities combineProductAndQuantity(List<MenuProductRequest> menuProductRequests) {
        List<ProductQuantity> productQuantities = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository
                    .findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new NonExistentException("상품을 찾을 수 없습니다."));
            productQuantities.add(new ProductQuantity(product, menuProductRequest.getQuantity()));
        }
        return new ProductQuantities(productQuantities);
    }

    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenuId(menuId);
    }
}
