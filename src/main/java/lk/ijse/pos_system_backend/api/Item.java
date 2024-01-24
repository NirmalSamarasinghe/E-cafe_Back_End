package lk.ijse.pos_system_backend.api;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.pos_system_backend.Db.ItemDb;
import lk.ijse.pos_system_backend.dto.ItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "item", urlPatterns = "/item")
public class Item extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(Item.class);
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
        if ("generateItemCode".equals(action)) {
            generateItemCode(req, resp);
        } else if ("getAllItem".equals(action)) {
            getAllItem(req, resp);
        } else if ("getItem".equals(action)) {
            String code = req.getParameter("itemCode");
            getItem(req, resp, code);
        }
    }

    private void generateItemCode(HttpServletRequest req, HttpServletResponse resp) {
        ItemDb itemDb = new ItemDb();
        String itemCode = itemDb.generateItemCode(connection);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(itemCode);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned generated item code successfully: {}", itemCode);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while generating item code", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getContentType() != null && req.getContentType().toLowerCase().startsWith("application/json")) {
            Jsonb jsonb = JsonbBuilder.create();
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            var itemDb = new ItemDb();
            boolean result = itemDb.saveItem(connection, itemDTO);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Item information saved successfully!");
                logger.info("Item information saved successfully: {}", itemDTO.toString());
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to save item information!");
                logger.error("Failed to save item information.");
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
            ItemDTO itemDTO = jsonb.fromJson(req.getReader(), ItemDTO.class);

            var itemDb = new ItemDb();
            boolean result = itemDb.updateItem(connection, itemDTO);

            if (result) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Item information updated successfully!");
                logger.info("Item information updated successfully: {}", itemDTO.toString());
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update item information!");
                logger.error("Failed to update item information.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            logger.warn("Invalid request format for PUT");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String code = req.getParameter("itemCode");
        var itemDb = new ItemDb();
        boolean result = itemDb.deleteItem(connection, code);

        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Item information deleted successfully!");
            logger.info("Item information deleted successfully: {}", code);
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete item information!");
            logger.error("Failed to delete item information.");
        }
    }

    private void getAllItem(HttpServletRequest req, HttpServletResponse resp) {
        var itemDb = new ItemDb();
        ArrayList<ItemDTO> allItem = itemDb.getAllItem(connection);

        Jsonb jsonb = JsonbBuilder.create();
        var json = jsonb.toJson(allItem);

        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned all items successfully");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while getting all items", e);
            throw new RuntimeException(e);
        }
    }

    private void getItem(HttpServletRequest req, HttpServletResponse resp, String code) {
        var itemDb = new ItemDb();
        ItemDTO itemDTO = itemDb.getItem(connection, code);
        Jsonb jsonb = JsonbBuilder.create();

        var json = jsonb.toJson(itemDTO);
        resp.setContentType("application/json");
        try {
            resp.getWriter().write(json);
            logger.debug("Returned item successfully: {}", code);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            logger.error("Exception while getting item", e);
            throw new RuntimeException(e);
        }
    }
}