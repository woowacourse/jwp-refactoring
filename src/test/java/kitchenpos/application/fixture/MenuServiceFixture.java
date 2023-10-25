package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.infrastructure.persistence.JpaMenuGroupRepository;
import kitchenpos.infrastructure.persistence.JpaMenuProductRepository;
import kitchenpos.infrastructure.persistence.JpaMenuRepository;
import kitchenpos.infrastructure.persistence.JpaProductRepository;
import kitchenpos.ui.dto.CreateMenuRequest;
import kitchenpos.ui.dto.MenuProductDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class MenuServiceFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaProductRepository productRepository;

    @Autowired
    private JpaMenuGroupRepository menuGroupRepository;

    @Autowired
    private JpaMenuRepository menuRepository;

    @Autowired
    private JpaMenuProductRepository menuProductRepository;

    protected CreateMenuRequest 가격이_입력되지_않은_메뉴_생성_요청_dto;
    protected List<Menu> 저장된_메뉴_리스트;

    protected CreateMenuRequest 메뉴_생성_요청_dto;
    protected CreateMenuRequest 가격이_0보다_작은_메뉴_생성_요청_dto;
    protected CreateMenuRequest 유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴_생성_요청_dto;
    protected CreateMenuRequest 유효하지_않은_메뉴_상품_아이디를_갖는_메뉴_생성_요청_dto;
    protected CreateMenuRequest 유효하지_않은_가격을_갖는_메뉴_생성_요청_dto;

    public void 메뉴를_생성한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(첫_번째_상품.getId(), 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        메뉴_생성_요청_dto = new CreateMenuRequest(저장된_메뉴_그룹.getId(), 메뉴_상품_dto_2개, "메뉴 1", BigDecimal.valueOf(30_000));
    }

    protected void 전달_받은_메뉴의_가격이_입력되지_않았다면_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(첫_번째_상품.getId(), 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        가격이_입력되지_않은_메뉴_생성_요청_dto = new CreateMenuRequest(저장된_메뉴_그룹.getId(), 메뉴_상품_dto_2개, "메뉴 이름", null);
    }

    protected void 전달_받은_메뉴의_가격이_0보다_작은_경우_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(첫_번째_상품.getId(), 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        가격이_0보다_작은_메뉴_생성_요청_dto = new CreateMenuRequest(저장된_메뉴_그룹.getId(), 메뉴_상품_dto_2개, "메뉴 이름", BigDecimal.valueOf(-1L));
    }

    protected void 유효하지_않은_메뉴_그룹_아이디를_전달_받으면_예외가_발생한다_픽스처_생성() {
        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(첫_번째_상품.getId(), 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        유효하지_않은_메뉴_그룹_아이디를_갖는_메뉴_생성_요청_dto = new CreateMenuRequest(-999L, 메뉴_상품_dto_2개, "메뉴 이름", BigDecimal.valueOf(-1L));
    }

    protected void 유효하지_않은_메뉴_상품_아이디를_전달_받으면_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(-999L, 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        유효하지_않은_메뉴_상품_아이디를_갖는_메뉴_생성_요청_dto = new CreateMenuRequest(저장된_메뉴_그룹.getId(), 메뉴_상품_dto_2개, "메뉴 1", BigDecimal.valueOf(30_000));
    }

    protected void 메뉴의_가격이_메뉴에_포함된_상품_가격을_합친_것보다_작은_경우_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Product 첫_번째_상품 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 두_번째_상품 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(첫_번째_상품, 두_번째_상품));

        final MenuProductDto 메뉴_상품_dto_1 = new MenuProductDto(첫_번째_상품.getId(), 2);
        final MenuProductDto 메뉴_상품_dto_2 = new MenuProductDto(두_번째_상품.getId(), 2);
        final List<MenuProductDto> 메뉴_상품_dto_2개 = List.of(메뉴_상품_dto_1, 메뉴_상품_dto_2);

        final BigDecimal 상품_총합_가격_보다_적은_가격 = 첫_번째_상품.getPrice().subtract(두_번째_상품.getPrice());
        유효하지_않은_가격을_갖는_메뉴_생성_요청_dto = new CreateMenuRequest(저장된_메뉴_그룹.getId(), 메뉴_상품_dto_2개, "메뉴 1", 상품_총합_가격_보다_적은_가격);
    }

    protected void 모든_메뉴를_조회한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹 = new MenuGroup("한식");
        final MenuGroup 저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);

        final Menu 저장된_메뉴_1 = new Menu(저장된_메뉴_그룹, new Price(BigDecimal.valueOf(10_000)), "저장된 메뉴 1");
        final Menu 저장된_메뉴_2 = new Menu(저장된_메뉴_그룹, new Price(BigDecimal.valueOf(10_000)), "저장된 메뉴 2");
        menuRepository.saveAll(List.of(저장된_메뉴_1, 저장된_메뉴_2));

        final Product 상품_1 = new Product("첫_번째_상품", new Price(BigDecimal.valueOf(10_000)));
        final Product 상품_2 = new Product("두_번째_상품", new Price(BigDecimal.valueOf(20_000)));
        productRepository.saveAll(List.of(상품_1, 상품_2));

        final MenuProduct 저장된_메뉴_1의_메뉴_상품_1 = new MenuProduct(저장된_메뉴_1, 상품_1, 2);
        final MenuProduct 저장된_메뉴_1의_메뉴_상품_2 = new MenuProduct(저장된_메뉴_1, 상품_2, 1);
        final MenuProduct 저장된_메뉴_2의_메뉴_상품_1 = new MenuProduct(저장된_메뉴_2, 상품_1, 2);
        menuProductRepository.saveAll(List.of(저장된_메뉴_1의_메뉴_상품_1, 저장된_메뉴_1의_메뉴_상품_2, 저장된_메뉴_2의_메뉴_상품_1));

        저장된_메뉴_리스트 = List.of(저장된_메뉴_1, 저장된_메뉴_2);
    }
}
