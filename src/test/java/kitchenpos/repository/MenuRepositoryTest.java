package kitchenpos.repository;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("MenuRepository 테스트")
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;
    private MenuGroup menuGroup;

    @Autowired
    private ProductRepository productRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        menuGroup = MenuGroupFixture.create();
        menuGroup = menuGroupRepository.save(menuGroup);

        product = ProductFixture.create();
        product = productRepository.save(product);
    }

    @DisplayName("메뉴 추가 - 성공")
    @Test
    void create() {
        //given
        //when
        Menu menu = MenuFixture.create();
        Menu createdMenu = menuRepository.save(menu);
        //then
        assertThat(createdMenu.getId()).isNotNull();
    }

    @DisplayName("메뉴 추가 - 실패 - 존재하지 않는 MenuGroup인 경우")
    @Test
    void createFailureWhenInvalidMenuGroup() {
        MenuGroup menuGroup = new MenuGroup("INVALID_MENU_GROUP");
        Menu invalidMenu = MenuFixture.create(1L, "치킨", BigDecimal.valueOf(20_000), menuGroup);

        assertThatThrownBy(() -> menuRepository.save(invalidMenu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("모든 메뉴 반환")
    @Test
    void findAll() {
        //given
        Menu menu = MenuFixture.create();
        menuRepository.save(menu);
        //when
        List<Menu> menus = menuRepository.findAll();
        //then
        assertThat(menus).hasSize(1);
    }
}
