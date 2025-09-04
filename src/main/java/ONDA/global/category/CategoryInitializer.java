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
        save("창의 • 예술");
        save("라이프스타일");
        save("스포츠 • 웰빙");
        save("언어 • 학습");
        save("IT • 디지털");
        save("자기계발");
    }

    private void save(String name) {
        categoryRepository.save(Category.builder()
                .name(name)
                .build());
    }
}