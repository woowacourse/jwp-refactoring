package kitchenpos.application;

import static kitchenpos.support.TestFixtureFactory.새로운_단체_지정;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_상품;
import static kitchenpos.support.TestFixtureFactory.새로운_상품;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.dto.request.OrderTableCreateRequest;
import kitchenpos.table.dto.request.OrderTableUpdateEmptyRequest;
import kitchenpos.table.dto.request.OrderTableUpdateNumberOfGuestsRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private TableService tableService;

    @Test
    void 테이블을_등록한다() {
        OrderTableCreateRequest 주문_테이블_생성_요청 = new OrderTableCreateRequest(1, false);

        OrderTableResponse 등록된_주문_테이블 = tableService.create(주문_테이블_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문_테이블.getId()).isNotNull();
            softly.assertThat(등록된_주문_테이블.getNumberOfGuests()).isEqualTo(1);
            softly.assertThat(등록된_주문_테이블.isEmpty()).isFalse();
        });
    }

    @Test
    void 테이블_목록을_조회한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
        OrderTable 빈_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        List<OrderTableResponse> 테이블_목록_조회_응답 = tableService.readAll();

        assertThat(테이블_목록_조회_응답).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(OrderTableResponse.from(주문_테이블), OrderTableResponse.from(빈_테이블));
    }

    @Test
    void 테이블_상태를_변경한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(false);
        OrderTableResponse 주문_테이블_상태_변경_응답 = tableService.updateIsEmpty(주문_테이블.getId(), 주문_테이블_상태_변경_요청);

        assertThat(주문_테이블_상태_변경_응답.isEmpty()).isFalse();
    }

    @Test
    void 존재하지_않는_테이블의_상태를_변경할_수_없다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(false);

        assertThatThrownBy(() -> tableService.updateIsEmpty(Long.MIN_VALUE, 주문_테이블_상태_변경_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("해당하는 주문 테이블이 없습니다.");
    }

    @Test
    void 단체_지정된_주문_테이블은_빈_테이블로_설정할_수_없다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        TableGroup 단체_지정 = 새로운_단체_지정(List.of(주문_테이블1, 주문_테이블2));
        주문_테이블1.assignTableGroup(단체_지정);
        주문_테이블2.assignTableGroup(단체_지정);
        tableGroupRepository.save(단체_지정);

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(true);

        assertSoftly(softly -> {
            softly.assertThatThrownBy(() -> tableService.updateIsEmpty(주문_테이블1.getId(), 주문_테이블_상태_변경_요청))
                    .isInstanceOf(OrderTableException.class)
                    .hasMessage("테이블에 테이블 그룹이 존재합니다.");
            softly.assertThatThrownBy(() -> tableService.updateIsEmpty(주문_테이블2.getId(), 주문_테이블_상태_변경_요청))
                    .isInstanceOf(OrderTableException.class)
                    .hasMessage("테이블에 테이블 그룹이 존재합니다.");
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 주문_상태가_조리_또는_식사인_주문_테이블은_빈_테이블로_설정할_수_없다(OrderStatus orderStatus) {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));

        주문_테이블.changeEmpty(false);
        주문_테이블.changeNumberOfGuests(3);

        MenuGroup 메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹(null, "메뉴 그룹"));
        Menu 메뉴 = menuRepository.save(새로운_메뉴("메뉴", BigDecimal.valueOf(30000L), 메뉴_그룹));
        Product 상품1 = productRepository.save(새로운_상품(null, "상품1", BigDecimal.valueOf(20000L)));
        Product 상품2 = productRepository.save(새로운_상품(null, "상품2", BigDecimal.valueOf(20000L)));

        MenuProduct 메뉴_상품1 = menuProductRepository.save(새로운_메뉴_상품(메뉴, 상품1, 1));
        MenuProduct 메뉴_상품2 = menuProductRepository.save(새로운_메뉴_상품(메뉴, 상품2, 1));

        메뉴.addMenuProducts(List.of(메뉴_상품1, 메뉴_상품2));

        orderRepository.save(새로운_주문(
                주문_테이블,
                orderStatus,
                LocalDateTime.now(),
                List.of(새로운_주문_항목(메뉴, 1))
        ));

        OrderTableUpdateEmptyRequest 주문_테이블_상태_변경_요청 = new OrderTableUpdateEmptyRequest(true);

        assertThatThrownBy(() -> tableService.updateIsEmpty(주문_테이블.getId(), 주문_테이블_상태_변경_요청))
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 상태가 주문 완료가 아닙니다.");
    }

    @Test
    void 방문한_손님_수를_입력한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));

        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(3);

        OrderTableResponse 방문한_손님_수_입력_응답 = tableService.updateNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_입력_요청);

        assertThat(방문한_손님_수_입력_응답.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    void 방문한_손님_수는_0명_이상이어야_한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));

        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.updateNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_입력_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("방문한 손님 수가 유효하지 않습니다.");
    }

    @Test
    void 존재하지_않는_테이블에_방문한_손님_수를_입력할_수_없다() {
        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(-1);

        assertThatThrownBy(() -> tableService.updateNumberOfGuests(Long.MIN_VALUE, 방문한_손님_수_입력_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("해당하는 주문 테이블이 없습니다.");
    }

    @Test
    void 빈_테이블에_방문한_손님_수를_입력할_수_없다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        OrderTableUpdateNumberOfGuestsRequest 방문한_손님_수_입력_요청 = new OrderTableUpdateNumberOfGuestsRequest(3);

        assertThatThrownBy(() -> tableService.updateNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_입력_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("테이블이 비어있습니다.");
    }
}
