package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateDto;
import kitchenpos.dto.MenuDto;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuRepository menuRepository;

    public MenuService(final MenuGroupRepository menuGroupRepository, final ProductRepository productRepository,
                       final MenuRepository menuRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuDto create(final MenuCreateDto request) {
        final Menu menu = getMenu(request);
        menuRepository.save(menu);

        return MenuDto.toDto(menu);
    }

    private Menu getMenu(final MenuCreateDto request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        return new Menu(request.getName(), new Price(request.getPrice()), menuGroup, getMenuProducts(request));
    }

    private List<MenuProduct> getMenuProducts(final MenuCreateDto request) {
        return request.getMenuProducts().stream()
                .map(menuProductDto -> {
                    Product product = productRepository.findById(menuProductDto.getProductId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new MenuProduct(product, menuProductDto.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public List<MenuDto> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuDto::toDto)
                .collect(Collectors.toList());
    }
}
