package kitchenpos.menu.application;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static kitchenpos.menu.domain.exception.MenuExceptionType.MENU_GROUP_IS_NOT_FOUND;
import static kitchenpos.menu.domain.exception.MenuExceptionType.MENU_PRODUCT_IS_CONTAIN_NOT_SAVED_PRODUCT;
import static kitchenpos.menu.domain.exception.MenuExceptionType.PRICE_IS_BIGGER_THAN_MENU_PRODUCT_PRICES_SUM;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuDto;
import kitchenpos.menu.application.dto.MenuProductDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.exception.MenuException;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.vo.Price;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<MenuDto> list() {
        return menuRepository.findAll()
            .stream()
            .map(MenuDto::from)
            .collect(toUnmodifiableList());
    }

    @Transactional
    public MenuDto create(final MenuDto menuDto) {
        final Price price = new Price(menuDto.getPrice());

        if (!menuGroupRepository.existsById(menuDto.getMenuGroupId())) {
            throw new MenuException(MENU_GROUP_IS_NOT_FOUND);
        }

        final List<Product> products = findProducts(menuDto, price);

        final Menu menu = new Menu(menuDto.getName(), price, menuDto.getMenuGroupId());
        addMenuProducts(menuDto, products, menu);
        final Menu savedMenu = menuRepository.save(menu);

        return MenuDto.from(savedMenu);
    }

    private List<Product> findProducts(final MenuDto menuDto, final Price price) {
        final List<Long> productIds = menuDto.getMenuProducts()
            .stream()
            .map(MenuProductDto::getProductId)
            .collect(toUnmodifiableList());
        final List<Product> products = productRepository.findAllById(productIds);

        validatePrice(menuDto.getMenuProducts(), price, products);

        return products;
    }

    private static void addMenuProducts(
        final MenuDto menuDto, final List<Product> products, final Menu menu
    ) {

        final Map<Long, Long> productQuantityMap = menuDto.getMenuProducts()
            .stream()
            .collect(toUnmodifiableMap(MenuProductDto::getProductId, MenuProductDto::getQuantity));

        menu.addProducts(products, productQuantityMap);
    }

    private void validatePrice(final List<MenuProductDto> menuProductDtos, final Price price,
        final List<Product> products) {
        if (menuProductDtos.size() != products.size()) {
            throw new MenuException(MENU_PRODUCT_IS_CONTAIN_NOT_SAVED_PRODUCT);
        }

        final Price sum = sumMenuProductPrices(products, menuProductDtos);

        if (price.isBigger(sum)) {
            throw new MenuException(PRICE_IS_BIGGER_THAN_MENU_PRODUCT_PRICES_SUM);
        }
    }

    private Price sumMenuProductPrices(
        final List<Product> products,
        final List<MenuProductDto> menuProducts
    ) {
        final Map<Long, Price> productPriceMap = products.stream()
            .collect(Collectors.toMap(Product::getId, Product::getPrice));

        return menuProducts.stream()
            .map(menuProductDto -> productPriceMap.get(menuProductDto.getProductId())
                .multiply(menuProductDto.getQuantity()))
            .reduce(Price.ZERO, Price::add);
    }
}
