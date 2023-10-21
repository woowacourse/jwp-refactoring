package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateDto;
import kitchenpos.application.dto.MenuProductCreateDto;
import kitchenpos.application.exception.ProductAppException.NotFoundProductException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuService(final ProductRepository productRepository,
        final MenuGroupRepository menuGroupRepository, final MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final MenuCreateDto menuCreateDto) {
        final MenuGroup findMenuGroup = menuGroupRepository.findById(menuCreateDto.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        final List<MenuProduct> menuProducts = makeMenuProducts(
            menuCreateDto.getMenuProductCreateDtos());

        final Menu newMenu = Menu.of(menuCreateDto.getName(), menuCreateDto.getPrice(),
            findMenuGroup, menuProducts);

        return menuRepository.save(newMenu);
    }

    private List<MenuProduct> makeMenuProducts(
        final List<MenuProductCreateDto> menuProductCreateDtos) {
        return menuProductCreateDtos.stream()
            .map(
                menuProductCreateDto -> {
                    final Product product = productRepository.findById(
                        menuProductCreateDto.getProductId()).orElseThrow(
                        () -> new NotFoundProductException(menuProductCreateDto.getProductId()));

                    return new MenuProduct(product, menuProductCreateDto.getQuantity());
                })
            .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
