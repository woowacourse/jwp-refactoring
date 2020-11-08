package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup savedMenuGroup;
    private Product savedProduct1;
    private Product savedProduct2;
    private Product savedProduct3;
    private MenuProductCreateRequest menuProductCreateRequest1;
    private MenuProductCreateRequest menuProductCreateRequest2;
    private MenuProductCreateRequest menuProductCreateRequest3;

    @BeforeEach
    void setUp() {
        this.savedMenuGroup = createSavedMenuGroup("두마리메뉴");
        this.savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        this.savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
        this.savedProduct3 = createSavedProduct("후라이드치킨", BigDecimal.valueOf(15_000));
        this.menuProductCreateRequest1 = new MenuProductCreateRequest(this.savedProduct1.getId(), 1);
        this.menuProductCreateRequest2 = new MenuProductCreateRequest(this.savedProduct2.getId(), 1);
        this.menuProductCreateRequest3 = new MenuProductCreateRequest(this.savedProduct3.getId(), 1);
    }

    @DisplayName("새로운 메뉴 생성")
    @Test
    void createMenuTest() {
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(this.menuProductCreateRequest1,
                                                                                 this.menuProductCreateRequest2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    this.savedMenuGroup.getId(),
                                                                    menuProductCreateRequests);

        MenuResponse menuResponse = this.menuService.create(menuCreateRequest);

        assertAll(
                () -> assertThat(menuResponse).isNotNull(),
                () -> assertThat(menuResponse.getName()).isEqualTo(menuCreateRequest.getName()),
                () -> assertThat(menuResponse.getPrice()).isEqualTo(menuCreateRequest.getPrice()),
                () -> assertThat(menuResponse.getMenuGroupId()).isEqualTo(menuCreateRequest.getMenuGroupId()),
                () -> assertThat(menuResponse.getMenuProducts()).hasSize(menuCreateRequest.getMenuProductCreateRequests().size())
        );
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 존재하지 않으면 예외 발생")
    @Test
    void createMenuWithNullPriceThenThrowException() {
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(this.menuProductCreateRequest1,
                                                                                 this.menuProductCreateRequest2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", null, this.savedMenuGroup.getId(),
                                                                    menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 가격이 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -9999})
    void createMenuWithInvalidPriceThenThrowException(int invalidPrice) {
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(this.menuProductCreateRequest1,
                                                                                 this.menuProductCreateRequest2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(invalidPrice),
                                                                    this.savedMenuGroup.getId(),
                                                                    menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 메뉴 그룹을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuGroupThenThrowException() {
        long notExistMenuGroupId = -1L;
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(this.menuProductCreateRequest1,
                                                                                 this.menuProductCreateRequest2);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    notExistMenuGroupId, menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 존재하지 않는 상품을 지정하면 예외 발생")
    @Test
    void createMenuWithNotExistMenuProductThenThrowException() {
        long notExistProductId = -1L;
        MenuProductCreateRequest menuProductCreateRequest = new MenuProductCreateRequest(notExistProductId, 1);
        List<MenuProductCreateRequest> menuProductCreateRequests = Collections.singletonList(menuProductCreateRequest);

        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                    this.savedMenuGroup.getId(), menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 메뉴를 생성할 때 메뉴의 가격이 지정한 상품 가격의 총합을 초과하면 예외 발생")
    @Test
    void createMenuWithInvalidPriceThenThrowException() {
        Product savedProduct1 = createSavedProduct("양념치킨", BigDecimal.valueOf(16_000));
        Product savedProduct2 = createSavedProduct("간장치킨", BigDecimal.valueOf(16_000));
        MenuProductCreateRequest menuProductCreateRequest1 = new MenuProductCreateRequest(savedProduct1.getId(), 1);
        MenuProductCreateRequest menuProductCreateRequest2 = new MenuProductCreateRequest(savedProduct2.getId(), 1);
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(menuProductCreateRequest1,
                                                                                 menuProductCreateRequest2);

        BigDecimal invalidPrice = BigDecimal.valueOf(33_000);
        MenuCreateRequest menuCreateRequest = new MenuCreateRequest("양념간장두마리메뉴", invalidPrice,
                                                                    this.savedMenuGroup.getId(),
                                                                    menuProductCreateRequests);

        assertThatThrownBy(() -> this.menuService.create(menuCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 모든 메뉴를 조회")
    @Test
    void listMenuTest() {
        List<MenuProductCreateRequest> menuProductCreateRequests1 = Arrays.asList(this.menuProductCreateRequest1,
                                                                                  this.menuProductCreateRequest2);
        MenuCreateRequest menuCreateRequest1 = new MenuCreateRequest("양념간장두마리메뉴", BigDecimal.valueOf(28_000),
                                                                     this.savedMenuGroup.getId(),
                                                                     menuProductCreateRequests1);
        List<MenuProductCreateRequest> menuProductCreateRequests2 = Arrays.asList(this.menuProductCreateRequest1,
                                                                                  this.menuProductCreateRequest3);
        MenuCreateRequest menuCreateRequest2 = new MenuCreateRequest("후라이드양념두마리메뉴", BigDecimal.valueOf(27_000),
                                                                     this.savedMenuGroup.getId(),
                                                                     menuProductCreateRequests2);

        List<MenuCreateRequest> menuCreateRequests = Arrays.asList(menuCreateRequest1, menuCreateRequest2);
        menuCreateRequests.forEach(menuCreateRequest -> this.menuService.create(menuCreateRequest));

        List<MenuResponse> menuResponses = this.menuService.list();

        assertThat(menuResponses).hasSize(menuCreateRequests.size());
    }

    private MenuGroup createSavedMenuGroup(String menuName) {
        MenuGroup menuGroup = new MenuGroup(menuName);
        return this.menuGroupRepository.save(menuGroup);
    }

    private Product createSavedProduct(String name, BigDecimal price) {
        Product product = new Product(name, price);
        return this.productRepository.save(product);
    }
}