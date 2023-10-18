package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuFactory;
import kitchenpos.domain.ProductFactory;
import kitchenpos.domain.menugroup.MenuGroup;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@SuppressWarnings("NonAsciiCharacters")
class MenuIntegrationTest extends IntegrationTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Nested
    class 메뉴_등록시 {

        @ParameterizedTest
        @NullAndEmptySource
        void 이름이_입력되지_않으면_예외가_발생한다(String name) {
            // given
            final var menu = MenuFactory.createMenuOf(name, BigDecimal.valueOf(1000), 1L);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 이름이_255자_보다_크다면_예외가_발생한다() {
            // given
            final var menu = MenuFactory.createMenuOf("메".repeat(256), BigDecimal.valueOf(1000), 1L);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 가격이_입력되지_않으면_예외가_발생한다() {
            // given
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", null, 1L);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "12345678901234567890"})
        void 가격이_0_또는_1에서_19자리_양의_정수가_아니라면_예외가_발생한다(BigDecimal price) {
            // given
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", price, 1L);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 메뉴_그룹_아이디가_입력되지_않으면_예외가_발생한다() {
            // given
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), null);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 메뉴_그룹_아이디가_존재하지_않으면_예외가_발생한다() {
            // given
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), 100L);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 메뉴_상품_목록에_있는_상품이_등록된_상품이_아니라면_예외가_발생한다() {
            // given
            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), 1L);
            final var menuProduct = MenuFactory.createMenuProductOf(100L, 1L);
            menu.getMenuProducts().add(menuProduct);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ParameterizedTest
        @ValueSource(ints = {-1, 0})
        void 수량이_0_이하라면_예외가_발생한다(int quantity) {
            // given
            final var menuGroup = new MenuGroup("메뉴그룹1");
            final var savedMenuGroup = menuGroupDao.save(menuGroup);

            final var product = ProductFactory.createProductOf("후라이드", BigDecimal.valueOf(1000));
            final var saveProduct = productDao.save(product);

            final var menuProduct = MenuFactory.createMenuProductOf(saveProduct.getId(), quantity);

            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), savedMenuGroup.getId());
            menu.getMenuProducts().add(menuProduct);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isBetween(HttpStatus.BAD_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        void 메뉴가_정상적으로_등록된다() {
            // given
            final var menuGroup = new MenuGroup("메뉴그룹1");
            final var savedMenuGroup = menuGroupDao.save(menuGroup);

            final var product = ProductFactory.createProductOf("후라이드", BigDecimal.valueOf(1000));
            final var saveProduct = productDao.save(product);

            final var menuProduct = MenuFactory.createMenuProductOf(saveProduct.getId(), 1L);

            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), savedMenuGroup.getId());
            menu.getMenuProducts().add(menuProduct);

            // when
            final var response = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }
    }

    @Nested
    class 메뉴_목록_조회시 {

        @Test
        void 메뉴_목록을_조회한다() {
            // given
            final var menuGroup = new MenuGroup("메뉴그룹1");
            final var savedMenuGroup = menuGroupDao.save(menuGroup);

            final var product = ProductFactory.createProductOf("후라이드", BigDecimal.valueOf(1000));
            final var saveProduct = productDao.save(product);

            final var menuProduct = MenuFactory.createMenuProductOf(saveProduct.getId(), 1L);

            final var menu = MenuFactory.createMenuOf("메뉴화 된 후라이드 치킨", BigDecimal.valueOf(1000), savedMenuGroup.getId());
            menu.getMenuProducts().add(menuProduct);
            final var savedMenu = restTemplate.postForEntity("http://localhost:" + port + "/api/menus", menu, Menu.class).getBody();

            // when
            final var response = restTemplate.getForEntity("http://localhost:" + port + "/api/menus", Menu[].class);

            // then
            assertAll(
                    () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                    () -> assertThat(response.getBody()).hasSize(1),
                    () -> assertThat(response.getBody()[0].getId()).isEqualTo(savedMenu.getId())
            );
        }
    }
}
