package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.dto.CombinedOrderDTO;
import lk.ijse.pos_system_backend.dto.OrderDetailsDTO;

import java.sql.Connection;

public class OrderDB {
    public boolean saveOrder(CombinedOrderDTO combinedOrderDTO, Connection connection) {
        System.out.println(combinedOrderDTO.getOrderDTO().toString());

        for (OrderDetailsDTO orderDetailsDTO : combinedOrderDTO.getOrderDetailsDTOS()) {
            System.out.println(orderDetailsDTO.toString());
        }

        return true;
    }
}
