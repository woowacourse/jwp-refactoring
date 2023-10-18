package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.dto.MenuCreateDto;
import kitchenpos.application.dto.MenuProductCreateDto;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProductRepository;
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
    private final MenuProductRepository menuProductRepository;

    public MenuService(final ProductRepository productRepository,
        final MenuGroupRepository menuGroupRepository, final MenuRepository menuRepository,
        final MenuProductRepository menuProductRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final MenuCreateDto menuCreateDto) {
        final MenuGroup findMenuGroup = menuGroupRepository.findById(menuCreateDto.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);

        final Menu newMenu = new Menu(menuCreateDto.getName(), menuCreateDto.getPrice(),
            findMenuGroup);
        final Menu savedMenu = menuRepository.save(newMenu);

        final List<MenuProduct> newMenuProducts = makeMenuProduct(menuCreateDto, savedMenu);

        if (savedMenu.isOverPrice()) {
            throw new IllegalArgumentException();
        }

        menuProductRepository.saveAll(newMenuProducts);

        return savedMenu;
    }

    private List<MenuProduct> makeMenuProduct(final MenuCreateDto menuCreateDto, final Menu menu) {
        final List<MenuProduct> newMenuProducts = new ArrayList<>();
        for (final MenuProductCreateDto menuProductCreateDto : menuCreateDto.getMenuProductCreateDtos()) {
            final Product product = productRepository.findById(menuProductCreateDto.getProductId())
                .orElseThrow(IllegalArgumentException::new);

            final MenuProduct newMenuProduct = MenuProduct.of(menu, product,
                menuProductCreateDto.getQuantity());

            newMenuProducts.add(newMenuProduct);
        }
        return newMenuProducts;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
