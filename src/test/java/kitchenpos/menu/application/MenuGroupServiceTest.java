package kitchenpos.menu.application;

import static kitchenpos.DomainFixture.세트_메뉴;
import static kitchenpos.DomainFixture.인기_메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.ui.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "classpath:truncate.sql")
class MenuGroupServiceTest {

    private MenuGroupRepository menuGroupRepository;
    private MenuGroupService menuGroupService;

    @Autowired
    public MenuGroupServiceTest(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    void 메뉴_그룹을_생성하고_결과를_반환한다() {
        // given
        final var request = new MenuGroupCreateRequest(인기_메뉴.getName());

        // when
        final var created = menuGroupService.create(request);

        // then
        assertAll(
                () -> assertThat(created.getId()).isNotNull(),
                () -> assertThat(created.getName()).isEqualTo(인기_메뉴.getName())
        );
    }

    @Test
    void 메뉴_그룹_목록을_조회한다() {
        // given
        menuGroupRepository.save(인기_메뉴);
        menuGroupRepository.save(세트_메뉴);

        // when
        final var found = menuGroupService.list();

        // then
        assertThat(found).hasSizeGreaterThanOrEqualTo(2);
    }
}
