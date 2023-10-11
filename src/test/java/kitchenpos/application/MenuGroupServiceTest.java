package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private MenuGroup 추천메뉴;
    private MenuGroup 인기메뉴;

    @BeforeEach
    void setUp() {
        추천메뉴 = new MenuGroup();
        추천메뉴.setName("추천메뉴");
        인기메뉴 = new MenuGroup();
        인기메뉴.setName("인기메뉴");
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        // when
        MenuGroup savedMenuGroup = menuGroupService.create(추천메뉴);
        Optional<MenuGroup> result = menuGroupDao.findById(savedMenuGroup.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get().getId()).isPositive(),
                () -> assertThat(result.get().getName()).isEqualTo("추천메뉴")
        );
    }

    @Test
    void 메뉴_그룹들을_조회한다() {
        // given
        menuGroupDao.save(추천메뉴);
        menuGroupDao.save(인기메뉴);

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getId()).isPositive(),
                () -> assertThat(result.get(0).getName()).isEqualTo("추천메뉴"),
                () -> assertThat(result.get(1).getId()).isPositive(),
                () -> assertThat(result.get(1).getName()).isEqualTo("인기메뉴")
        );
    }
}
