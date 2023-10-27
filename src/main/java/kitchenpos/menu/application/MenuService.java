package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.application.dto.MenuCreateDto;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
        return new Menu(request.getName(), new Price(request.getPrice()), menuGroup, getMenuProducts(request));
    }

    private List<MenuProduct> getMenuProducts(final MenuCreateDto request) {
        return request.getMenuProducts().stream()
                .map(menuProductDto -> {
                    Product product = productRepository.findById(menuProductDto.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
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
