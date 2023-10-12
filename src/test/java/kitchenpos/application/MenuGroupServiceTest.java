package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

import javax.sql.DataSource;

import static kitchenpos.fixture.MenuGroupFixture.일식메뉴;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class MenuGroupServiceTest {

    @Autowired
    private DataSource dataSource;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        this.menuGroupService = new MenuGroupService(new JdbcTemplateMenuGroupDao(dataSource));
    }

    @Test
    void 메뉴_그룹을_등록한다() {
        var menuGroup = 일식메뉴();

        assertThat(menuGroupService.create(menuGroup))
                .usingRecursiveComparison()
                .isEqualTo(일식메뉴());
    }

    @Test
    void 모든_메뉴그룹들을_가져온다() {
        assertThat(menuGroupService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(MenuGroupFixture.listAllInDatabase());
    }
}
