package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.collection.MenuProducts;
import kitchenpos.domain.entity.MenuProduct;
import kitchenpos.repository.MenuProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuProductService {

    private MenuProductRepository menuProductRepository;

    public MenuProductService(MenuProductRepository menuProductRepository) {
        this.menuProductRepository = menuProductRepository;
    }

    public MenuProduct findMenuProduct(Long menuProductId) {
        return menuProductRepository.findById(menuProductId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public MenuProducts findMenuProducts(List<Long> menuProductIds) {
        List<MenuProduct> menuProducts = menuProductIds.stream()
                .map(this::findMenuProduct)
                .collect(Collectors.toList());
        return new MenuProducts(menuProducts);
    }
}
