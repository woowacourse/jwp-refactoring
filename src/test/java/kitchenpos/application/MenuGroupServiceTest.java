package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.application.dto.MenuGroupCreateDto;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"classpath:truncate.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹을_등록할_수_있다() {
        // given
        final MenuGroupCreateDto menuGroupCreateDto = new MenuGroupCreateDto("테스트 메뉴 그룹");

        // when
        final MenuGroup result = menuGroupService.create(menuGroupCreateDto);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(menuGroupCreateDto.getName());
    }

    @Test
    void 메뉴그룹목록을_조회할_수_있다() {
        // given
        final MenuGroupCreateDto menuGroup = new MenuGroupCreateDto("테스트 메뉴 그룹");
        menuGroupService.create(menuGroup);

        // when
        final List<MenuGroup> results = menuGroupService.list();

        // then
        assertThat(results).hasSize(1);
        assertThat(results).extractingResultOf("getName")
            .containsExactly(menuGroup.getName());
    }
}
