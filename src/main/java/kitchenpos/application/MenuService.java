package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.request.MenuProductRequest;
import kitchenpos.dto.menu.request.MenuRequest;
import kitchenpos.dto.menu.response.MenuProductResponse;
import kitchenpos.dto.menu.response.MenuResponse;
import kitchenpos.exception.InvalidRequestParamException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest menuRequest) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        final List<Product> foundProducts = getValidProducts(menuRequest.getPrice(), menuProductRequests);
        final MenuGroup foundMenuGroup = findMenuGroupById(menuRequest.getMenuGroupId());
        final Menu newMenu = saveMenu(menuRequest, foundMenuGroup);
        final List<MenuProduct> newMenuProducts = saveMenuProducts(menuProductRequests, foundProducts, newMenu);

        return createMenuResponse(newMenu, newMenuProducts);
    }

    private List<Product> getValidProducts(BigDecimal requestMenuPrice, List<MenuProductRequest> menuProductRequests) {
        validateRequestMenuPriceIsNonNull(requestMenuPrice);

        BigDecimal allProductsPriceSum = BigDecimal.ZERO;
        final List<Product> foundProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            validateQuantityNonNull(menuProductRequest.getQuantity());

            final Product foundProduct = findProductById(menuProductRequest.getProductId());
            foundProducts.add(foundProduct);

            final BigDecimal productsPriceSum = getProductsPriceSum(foundProduct.getPrice(), menuProductRequest.getQuantity());
            allProductsPriceSum = allProductsPriceSum.add(productsPriceSum);
        }

        validateRequestMenuPriceGreaterThanAllProductsPriceSum(requestMenuPrice, allProductsPriceSum);
        return foundProducts;
    }

    private void validateRequestMenuPriceIsNonNull(BigDecimal requestMenuPrice) {
        if (Objects.isNull(requestMenuPrice)) {
            throw new InvalidRequestParamException("Menu의 price는 null일 수 없습니다.");
        }
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new InvalidRequestParamException("해당 id의 MenuGroup이 존재하지 않습니다."));
    }

    private Menu saveMenu(MenuRequest menuRequest, MenuGroup foundMenuGroup) {
        final Menu newMenu = convertRequestToEntity(menuRequest, foundMenuGroup);
        menuRepository.save(newMenu);

        return newMenu;
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProductRequest> menuProductRequests, List<Product> foundProducts, Menu newMenu) {
        final List<MenuProduct> newMenuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product foundProduct = getProductById(foundProducts, menuProductRequest.getProductId());
            final MenuProduct newMenuProduct = new MenuProduct(newMenu, foundProduct, menuProductRequest.getQuantity());
            menuProductRepository.save(newMenuProduct);
            newMenuProducts.add(newMenuProduct);
        }
        return newMenuProducts;
    }

    private MenuResponse createMenuResponse(Menu newMenu, List<MenuProduct> newMenuProducts) {
        final List<MenuProductResponse> menuProductResponses = createMenuProductResponses(newMenuProducts);
        return new MenuResponse(newMenu, menuProductResponses);
    }

    private Product getProductById(List<Product> foundProducts, Long id) {
        return foundProducts.stream()
            .filter(foundProductIt -> foundProductIt.hasId(id))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("앞서 DB에서 조회한 Product를 찾을 수 없습니다."));
    }

    private void validateRequestMenuPriceGreaterThanAllProductsPriceSum(BigDecimal requestMenuPrice, BigDecimal sum) {
        if (requestMenuPrice.compareTo(sum) > 0) {
            throw new InvalidRequestParamException("Menu의 price는 MenuProduct들의 price합보다 클 수 없습니다.");
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new InvalidRequestParamException("해당 id의 MenuProduct가 존재하지 않습니다."));
    }

    private List<MenuProductResponse> createMenuProductResponses(List<MenuProduct> newMenuProducts) {
        return newMenuProducts.stream()
            .map(MenuProductResponse::new)
            .collect(Collectors.toList())
            ;
    }

    private void validateQuantityNonNull(Long quantity) {
        if (Objects.isNull(quantity)) {
            throw new InvalidRequestParamException("요청 파라미터 menuProducts의 quantity값은 null일 수 없습니다.");
        }
    }

    private BigDecimal getProductsPriceSum(BigDecimal price, long quantity) {
        final BigDecimal requestQuantity = BigDecimal.valueOf(quantity);

        return price.multiply(requestQuantity);
    }

    private Menu convertRequestToEntity(MenuRequest menuRequest, MenuGroup menuGroup) {
        try {
            return new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestParamException(e.getMessage());
        }
    }

    public List<MenuResponse> findAll() {
        final List<Menu> foundAllMenus = menuRepository.findAll();
        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (Menu foundMenu : foundAllMenus) {
            final List<MenuProduct> foundMenuProducts = menuProductRepository.findAllByMenu(foundMenu);
            final List<MenuProductResponse> menuProductResponses = createMenuProductResponses(foundMenuProducts);
            menuResponses.add(new MenuResponse(foundMenu, menuProductResponses));
        }

        return menuResponses;
    }
}
