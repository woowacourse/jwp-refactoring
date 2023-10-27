package kitchenpos.menugroup;

import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static kitchenpos.fixture.MenuGroupFixture.일식메뉴;
import static kitchenpos.fixture.MenuGroupFixture.일식메뉴_REQUEST;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_등록한다() {
        var menuGroup = 일식메뉴_REQUEST();

        assertThat(menuGroupService.create(menuGroup))
                .usingRecursiveComparison()
                .isEqualTo(일식메뉴());
    }

    @Test
    @Transactional
    void 모든_메뉴그룹들을_가져온다() {
        assertThat(menuGroupService.list())
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(MenuGroupFixture.listAllInDatabase());
    }
}
