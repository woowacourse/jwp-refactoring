package com.kitchenpos.ui.tablegroup;

import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.TableGroup;
import com.kitchenpos.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static com.kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupRestControllerAcceptanceTest extends TableGroupRestControllerAcceptanceTestFixture {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private TableGroup 테이블_그룹;
    private OrderTable 주문_테이블_A;
    private OrderTable 주문_테이블_B;

    @BeforeEach
    void setup() {
        테이블_그룹 = tableGroupRepository.save(TableGroup.createDefault());
        OrderTable 주문_테이블_A_데이터 = 주문_테이블_생성(10, true);
        주문_테이블_A_데이터.updateTableGroupStatus(테이블_그룹.getId());

        OrderTable 주문_테이블_B_데이터 = 주문_테이블_생성(20, true);
        주문_테이블_B_데이터.updateTableGroupStatus(테이블_그룹.getId());

        주문_테이블_A = 주문_테이블을_생성한다(주문_테이블_A_데이터);
        주문_테이블_B = 주문_테이블을_생성한다(주문_테이블_B_데이터);
    }

    @Test
    void 단체를_생성한다() {
        // when
        var 단체_지정_결과 = 단체를_지정한다("/api/table-groups", 단체_지정_생성_요청(List.of(주문_테이블_A.getId(), 주문_테이블_B.getId())));

        // then
        단체가_성공적으로_지정된다(단체_지정_결과, 테이블_그룹);
    }

    @Test
    void 단체를_제거한다() {
        // given
        var 단체 = 단체_데이터_생성(List.of(주문_테이블_A.getId(), 주문_테이블_B.getId()));

        // when
        var 제거_결과 = 단체를_제거한다("/api/table-groups/" + 단체.getId());

        // then
        단체가_성공적으로_제거된다(제거_결과);
    }
}
