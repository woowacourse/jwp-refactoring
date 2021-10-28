package kitchenpos.application;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup();
        menuGroup.setId(1L);
        menuGroup.setName("메뉴 그룹");
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        // given
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        // when, then
        assertDoesNotThrow(() -> menuGroupService.create(menuGroup));
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다.")
    void findAllMenuGroups() {
        // given
        given(menuGroupDao.findAll()).willReturn(Collections.singletonList(menuGroup));

        // when, then
        assertDoesNotThrow(() -> menuGroupService.list());
    }

}