package kitchenpos.application;

import static java.util.Collections.emptyList;
import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.MenuProductFixture.createMenuProductRequest;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuServiceTest extends AbstractServiceTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuProductDao menuProductDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setup() {
        menuGroup = menuGroupDao.save(createMenuGroup(null, "2+1메뉴"));
    }

    @DisplayName("메뉴를 생성할 수 있다.")
    @Test
    void create() {
        Product product1 = productDao.save(createProduct(null, "상품1", 100L));
        Product product2 = productDao.save(createProduct(null, "상품2", 500L));
        Product product3 = productDao.save(createProduct(null, "상품3", 1000L));
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(
            createMenuProductRequest(product1.getId(), 10),
            createMenuProductRequest(product2.getId(), 10),
            createMenuProductRequest(product3.getId(), 10)
        );

        MenuCreateRequest menuCreateRequest = createMenuRequest("메뉴1", 16000L, menuGroup.getId(),
            menuProductCreateRequests);

        MenuResponse savedMenu = menuService.create(menuCreateRequest);

        assertAll(
            () -> assertThat(savedMenu).isNotNull(),
            () -> assertThat(savedMenu.getId()).isNotNull(),
            () -> assertThat(savedMenu.getName()).isEqualTo(menuCreateRequest.getName()),
            () -> assertThat(savedMenu.getPrice().longValue())
                .isEqualTo(menuCreateRequest.getPrice().longValue()),
            () -> assertThat(savedMenu.getMenuGroupId())
                .isEqualTo(menuCreateRequest.getMenuGroupId())
        );
    }

    @DisplayName("메뉴 가격이 null 인 경우 메뉴를 생성할 수 없다.")
    @Test
    void create_throws_exception() {
        MenuCreateRequest menuCreateRequest = createMenuRequest("메뉴1", null, menuGroup.getId(),
            emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menuCreateRequest));
    }

    @DisplayName("메뉴 가격이 0원 미만인 경우 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -2L})
    void create_throws_exception(Long price) {
        MenuCreateRequest menuCreateRequest = createMenuRequest("메뉴1", price, menuGroup.getId(),
            emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menuCreateRequest));
    }

    @DisplayName("메뉴의 메뉴 그룹이 존재하지 않으면 메뉴를 생성할 수 없다.")
    @Test
    void create_throws_exception2() {
        MenuCreateRequest menuCreateRequest = createMenuRequest("메뉴1", 0L, null,
            emptyList());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menuCreateRequest));
    }

    @DisplayName("메뉴 상품 금액의 합이 메뉴의 가격보다 작다면 메뉴를 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(longs = {10001L, 10002L})
    void create_throws_exception3(Long price) {
        Product product = productDao.save(createProduct(null, "상품", 1000L));
        List<MenuProductCreateRequest> menuProductCreateRequests = Arrays.asList(
            createMenuProductRequest(product.getId(), 10)
        );

        MenuCreateRequest menuCreateRequest = createMenuRequest("메뉴1", price, menuGroup.getId(),
            menuProductCreateRequests);

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> menuService.create(menuCreateRequest));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        List<MenuResponse> savedMenus = Stream.of(
            menuDao.save(createMenu(null, "메뉴1", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴2", 0L, menuGroup.getId())),
            menuDao.save(createMenu(null, "메뉴3", 0L, menuGroup.getId()))
        )
            .map(it -> MenuResponse.of(it, emptyList()))
            .collect(Collectors.toList());

        List<MenuResponse> allMenus = menuService.list();

        System.out.println(Arrays.toString(allMenus.toArray()));

        assertThat(allMenus).usingFieldByFieldElementComparator().containsAll(savedMenus);
    }
}
