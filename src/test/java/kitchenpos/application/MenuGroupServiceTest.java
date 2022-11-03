package kitchenpos.application;

import static kitchenpos.common.constants.Constants.루나세트_이름;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.common.builder.MenuGroupBuilder;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.dto.response.MenuGroupResponse;
import kitchenpos.dto.response.MenuGroupsResponse;
import kitchenpos.dao.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    private final MenuGroupService menuGroupService;
    private final MenuGroupRepository menuGroupRepository;

    private MenuGroup 루나세트;

    @Autowired
    MenuGroupServiceTest(final MenuGroupService menuGroupService,
                         final MenuGroupRepository menuGroupRepository) {
        this.menuGroupService = menuGroupService;
        this.menuGroupRepository = menuGroupRepository;
    }

    @BeforeEach
    void setUp() {
        루나세트 = new MenuGroupBuilder()
                .name(루나세트_이름)
                .build();
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void 메뉴_그룹을_등록한다() {
        // given
        MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(루나세트_이름);

        // when
        MenuGroupResponse actual = menuGroupService.create(menuGroupCreateRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(루나세트_이름)
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void 메뉴_그룹을_조회한다() {
        // given
        menuGroupRepository.save(루나세트);

        // when
        MenuGroupsResponse 메뉴그룹들 = menuGroupService.list();

        // then
        assertThat(메뉴그룹들.getMenuGroupResponses()).hasSize(1);
    }
}
