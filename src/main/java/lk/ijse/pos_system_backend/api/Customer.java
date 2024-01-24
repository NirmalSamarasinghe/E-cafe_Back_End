package lk.ijse.pos_system_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.pos_system_backend.Db.CustomerDb;
import lk.ijse.pos_system_backend.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "customer", urlPatterns = "/customer")
public class Customer extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Customer.class);
    Connection connection;

    @Override
    public void init() throws ServletException {
        try {
            InitialContext ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/aad");
            this.connection = pool.getConnection();
            logger.info("Initialized database connection");
        } catch (NamingException | SQLException e) {
            logger.error("Exception during initialization", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("generateCustomerId".equals(action)) {
            generateCustomerId(req, resp);
        } else if ("getAllCustomer".equals(action)) {
            getAllCustomer(req, resp);
        } else if ("getCustomer".equals(action)) {
            String custId = req.getParameter("customerId");
            getCustomer(req, resp, custId);
        }
    }

    private void getCustomer(HttpServletRequest req, HttpServletResponse resp, String custId) {
        var customerDb = new CustomerDb();
        CustomerDTO customerDTO = customerDb.getCustomer(connection, custId);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(customerDTO);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned customer successfully: {}", custId);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while getting customer", e);
            throw new RuntimeException(e);
        }
    }

    private void generateCustomerId(HttpServletRequest req, HttpServletResponse resp) {
        CustomerDb customerDb = new CustomerDb();
        String customerId = customerDb.generateCustomerId(connection);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(customerId);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned generated customer ID successfully: {}", customerId);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while generating customer ID", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            var customerDb = new CustomerDb();
            boolean result = customerDb.saveCustomer(connection, customerDTO);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Customer information saved successfully!");
                logger.info("Customer information saved successfully: {}", customerDTO.toString());
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save customer information!");
                logger.error("Failed to save customer information.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("Invalid request format for POST");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            CustomerDTO customerDTO = jsonb.fromJson(req.getReader(), CustomerDTO.class);

            var customerDb = new CustomerDb();
            boolean result = customerDb.updateCustomer(connection, customerDTO);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Customer information updated successfully!");
                logger.info("Customer information updated successfully: {}", customerDTO.toString());
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update customer information!");
                logger.error("Failed to update customer information.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("Invalid request format for PUT");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String custId = req.getParameter("customerId");
        var customerDb = new CustomerDb();
        boolean result = customerDb.deleteCustomer(connection, custId);

        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Customer information deleted successfully!");
            logger.info("Customer information deleted successfully: {}", custId);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete customer information!");
            logger.error("Failed to delete customer information.");
        }
    }

    private void getAllCustomer(HttpServletRequest req, HttpServletResponse resp) {
        var customerDb = new CustomerDb();
        ArrayList<CustomerDTO> allCustomer = customerDb.getAllCustomer(connection);

        Jsonb jsonb = JsonbBuilder.create();
        var json = jsonb.toJson(allCustomer);

        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned all customers successfully");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while getting all customers", e);
            throw new RuntimeException(e);
        }
    }
}
