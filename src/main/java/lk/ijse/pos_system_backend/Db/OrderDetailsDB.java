package lk.ijse.pos_system_backend.Db;

import lk.ijse.pos_system_backend.api.Customer;
import lk.ijse.pos_system_backend.dto.OrderDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.mysql.cj.conf.PropertyKey.logger;

public class OrderDetailsDB {
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailsDB.class);
    public boolean saveOrderDetails(OrderDetailsDTO orderDetailsDTO, Connection connection) {
        try {
            String saveItem = "INSERT INTO OrderDetails (order_id, item_id, price, qty) VALUES (?, ?, ?, ?);";

            try (PreparedStatement preparedStatement = connection.prepareStatement(saveItem)) {
                preparedStatement.setString(1, orderDetailsDTO.getOrder_id());
                preparedStatement.setString(2, orderDetailsDTO.getItem_id());
                preparedStatement.setDouble(3, orderDetailsDTO.getPrice());
                preparedStatement.setInt(4, orderDetailsDTO.getQty());

                boolean result = preparedStatement.executeUpdate() != 0;
                if (result) {
//                    logger.info("Order details saved successfully: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                } else {
//                    logger.error("Failed to save order details: OrderID={}, ItemID={}", orderDetailsDTO.getOrder_id(), orderDetailsDTO.getItem_id());
                }
                return result;
            }
        } catch (SQLException e) {
//            logger.error("Error saving order details", e);
            throw new RuntimeException(e);
        }
    }

    public ArrayList<OrderDetailsDTO> getOrderDetails(String orderId, Connection connection) {
        ArrayList<OrderDetailsDTO> orderDetailsList = new ArrayList<>();

        try {
            String getOrderDetailsQuery = "SELECT * FROM OrderDetails WHERE order_id = ?;";

            try (PreparedStatement getOrderDetailsStatement = connection.prepareStatement(getOrderDetailsQuery)) {
                getOrderDetailsStatement.setString(1, orderId);
                try (ResultSet resultSet = getOrderDetailsStatement.executeQuery()) {
                    while (resultSet.next()) {
                        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
                        orderDetailsDTO.setOrder_id(resultSet.getString("order_id"));
                        orderDetailsDTO.setItem_id(resultSet.getString("item_id"));
                        orderDetailsDTO.setPrice(resultSet.getDouble("price"));
                        orderDetailsDTO.setQty(resultSet.getInt("qty"));
                        orderDetailsList.add(orderDetailsDTO);
                    }
                }
            }
            return orderDetailsList;

        } catch (SQLException e) {
//            logger.error("Error retrieving order details", e);
            throw new RuntimeException(e);
        }
    }

    public boolean deleteOrderDetails(String orderId, Connection connection) {
        try {
            String deleteOrderDetailsQuery = "DELETE FROM OrderDetails WHERE order_id = ?;";

            try (PreparedStatement deleteOrderDetailsStatement = connection.prepareStatement(deleteOrderDetailsQuery)) {
                deleteOrderDetailsStatement.setString(1, orderId);

                boolean result = deleteOrderDetailsStatement.executeUpdate() != 0;
                if (result) {
//                    logger.info("Order details deleted successfully: OrderID={}", orderId);
                } else {
//                    logger.error("Failed to delete order details: OrderID={}", orderId);
                }
                return result;
            }
        } catch (SQLException e) {
//            logger.error("Error deleting order details", e);
            throw new RuntimeException(e);
        }
    }
}
