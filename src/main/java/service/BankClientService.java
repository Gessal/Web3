package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.*;
import java.util.List;

public class BankClientService {

    public BankClientService() {
    }

    public BankClient getClientById(long id) {
        return getBankClientDAO().getClientById(id);
    }

    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean deleteClient(String name) {
        return getBankClientDAO().delClient(name);
    }

    public boolean addClient(BankClient client) {
        if (getBankClientDAO().getClientIdByName(client.getName()) == -1) {
            getBankClientDAO().addClient(client);
            return true;
        }
        return false;
    }

    public boolean sendMoneyToClient(BankClient clientFrom, String name, Long value) {
        BankClient clientTo = getBankClientDAO().getClientByName(name);
        if (getBankClientDAO().isClientHasSum(clientFrom.getName(), value) && clientTo != null) {
            getBankClientDAO().sendMoney(clientFrom, clientTo, value);
            return true;
        }
        return false;
    }
    /* Вынес проверкиу пароля в отдельный метод BankClientService */
    public boolean checkPassword(String name, String password) {
        return getBankClientDAO().validateClient(name, password);
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }
    public void createTable() throws DBException{
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=root&").          //login
                    append("password=root");       //password

            System.out.println("URL: " + url + "\n");

            Connection connection = DriverManager.getConnection(url.toString());
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
