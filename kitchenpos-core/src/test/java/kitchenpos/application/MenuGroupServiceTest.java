package kitchenpos.application;

import kitchenpos.application.fixture.MenuGroupServiceFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
class MenuGroupServiceTest extends MenuGroupServiceFixture {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹을_생성한다() {
        메뉴_그룹을_생성한다_픽스처_생성();

        final MenuGroup actual = menuGroupService.create(생성할_메뉴_그룹);

        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 메뉴_그룹을_조회한다() {
        메뉴_그룹을_조회한다_픽스처_생성();

        final List<MenuGroup> actual = menuGroupService.list();

        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.get(0).getName()).isEqualTo(저장한_메뉴_그룹_1.getName());
            softAssertions.assertThat(actual.get(1).getName()).isEqualTo(저장한_메뉴_그룹_2.getName());
        });
    }
}
