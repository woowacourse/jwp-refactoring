package kitchenpos.application;

import kitchenpos.domain.ProductQuantity;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ProductQuantity> productQuantities = combineProductAndQuantity(menuRequest.getMenuProducts());
        menu.validateTotalPrice(totalPrice(productQuantities));
        return saveMenuProducts(productQuantities, menu);
    }

    private List<ProductQuantity> combineProductAndQuantity(List<MenuProductRequest> menuProductRequests) {
        List<ProductQuantity> productQuantities = new ArrayList<>();
        menuProductRequests.forEach(menuProductRequest -> {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("상품을 찾을 수 없습니다."));
            productQuantities.add(new ProductQuantity(product, menuProductRequest.getQuantity()));
        });
        return productQuantities;
    }

    private BigDecimal totalPrice(List<ProductQuantity> productQuantities) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ProductQuantity productQuantity : productQuantities) {
            totalPrice = totalPrice.add(productQuantity.multiply());
        }
        return totalPrice;
    }

    private List<MenuProduct> saveMenuProducts(List<ProductQuantity> productQuantities, Menu menu) {
        return productQuantities.stream()
                .map(productQuantity ->
                        menuProductRepository.save(new MenuProduct(menu, productQuantity))
                ).collect(Collectors.toList());
    }

    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenuId(menuId);
    }
}
