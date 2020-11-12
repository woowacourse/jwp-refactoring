package kitchenpos.application;

import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductDto;
import org.springframework.stereotype.Service;

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

    public List<MenuProduct> createMenuProduct(Menu menu, List<MenuProductDto> menuProductDtos) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(null, menu, product, menuProductDto.getQuantity()));
        }
        validateTotalAmount(menu, menuProducts);
        return menuProductRepository.saveAll(menuProducts);
    }

    private void validateTotalAmount(Menu menu, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        menuProducts.forEach(menuProduct -> sum.add(menuProduct.calculateAmount()));
        if (menu.isSmaller(sum)) {
            throw new IllegalArgumentException("MenuProduct 전부를 합한 금액이 Menu 금액보다 작을 수 없습니다.");
        }
    }

    public List<MenuProduct> findAll() {
        return menuProductRepository.findAll();
    }
}
