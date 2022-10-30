package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.exception.MenuPriceException;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.ui.dto.MenuProductDto;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(MenuCreateRequest request) {
        menuRepository.validateMenuGroupId(request.getMenuGroupId());
        BigDecimal menuProductsPrice = menuProductRepository.getMenuProductsPrice(request.getMenuProducts());
        validatePrice(menuProductsPrice, request.getPrice());

        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        Menu savedMenu = menuRepository.save(menu);
        List<MenuProduct> menuProducts = getMenuProducts(menu.getId(), request.getMenuProducts());
        List<MenuProduct> savedMenuProducts = menuProductRepository.save(menuProducts);

        return new Menu(savedMenu, savedMenuProducts);
    }

    private void validatePrice(BigDecimal menuProductsPrice, BigDecimal price) {
        if (price.compareTo(menuProductsPrice) > 0) {
            throw new MenuPriceException();
        }
    }

    private List<MenuProduct> getMenuProducts(Long menuId, List<MenuProductDto> menuProductDtos) {
        return menuProductDtos.stream()
                .map(menuProductDto -> new MenuProduct(menuId, menuProductDto.getProductId(),
                        menuProductDto.getQuantity()))
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
