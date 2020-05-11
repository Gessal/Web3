package dao;

import com.sun.deploy.util.SessionState;
import model.BankClient;
import servlet.ResultServlet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {
        List<BankClient> result = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM bank_client");
            while (rs.next()) {
                result.add(new BankClient(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4)));
            }
            return result;
        } catch (SQLException e) {
            return result;
        }
    }

    public boolean validateClient(String name, String password) {
        BankClient client = getClientByName(name);
        if (client != null && client.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public void updateClientsMoney(String name, String password, Long transactValue) {
        if (validateClient(name, password)) {
            BankClient client = getClientByName(name);
            try (PreparedStatement st = connection.prepareStatement("UPDATE bank_client SET money = ? WHERE id = ?")) {
                st.setLong(1, client.getMoney() + transactValue);
                st.setLong(2, client.getId());
                st.executeUpdate();
            } catch (SQLException e) {}
        }
    }

    public BankClient getClientById(long id) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM bank_client WHERE id = ?")) {
            st.setLong(1, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new BankClient(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean isClientHasSum(String name, Long expectedSum) {
        BankClient client = getClientByName(name);
        if (client != null && client.getMoney() >= expectedSum) {
            return true;
        }
        return false;
    }

    public long getClientIdByName(String name) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("SELECT id FROM bank_client WHERE name = ?")) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            return -1;
        }
    }

    public BankClient getClientByName(String name) {
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM bank_client WHERE name = ?")) {
            st.setString(1, name);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return new BankClient(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getLong(4));
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public void addClient(BankClient client) throws SQLException {
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO bank_client (name, password, money) VALUES (?, ?, ?)")) {
            st.setString(1, client.getName());
            st.setString(2, client.getPassword());
            st.setLong(3, client.getMoney());
            st.executeUpdate();
        } catch (SQLException ignored) {}
    }

    public boolean delClient(String name) throws SQLException {
        long id = getClientIdByName(name);
        if (id != -1) {
            try (PreparedStatement st = connection.prepareStatement("DELETE FROM bank_client WHERE id = ?")) {
                st.setLong(1, id);
                st.executeUpdate();
                return true;
            } catch (SQLException e) {
                return false;
            }
        }
        return false;
    }

    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}