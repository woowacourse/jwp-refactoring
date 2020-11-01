package kitchenpos.domain.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;

@Service
public class MenuCreateService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductDao productDao;

    public MenuCreateService(MenuGroupRepository menuGroupRepository, ProductDao productDao) {
        this.menuGroupRepository = menuGroupRepository;
        this.productDao = productDao;
    }

    public void validate(Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        validateMenuGroup(menuGroupId);
        validatePrice(price, menuProducts);
    }

    private void validateMenuGroup(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(
                    product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }
}
