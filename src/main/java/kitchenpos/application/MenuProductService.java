package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuProductService {
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuProductService(ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public List<MenuProduct> createMenuProduct(Menu menu, List<MenuProductRequest> menuProductRequests) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(null, menu, product, menuProductRequest.getQuantity()));
        }
        validateTotalAmount(menu, menuProducts);
        return menuProductRepository.saveAll(menuProducts);
    }

    private void validateTotalAmount(Menu menu, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculateAmount());
        }
        if (menu.isSmallerPrice(sum)) {
            throw new IllegalArgumentException("MenuProduct 전부를 합한 금액이 Menu 금액보다 작을 수 없습니다.");
        }
    }

    public List<MenuProduct> findAll() {
        return menuProductRepository.findAll();
    }
}
