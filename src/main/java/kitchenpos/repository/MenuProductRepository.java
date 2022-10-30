package kitchenpos.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.ui.dto.MenuProductDto;
import org.springframework.stereotype.Component;

@Component
public class MenuProductRepository {

    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuProductRepository(MenuProductDao menuProductDao, ProductDao productDao) {
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    public List<MenuProduct> save(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(menuProductDao::save)
                .collect(Collectors.toList());
    }

    public BigDecimal getMenuProductsPrice(List<MenuProductDto> menuProductDtos) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productDao.findById(menuProductDto.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
        }

        return sum;
    }
}
