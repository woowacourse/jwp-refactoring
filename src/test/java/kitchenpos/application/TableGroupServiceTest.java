package kitchenpos.application;

import kitchenpos.application.exception.NotAllowedUngroupException;
import kitchenpos.application.exception.NotFoundOrderTableException;
import kitchenpos.application.exception.NotFoundTableGroupException;
import kitchenpos.common.ServiceTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exception.InvalidOrderTableToTableGroup;
import kitchenpos.domain.exception.InvalidOrderTablesSize;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.repository.TableGroupRepository;
import kitchenpos.ui.dto.tablegroup.TableGroupRequest;
import kitchenpos.ui.dto.tablegroup.TableGroupResponse;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("테이블 그룹 서비스 테스트")
class TableGroupServiceTest extends ServiceTestConfig {

    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuRepository menuRepository;

    @Nested
    class 단체_지정_등록 {

        @Test
        void 단체_지정을_등록한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(orderTables);

            // when
            final TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

            // then
            final List<OrderTable> actualOrderTables = orderTableRepository.findAll();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actual.getId()).isPositive();
                softAssertions.assertThat(actualOrderTables.get(0).getTableGroup().getId()).isEqualTo(actual.getId());
                softAssertions.assertThat(actualOrderTables.get(1).getTableGroup().getId()).isEqualTo(actual.getId());
            });
        }

        @Test
        void 단체_지정_등록시_주문_테이블이_한개라면_예외를_반환한다() {
            // given
            final OrderTable orderTable = orderTableRepository.save(OrderTableFixture.빈_테이블_엔티티_생성());
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(List.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(InvalidOrderTablesSize.class)
                    .hasMessage("주문 테이블은 3개 이상 있어야 합니다.");
        }

        @Test
        void 단체_지정_등록시_주문_테이블이_비어있다면_예외를_반환한다() {
            // given
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(List.of());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(InvalidOrderTablesSize.class)
                    .hasMessage("주문 테이블은 3개 이상 있어야 합니다.");
        }

        @Test
        void 단체_지정_등록시_전달된_주문_테이블_개수와_실제_저장되어_있는_주문_테이블_개수가_다르다면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            orderTables.add(OrderTableFixture.빈_테이블_엔티티_생성());
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(NotFoundOrderTableException.class)
                    .hasMessage("존재하지 않는 주문 테이블이 있습니다.");
        }

        @Test
        void 단체_지정_등록시_주문_테이블중_비어있지_않은_것이_존재한다면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final OrderTable notEmptyOrderTable = orderTableRepository.save(OrderTableFixture.주문_테이블_엔티티_생성());
            orderTables.add(notEmptyOrderTable);
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(InvalidOrderTableToTableGroup.class)
                    .hasMessage("주문 테이블이 테이블 그룹을 만들 수 없는 상태입니다.");
        }

        @Test
        void 단체_지정_등록시_단체_지정이_null이_아닌_것이_존재한다면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            tableGroupRepository.save(TableGroupFixture.단체_지정_엔티티_생성(orderTables));
            orderTableRepository.saveAll(orderTables);
            orderTables.add(OrderTableFixture.빈_테이블_엔티티_생성());
            final TableGroupRequest tableGroupRequest = TableGroupFixture.단체_지정_요청_dto_생성(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(NotFoundOrderTableException.class)
                    .hasMessage("존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    @Nested
    class 그룹_해제 {

        @Test
        void 그룹을_해제한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final TableGroup tableGroup = tableGroupRepository.save(TableGroupFixture.단체_지정_엔티티_생성(orderTables));

            // when
            tableGroupService.ungroup(tableGroup.getId());

            // then
            final List<OrderTable> actualOrderTables = orderTableRepository.findAll();

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(actualOrderTables.get(0).getTableGroup()).isNull();
                softAssertions.assertThat(actualOrderTables.get(0).isEmpty()).isFalse();
                softAssertions.assertThat(actualOrderTables.get(1).getTableGroup()).isNull();
                softAssertions.assertThat(actualOrderTables.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        void 그룹_해제시_존재하지_않는_특정_주문_테이블이라면_예외를_반환한다() {
            // given
            final Long unsavedTableGroup = 999L;

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(unsavedTableGroup))
                    .isInstanceOf(NotFoundTableGroupException.class)
                    .hasMessage("해당 단체 지정이 존재하지 않습니다.");
        }

        @Test
        void 그룹_해제시_특정_주문_테이블_아이디들_중_조리_혹은_식사_상태인_것이_존재한다면_예외를_반환한다() {
            // given
            final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.빈_테이블_엔티티들_생성(2));
            final MenuGroup menuGroup = menuGroupRepository.save(MenuGroupFixture.메뉴_그룹_엔티티_생성());
            final List<Product> products = productRepository.saveAll(ProductFixture.상품_엔티티들_생성(2));
            final Menu menu = menuRepository.save(MenuFixture.메뉴_엔티티_생성(menuGroup, products));
            orderRepository.save(OrderFixture.조리_상태의_주문_엔티티_생성(orderTables.get(0), menu));
            final TableGroup tableGroup = tableGroupRepository.save(TableGroupFixture.단체_지정_엔티티_생성(orderTables));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(NotAllowedUngroupException.class)
                    .hasMessage("단체 지정을 해제할 수 없는 주문이 존재합니다.");
        }
    }
}
