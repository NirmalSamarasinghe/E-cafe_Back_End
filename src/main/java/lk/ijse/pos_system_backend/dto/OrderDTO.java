package lk.ijse.pos_system_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class OrderDTO {
    private String order_date;
    private String order_id;
    private String customer_id;
    private double total;
    private double discount;
    private double cash;
}
