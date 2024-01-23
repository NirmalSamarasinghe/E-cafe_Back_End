package lk.ijse.pos_system_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CombinedOrderDTO {
    private OrderDTO orderDTO;
    private ArrayList<OrderDetailsDTO> orderDetailsDTOS;

}
