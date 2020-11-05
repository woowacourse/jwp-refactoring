package kitchenpos.dao;

import kitchenpos.domain.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.application.fixture.MenuGroupFixture.createMenuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateMenuDaoTest {
    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Long menuGroupId;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();
    }

    @Test
    @DisplayName("id가 없는 메뉴 엔티티를 저장하면 id가 부여되고, 엔티티의 필드인 메뉴 상품 리스트는 저장되지 않는다")
    void insert() {
        Menu menu = createMenu(
                null,
                "후라이드",
                BigDecimal.valueOf(1000, 2),
                menuGroupId,
                Collections.singletonList(createMenuProduct(null, null, 1, 1L))
        );

        Menu result = menuDao.save(menu);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(menu, "id", "menuProducts"),
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getMenuProducts()).isNull()
        );
    }

    @Test
    @DisplayName("존재하는 id로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        Menu menu = createMenu(null, "후라이드", BigDecimal.valueOf(1000, 2), menuGroupId, null);
        Menu persisted = menuDao.save(menu);

        Menu result = menuDao.findById(persisted.getId()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(menuDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        menuDao.save(createMenu(null, "후라이드", BigDecimal.valueOf(1000, 2), menuGroupId, null));
        menuDao.save(createMenu(null, "양념치킨", BigDecimal.valueOf(2000, 2), menuGroupId, null));
        menuDao.save(createMenu(null, "무슨치킨", BigDecimal.valueOf(3000, 2), menuGroupId, null));
        menuDao.save(createMenu(null, "땅땅치킨", BigDecimal.valueOf(4000, 2), menuGroupId, null));

        assertThat(menuDao.findAll()).hasSize(4);
    }

    @Test
    @DisplayName("id의 리스트 중 저장되어있는 데이터가 몇 개인지 확인한다")
    void countByIdIn() {
        List<Menu> shouldFound = Arrays.asList(
                menuDao.save(createMenu(null, "후라이드", BigDecimal.valueOf(1000, 2), menuGroupId, null)),
                menuDao.save(createMenu(null, "양념치킨", BigDecimal.valueOf(2000, 2), menuGroupId, null))
        );
        menuDao.save(createMenu(null, "무슨치킨", BigDecimal.valueOf(3000, 2), menuGroupId, null));
        menuDao.save(createMenu(null, "땅땅치킨", BigDecimal.valueOf(4000, 2), menuGroupId, null));

        List<Long> ids = shouldFound.stream().map(Menu::getId).collect(Collectors.toList());
        assertThat(menuDao.countByIdIn(ids)).isEqualTo(2);
    }
}