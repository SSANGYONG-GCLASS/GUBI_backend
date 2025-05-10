package com.spring.gubi.service.reviews;

import com.spring.gubi.config.error.exception.OptionNotFoundException;
import com.spring.gubi.config.error.exception.ReviewNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.reviews.Review;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.reviews.UpdateReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewResponse;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.reviews.ReviewRepository;
import com.spring.gubi.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    @Value("${image-dir.review}")
    String uploadPath;

    // 리뷰 등록
    @Transactional
    public WriteReviewResponse save(WriteReviewRequest request, MultipartFile img) throws IOException {
        // 회원 정보와 상품 정보를 가져와야 함.

        // 회원 검증 로직 (추후 변경)
        User user = userRepository.findById(request.getUserNo())
                                .orElseThrow(UserNotFondException::new);    // 기본 에러 메시지
//                                .orElseThrow(() -> new UserNotFondException("회원 정보를 찾을 수 없습니다."));  // 커스텀 에러 메시지

        // 상품 검증 로직 (추후 변경)
        Option option = optionRepository.findById(request.getOptionNo())
                                .orElseThrow(OptionNotFoundException::new);
//                                .orElseThrow(() -> new OptionNotFoundException("옵션 정보를 찾을 수 없습니다."));
        // 첨부 이미지가 존재할 경우
        if (img != null && !img.isEmpty()) {
            // 이미지 서버에 저장
            String newFileName = uploadFile(img);
            request.setImg(newFileName);
        }

        Review review = reviewRepository.save(request.toEntity(user, option));

        return new WriteReviewResponse(review);
    }// end of public WriteReviewResponse save(WriteReviewRequest request, MultipartFile img) throws IOException -------------------


    // 리뷰 수정
    @Transactional
    public WriteReviewResponse update(UpdateReviewRequest request, MultipartFile img) throws IOException {

        // 기존 리뷰 정보 조회
        Review review = reviewRepository.findById(request.getId())
                            .orElseThrow(ReviewNotFoundException::new);

        // 작성자 일치 여부 확인 (추후)

        // 기존 이미지 삭제: 수정 시 이미지가 없고, 기존 이미지가 있었던 경우
        if ((img == null || img.isEmpty()) && review.getImg() != null) {
            deleteFile(review.getImg());
            review.updateImg(null);
        }


        // 정보 업데이트

        // 첨부 이미지가 존재할 경우
        if (img != null && !img.isEmpty()) {
            // 이미지 서버에 저장
            String newFileName = uploadFile(img);
            review.updateImg(newFileName);
        }

        review.updateReview(request);

        return new WriteReviewResponse(review);
    }// end of public WriteReviewResponse update(UpdateReviewRequest request, MultipartFile img) -------------------


    // 파일 업로드 메소드
    private String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();    // 원본 파일명

        // 확장자가 없는 경우 기본확장자 지정
        String ext = "";
        int index = originalFilename.lastIndexOf(".");
        if (index == -1) {
            ext = ".png";
        }
        else {
            ext = originalFilename.substring(index); // 확장자
        }

        String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;       // UUID + 확장자

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File saveFile = new File(uploadPath + File.separator + newFileName);
        file.transferTo(saveFile);       // 서버에 파일 저장

        return newFileName;
    }// end of private String uploadFile(MultipartFile file) throws IOException -------------------


    // 파일 삭제 메소드
    private void deleteFile(String fileName) {
        File file = new File(uploadPath + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }
    }// end of private void deleteFile(String fileName) --------------------
}
