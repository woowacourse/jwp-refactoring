package kitchenpos.application;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.repository.OrderTableDao;
import kitchenpos.table.repository.TableGroupDao;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import org.assertj.core.api.ThrowableAssert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TableGroupServiceTest extends ServiceTest {

    private final TableGroupRequest tableGroup;

    public TableGroupServiceTest() {
        this.tableGroup = Fixtures.makeTableGroup();
    }

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("테이블 그룹 생성")
    void createTest() {

        // when
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        assertThat(tableGroupDao.findAll()).contains(savedTableGroup);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패 - 빈 테이블 항목")
    void createTestFailEmptyItem() {

        // given
        TableGroupRequest emptyTableGroup = new TableGroupRequest(new ArrayList<>());

        // when
        ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(emptyTableGroup);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable)
                                            .withMessage("테이블 비어있거나 2보다 작습니다.");
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패 - 테이블 수 불일치")
    void createTestFailEmpty() {

        // given
        TableGroupRequest emptyTableGroup = new TableGroupRequest(Arrays.asList(1L, 2L, 3L, 10L));


        // when
        ThrowableAssert.ThrowingCallable callable = () -> tableGroupService.create(emptyTableGroup);

        // then
        assertThatIllegalArgumentException().isThrownBy(callable)
                                            .withMessage("저장된 테이블 수와 요청 테이블 수가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("테이블 등록 해제")
    void ungroupTest() {

        // given
        final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertThat(orderTableDao.findAllByTableGroupId(savedTableGroup.getId())).isEmpty();
    }
}
