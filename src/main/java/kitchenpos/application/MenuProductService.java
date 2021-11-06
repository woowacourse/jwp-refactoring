package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
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

    public List<MenuProduct> create(Menu menu, MenuRequest menuRequest) {
        BigDecimal totalPrice = calculateTotalPrice(menuRequest.getMenuProducts(), BigDecimal.ZERO);
        menu.validateTotalPrice(totalPrice);
        return mapToMenuProduct(menu, menuRequest);
    }

    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return menuProductRepository.findAllByMenuId(menuId);
    }

    private List<MenuProduct> mapToMenuProduct(Menu menu, MenuRequest menuRequest) {

        return menuRequest.getMenuProducts().stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId()).get();
                    MenuProduct newMenuProduct = new MenuProduct(menu, product, menuProduct.getQuantity());
                    return menuProductRepository.save(newMenuProduct);
                })
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalPrice(List<MenuProduct> menuProducts, BigDecimal totalPrice) {
        for (MenuProduct menuProduct : menuProducts) {
            Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
            BigDecimal price = product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }
}
