package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateDto;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductCreateDto;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.domain.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuGroupRepository menuGroupRepository, final MenuRepository menuRepository,
                       final ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuDto create(final MenuCreateDto request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final Menu menu = new Menu(request.getName(), new Price(request.getPrice()), menuGroup);

        Price sum = new Price(BigDecimal.ZERO);
        final List<MenuProductCreateDto> menuProducts = request.getMenuProducts();
        for (MenuProductCreateDto menuProductDto : menuProducts) {
            Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(menuProductDto.getQuantity()));

            MenuProduct menuProduct = new MenuProduct(product, menuProductDto.getQuantity());
            menu.addMenuProduct(menuProduct);
        }
        if (menu.getPrice().isBiggerThan(sum)) {
            throw new IllegalArgumentException();
        }

        menuRepository.save(menu);
        return MenuDto.toDto(menu);
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuDto::toDto)
                .collect(Collectors.toList());
    }
}
