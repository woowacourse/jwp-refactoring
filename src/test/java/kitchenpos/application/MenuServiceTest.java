package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static kitchenpos.fixture.MenuFixture.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.support.SpringBootNestedTest;

@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    MenuGroup 두마리메뉴;
    Product 후라이드;
    Product 양념치킨;

    @BeforeEach
    void setUp() {
        두마리메뉴 = menuGroupDao.save(MenuGroupFixture.두마리메뉴.toMenuGroup());

        후라이드 = productDao.save(ProductFixture.후라이드.toProduct());
        양념치킨 = productDao.save(ProductFixture.양념치킨.toProduct());
    }

    @DisplayName("메뉴를 생성한다")
    @SpringBootNestedTest
    class CreateTest {

        @DisplayName("메뉴를 생성하면 ID를 할당된 Menu객체가 반환된다")
        @Test
        void create() {
            MenuRequest request = 후라이드_양념치킨_두마리세트.toRequest(두마리메뉴.getId(), 후라이드.getId(), 양념치킨.getId());

            MenuResponse actual = menuService.create(request);
            assertThat(actual.getId()).isNotNull();
        }

        @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNotExistMenuGroup() {
            Long notExistMenuGroupId = 0L;
            MenuRequest request = 후라이드_양념치킨_두마리세트.toRequest(notExistMenuGroupId, 후라이드.getId(), 양념치킨.getId());

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("메뉴 그룹이 존재하지 않습니다.");
        }

        @DisplayName("상품이 존재하지 않을 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNotExistProduct() {
            Long notExistProductId = 0L;
            MenuRequest request = 후라이드_양념치킨_두마리세트.toRequest(두마리메뉴.getId(), notExistProductId, 양념치킨.getId());

            assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 상품입니다.");
        }
    }

    @DisplayName("존재하는 모든 메뉴 목록을 조회한다")
    @Test
    void list() {
        MenuRequest request = 후라이드_양념치킨_두마리세트.toRequest(두마리메뉴.getId(), 후라이드.getId(), 양념치킨.getId());
        menuService.create(request);

        List<MenuResponse> list = menuService.list();

        assertThat(list).hasSize(1);
    }
}
