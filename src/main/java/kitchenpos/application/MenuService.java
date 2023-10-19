package kitchenpos.application;

import static java.util.stream.Collectors.toUnmodifiableMap;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuDto;
import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductDao menuProductDao;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductDao menuProductDao, final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductDao = menuProductDao;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuDto::from)
            .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        final BigDecimal price = menuDto.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(menuDto.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<Long> productIds = menuDto.getMenuProducts()
            .stream()
            .map(MenuProductDto::getProductId)
            .collect(Collectors.toUnmodifiableList());
        final List<Product> products = productRepository.findAllById(productIds);
        validatePrice(menuDto.getMenuProducts(), price, products);

        final Menu menu = new Menu(menuDto.getName(), menuDto.getPrice(), menuDto.getMenuGroupId());
        final Map<Long, Long> productQuantityMap = menuDto.getMenuProducts()
            .stream()
            .collect(toUnmodifiableMap(MenuProductDto::getProductId, MenuProductDto::getQuantity));
        menu.addProducts(products, productQuantityMap);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuDto.from(savedMenu);
    }

    private void validatePrice(final List<MenuProductDto> menuProductDtos, final BigDecimal price,
        final List<Product> products) {
        final BigDecimal sum = sumMenuProductPrices(products, menuProductDtos);

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    private BigDecimal sumMenuProductPrices(
        final List<Product> products,
        final List<MenuProductDto> menuProducts
    ) {
        final Map<Long, BigDecimal> productPriceMap = products.stream()
            .collect(Collectors.toMap(Product::getId, Product::getPrice));

        return menuProducts.stream()
            .map(menuProductDto -> productPriceMap.get(menuProductDto.getProductId())
                .multiply(BigDecimal.valueOf(menuProductDto.getQuantity())
                ))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
