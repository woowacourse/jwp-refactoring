package kitchenpos.application;

import kitchenpos.dao.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableServiceTest {

    private TableService tableService;
    @Autowired
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository);
    }

    @Test
    void 주문_테이블_생성할_수_있다() {

    }

    @Test
    void 주문_테이블_전체_조회할_수_있다() {

    }

    @Test
    void 주문_테이블_Id가_존재하지_않는경우_예외가_발생한다() {
    }

    @Test
    void 주문_테이블_그룹_Id가_존재하는_경우_예외가_발생한다() {

    }

    @Test
    void 주문_테이블_아이디_주문상태가_존재하는_경우_예외가_발생한다() {
    }

    @Test
    void 주문_테이블_상태를_빈_테이블_변경할_수_있다() {
    }

    @Test
    void 게스트_수는_0명_이하일_수_없다() {
    }

    @Test
    void 주문_테이블_Id가_존재하지_않는_경우_예외가_발생한다() {
    }

    @Test
    void 주문_테이블_방문한_손님_수_변경을_할_수_있다() {
    }
}
