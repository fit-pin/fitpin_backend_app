package com.mzm.Fitpin.controller.app.order;

import com.mzm.Fitpin.dto.order.OrderDTO;
import com.mzm.Fitpin.mapper.ItemImgMapper;
import com.mzm.Fitpin.mapper.order.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
//TODO: 수선된 사이즈, 구매 날짜, 수선 상태 여부 추가
//TODO: 구매날짜 추가 완료, 수선 상태 여부 추가 완료, 수선된 사이즈 반환 작업 해야함.
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ItemImgMapper itemImgMapper;

    @PostMapping("/post_order") //주문등록
    public ResponseEntity<?> postOrder(@RequestBody OrderDTO orderDTO) {
        try {
            // itemKey로 이미지명 조회 및 설정
            String itemImgName = itemImgMapper.getItemImgNameByItemKey(orderDTO.getItemKey());
            orderDTO.setItemImg(itemImgName);

            // 주문 등록
            orderMapper.insertOrder(orderDTO);
            return ResponseEntity.ok(Collections.singletonMap("message", "주문 등록 완료."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "알수없는 오류가 발생했습니다."));
        }
    }

    @GetMapping("/get_order/{userEmail}") // 주문 조회
    public ResponseEntity<?> getOrder(@PathVariable String userEmail) {
        try {
            List<OrderDTO> orderLists = orderMapper.getOrderByUserKey(userEmail);
            if (orderLists.isEmpty()) {
                return ResponseEntity.status(404).body(Collections.singletonMap("message", "주문 리스트가 없습니다."));
            }

            // 조건에 따른 필드 수정 로직
            List<OrderDTO> processedOrders = orderLists.stream().map(order -> {
                // 수선 여부 처리 (0: false, 1: true)
                order.setPitStatus(order.isPitStatus() == true); //boolean타입은 get(메서드명)이 아닌 is(메서드명)로 가져와야함

                // 수선 비용 처리 (null인 경우 "경매중")
                order.setDisplayPitPrice(Objects.isNull(order.getPitPrice()) ? "경매중" : String.valueOf(order.getPitPrice()));

                // 주문 상태 처리 (0: 결제 완료, 1: 배송중, 2: 배송완료)
                switch (order.getOrderStatus()) {
                    case 0:
                        order.setDisplayOrderStatus("결제 완료");
                        break;
                    case 1:
                        order.setDisplayOrderStatus("배송중");
                        break;
                    case 2:
                        order.setDisplayOrderStatus("배송완료");
                        break;
                    default:
                        order.setDisplayOrderStatus("알 수 없음");
                }
                return order;
            }).toList();

            return ResponseEntity.ok(processedOrders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "주문 조회 중 오류가 발생했습니다."));
        }
    }

    @PutMapping("/update_status/{orderKey}") // 주문 상태 갱신 API
    public ResponseEntity<?> updateOrderStatus(@PathVariable int orderKey, @RequestParam int orderStatus) {
        try {
            int rowsAffected = orderMapper.updateOrderStatus(orderKey, orderStatus);
            if (rowsAffected > 0) {
                return ResponseEntity.ok(Collections.singletonMap("message", "주문 상태가 성공적으로 업데이트되었습니다."));
            } else {
                return ResponseEntity.status(404).body(Collections.singletonMap("message", "해당 주문을 찾을 수 없습니다."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Collections.singletonMap("message", "주문 상태 업데이트 중 오류가 발생했습니다."));
        }
    }

}
