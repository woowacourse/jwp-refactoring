package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuProductRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import kitchenpos.domain.vo.Money;
import kitchenpos.ui.dto.menu.CreateMenuRequest;
import kitchenpos.ui.dto.menu.MenuProductDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest createMenuRequest) {
        validate(createMenuRequest);

        final Menu menu = menuRepository.save(createMenuRequest.toDomain());
        final List<MenuProduct> menuProducts = createMenuRequest.getMenuProducts().stream()
                .map(menuProductDto -> new MenuProduct(menu.getId(), menuProductDto.getProductId(), menuProductDto.getQuantity()))
                .collect(Collectors.toList());
        menuProductRepository.saveAll(menuProducts);
        return menu;
    }

    private void validate(final CreateMenuRequest createMenuRequest) {
        validateMenuGroupExists(createMenuRequest);
        validatePrice(createMenuRequest);
        validateProductExists(createMenuRequest);
    }

    private void validateMenuGroupExists(final CreateMenuRequest createMenuRequest) {
        if (!menuGroupRepository.existsById(createMenuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePrice(final CreateMenuRequest createMenuRequest) {
        final Money price = Money.valueOf(createMenuRequest.getPrice());
        if (price.isBiggerThan(calculateMoney(createMenuRequest.getMenuProducts()))) {
            throw new IllegalArgumentException();
        }
    }

    private void validateProductExists(final CreateMenuRequest createMenuRequest) {
        createMenuRequest.getMenuProducts()
                .forEach(menuProductDto -> productRepository.findById(menuProductDto.getProductId()).orElseThrow(IllegalArgumentException::new));
    }

    private Money calculateMoney(final List<MenuProductDto> menuProductDtos) {
        Money sum = Money.empty();
        for (final MenuProductDto menuProductDto : menuProductDtos) {
            final Product product = productRepository.findById(menuProductDto.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(Money.valueOf(menuProductDto.getQuantity())));
        }
        return sum;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
