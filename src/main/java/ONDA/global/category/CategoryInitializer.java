package ONDA.global.category;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryInitializer {

    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initCategories() {
        save("외국어 · 번역 · 통역");
        save("프로그래밍 · IT · 데이터");
        save("디자인 · 크리에이티브");
        save("영상 · 사진 · 콘텐츠 제작");
        save("음악 · 보컬 · 악기");
        save("미술 · 공예 · 수공예");
        save("뷰티 · 패션 · 스타일링");
        save("스포츠 · 피트니스 · 건강");
        save("라이프 · 자기계발");
        save("취업 · 진로 · 입시");
        save("비즈니스 · 창업 · 마케팅");
        save("경제 · 재테크 · 투자");
        save("글쓰기 · 출판 · 스피치");
        save("여행 · 문화 · 생활 취미");
        save("심리 · 상담 · 힐링");
    }

    private void save(String name) {
        categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }
}