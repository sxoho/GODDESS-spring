package dev.jpa.team1.product.productcolorimage;

import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productcolor.ProductColorRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductColorImageService {

    private final ProductColorImageRepository repository;
    private final ProductColorRepository productColorRepository;

    private static final String UPLOAD_DIR = "uploads/product/";

    // 전체 조회
    @Transactional(readOnly = true)
    public List<ProductColorImageResponseDto> findAll() {

        return repository.findAll()
                .stream()
                .map(ProductColorImageResponseDto::from)
                .toList();
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public ProductColorImageResponseDto findById(Long id) {

        ProductColorImage entity = repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "ProductColorImage not found. id=" + id
                        )
                );

        return ProductColorImageResponseDto.from(entity);
    }

    // 이미지 저장
    @Transactional
    public ProductColorImageResponseDto save(
            Long productColorId,
            MultipartFile file,
            String fileName,
            String isMain,
            String imageType,
            Integer sortOrder
    ) throws IOException {

        // 1. 상품 색상 조회
        ProductColor productColor =
                productColorRepository.findById(productColorId)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "ProductColor not found. id="
                                                + productColorId
                                )
                        );

        // 2. 파일 검사
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "이미지 파일을 선택해주세요."
            );
        }

        // 3. 저장 파일명 검사
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException(
                    "저장할 이미지 이름을 입력해주세요."
            );
        }

        // 4. 원본 파일명
        String fileOrigin = file.getOriginalFilename();

        if (fileOrigin == null || fileOrigin.isBlank()) {
            throw new IllegalArgumentException(
                    "파일명이 올바르지 않습니다."
            );
        }

        // 5. 확장자 추출
        String extension = "";

        int dotIndex = fileOrigin.lastIndexOf(".");

        if (dotIndex >= 0) {
            extension = fileOrigin
                    .substring(dotIndex)
                    .toLowerCase();
        }

        // 6. 사용자가 입력한 파일명 정리
        String safeFileName = fileName
                .trim()
                .replaceAll("[^a-zA-Z0-9가-힣_-]", "_");

        if (safeFileName.isBlank()) {
            throw new IllegalArgumentException(
                    "사용할 수 없는 이미지 이름입니다."
            );
        }

        // 서버 저장용 고유 파일명 생성
        String uniqueId =
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8);

        String fileSaved =
                safeFileName
                        + "_"
                        + uniqueId
                        + extension;

        // 7. 저장 폴더 절대경로
        Path uploadPath =
                Paths.get(UPLOAD_DIR)
                        .toAbsolutePath()
                        .normalize();

        // 폴더 없으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 8. 최종 저장 경로
        Path savePath =
                uploadPath.resolve(fileSaved);

        // 9. 실제 파일 저장
        Files.copy(
                file.getInputStream(),
                savePath
        );

        System.out.println(
                "상품 이미지 저장 완료 = " + savePath
        );

        // 10. 브라우저 접근 경로
        String filePath =
                "/images/product/" + fileSaved;

        // 11. DB 저장
        ProductColorImage entity =
                ProductColorImage.builder()
                        .productColor(productColor)
                        .fileOrigin(fileOrigin)
                        .fileSaved(fileSaved)
                        .filePath(filePath)
                        .isMain(isMain)
                        .imageType(imageType)
                        .sortOrder(sortOrder)
                        .build();

        ProductColorImage saved =
                repository.save(entity);

        return ProductColorImageResponseDto.from(saved);
    }

    // 이미지 메타정보 수정
    @Transactional
    public ProductColorImageResponseDto update(
            Long id,
            String isMain,
            String imageType,
            Integer sortOrder
    ) {
        ProductColorImage entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColorImage not found. id=" + id
                ));

        entity.updateMetadata(
                isMain == null ? entity.getIsMain() : isMain,
                imageType == null ? entity.getImageType() : imageType,
                sortOrder == null ? entity.getSortOrder() : sortOrder
        );

        return ProductColorImageResponseDto.from(repository.saveAndFlush(entity));
    }

    // 이미지 삭제: 실제 파일과 DB 데이터를 함께 삭제
    @Transactional
    public void delete(Long id) throws IOException {
        ProductColorImage entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "ProductColorImage not found. id=" + id
                ));

        Path uploadPath = Paths.get(UPLOAD_DIR)
                .toAbsolutePath()
                .normalize();

        Path filePath = uploadPath.resolve(entity.getFileSaved()).normalize();

        if (filePath.startsWith(uploadPath)) {
            Files.deleteIfExists(filePath);
        }

        repository.delete(entity);
    }
}
