package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.exception.NotFoundProductException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductDao productDao;

    public MenuService(MenuRepository menuRepository, ProductDao productDao) {
        this.menuRepository = menuRepository;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(MenuCreateRequest menuCreateRequest) {
        List<MenuProduct> menuProducts = getMenuProducts(menuCreateRequest.getMenuProducts());
        validatePrice(menuProducts, menuCreateRequest.getPrice());
        Menu menu = new Menu(
                menuCreateRequest.getName(),
                menuCreateRequest.getPrice(),
                menuCreateRequest.getMenuGroupId());

        return menuRepository.save(menu, menuProducts);
    }

    private List<MenuProduct> getMenuProducts(List<MenuProductDto> menuProductDtos) {
        return menuProductDtos.stream()
                .map(menuProductDto -> new MenuProduct(menuProductDto.getProductId(), menuProductDto.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validatePrice(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProduct menuProduct : menuProducts) {
            Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(NotFoundProductException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new MenuPriceException();
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
