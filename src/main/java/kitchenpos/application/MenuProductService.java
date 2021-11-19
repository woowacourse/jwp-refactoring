package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuProductService {

    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuProductService(MenuProductRepository menuProductRepository, ProductRepository productRepository) {
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    public List<MenuProduct> create(Menu menu, List<MenuProductRequest> menuProductRequests) {
        BigDecimal totalPrice = calculateTotalPrice(menuProductRequests, BigDecimal.ZERO);
        menu.validateTotalPrice(totalPrice);

        return mapToMenuProduct(menu, menuProductRequests);
    }

    private BigDecimal calculateTotalPrice(List<MenuProductRequest> menuProductRequests, BigDecimal totalPrice) {
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity()));
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }

    private List<MenuProduct> mapToMenuProduct(Menu menu, List<MenuProductRequest> menuProductRequests) {

        return menuProductRequests.stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId()).get();
                    MenuProduct newMenuProduct = new MenuProduct(menu, product, menuProduct.getQuantity());
                    return menuProductRepository.save(newMenuProduct);
                })
                .collect(Collectors.toList());
    }

    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenuId(menuId);
    }
}
