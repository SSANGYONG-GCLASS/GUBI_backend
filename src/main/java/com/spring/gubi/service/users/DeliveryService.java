package com.spring.gubi.service.users;

import com.spring.gubi.config.error.exception.DeliveryNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.domain.users.DeliveryDefault;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.users.*;
import com.spring.gubi.repository.users.DeliveryRepository;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.util.Pagination;
import com.spring.gubi.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    // 임시방편! 원래는 userServiece 에서 레포지토리를 가져오는 것이 맞다!
    // private  final userService userservice; // 이게 맞음
    private final UserRepository userRepository;
    
    public DeliveryService(DeliveryRepository deliveryRepository, UserRepository userRepository) {
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
    }
    
    
    // 배송지 추가
    @Transactional
    public AddDeliveryResponse addDelivery(AddDeliveryRequest request) throws IOException {
        
        // 먼저 받은 유저 정보가 옳바른지 검사
        User user = userRepository.findById(request.getUserNo())
                        .orElseThrow(UserNotFondException::new);
        
        // 딜리버리 엔티티로 변환!
        Delivery delivery = request.toEntity(user);
        
        // 만약 옳바르면 저장, 엔티티 타입으로 다시 나온다.
        Delivery response = deliveryRepository.save(delivery);
        
        // 다시 리스폰스 타입으로 변환 후 반환
        return new AddDeliveryResponse(response);
        
    }//end of public AddDeliveryResponse addDelivery(AddDeliveryRequest request) throws IOException {}...
    
    
    
    // 배송지 수정
    @Transactional
    public void updateDelivery(UpdateDeliveryRequest request) throws IOException {
        
        // 먼저 받은 유저 정보가 옳바른지 검사
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(UserNotFondException::new);
        
        // // 배송지가 디폴트인 경우 모든 배송지는 넌이 돼어야한다. 디폴트로 수정하는 경우 모두 바꿔주자!!! 체크해야함
        // // 사용자의 모든 배송지를 리스트로 조회
        // List<Delivery> deliveryList = deliveryRepository.findByUser_Id(user.getId());
        //
        // // !!!!!! 모두 조회하고 넌으로 바꾼 다음, for 문으로 해당되는 배송지 번호의 녀석만 디폴트로 바꿔주자
        // for (Delivery delivery : deliveryList) {
        //     if ("DEFAULT".equals(request.getIsDefault())) { // 받은 수정 정보가 디폴트라면
        //         if (delivery.getId() == request.getDeliveryNo()) {// 만약 검색된 배송번호가 바꾸려는 번호와 같다면 수정
        //             delivery.updateDelivery(request);
        //         } else {
        //             // 아니라면 모두 NONE으로 바꾸자
        //             delivery.updateDefault("NONE");
        //         } //end of if else (delivery.getId() == request.getDeliveryNo()) {}...
        //
        //     } else if("NONE".equals(request.getIsDefault())) { // 만약 NONE 이라면 그냥 그 배송지번호의 정보만 수정함
        //         if (delivery.getId() == request.getDeliveryNo()) {
        //             delivery.updateDelivery(request);
        //             break;
        //         }//end of if (delivery.getId() == request.getDeliveryNo()) {}...
        //     }//end of if else ("DEFAULT".equals(request.getIsDefault())) {}...
        // }//end of for...
        //
        // return new UpdateDeliveryResponse(deliveryList, request);
        
        // .getContent().stream().map(GetDeliveryDTO::new).collect(Collectors.toList())
        // 이게 map이 마치 if문처럼 작용 괄호 안의 부분은 리스트에서 꺼내는 형식, stream은 반복, map은 끝까지 모두 돌고,
        // filter() 는 조건에 맞는 구분, .findFirst() 특정 조건
        // this.deliveries = deliveryList.stream().map(GetDeliveryDTO::new).collect(Collectors.toList());
        
        /// ///////////////////////////////////////////////////////////////////////////////////////
        // 스트림으로 만들어야함
        
        // 받은 수정 정보가 디폴트라면
        if ("DEFAULT".equals(request.getIsDefault())) {
            List<Delivery> deliveryList = deliveryRepository.findByUser_Id(user.getId());
            // 스트림 객체 선언
            // Stream<Delivery> listStream =
            deliveryList.stream() // 번호가 같은 경우 업데이트 수행
                    .filter(delivery -> delivery.getId() == request.getDeliveryNo())
                    .forEach(delivery -> delivery.updateDelivery(request));
                    
            deliveryList.stream() // 번호가 같지 않는 경우는 모두 NONE 으로 바꿔주자
                    .filter(delivery -> delivery.getId() != request.getDeliveryNo())
                    .forEach(delivery -> delivery.updateDefault("NONE"));
            
        } else if ("NONE".equals(request.getIsDefault())) { // 만약 NONE 이라면 그냥 그 배송지번호의 정보만 수정함
            Delivery delivery = deliveryRepository.findById(request.getDeliveryNo())
                    .orElseThrow(DeliveryNotFoundException::new);
            
            delivery.updateDelivery(request); // 수정
        }
        
    }//end of public UpdateDeliveryResponse updateDelivery(UpdateDeliveryRequest request) throws IOException {}...
    
    
    
    // 배송지 삭제
    @Transactional
    public void deleteDelivery(Long id) throws IOException {
        
        // 딜리버리 번호로 검색
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(DeliveryNotFoundException::new);
        
        log.info("배송지 삭제 완료");
        
        // 검색되면 삭제
        deliveryRepository.delete(delivery);
    }//end of public void deleteDelivery(Long id) {}...
    
    
    // @Transactional
    public GetDeliverysResponse getDelivery(GetDeliverysRequest request) {
        
        // 프론트에서 받은 유저 번호가 정말 존재하는지 검색!
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(UserNotFondException::new);
        
        // 유저 번호로 검색해서 딜리버리 엔티티타입의 리스트로 뽑아내기
        Page<Delivery> deliveryList = deliveryRepository.findByUser_Id(user.getId(), request.getPageable());
        
        // 페이징네이션 객체를 페이징유틸 스테틱메소드로 만들어준다! (blockSize는 최대로 표시되는 페이지바 갯수)
        Pagination pagination = PagingUtil.getPagination(deliveryList, 5);
        
        return new GetDeliverysResponse(deliveryList, pagination);
    }//
    
    
    
    
    
    
    
    
    
    
}//end of class...




















