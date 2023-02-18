package model.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import database.DBConfig;
import database.DBException;
import model.dao.GenericDAO;
import model.entities.Expense;
import model.entities.Person;
import model.entities.enums.Categories;

public class PersonDAOImpl implements GenericDAO<Person> {

    @Override
    public void insert(Person person) {
        String query = "INSERT INTO person VALUES (DEFAULT, ?, ?)";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            conn.setAutoCommit(false);

            pstmt.setString(1, person.getName());
            pstmt.setInt(2, person.getExpense().getId());

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    person.setId(rs.getInt(1));
                    conn.commit();
                }
                DBConfig.closeResultSet(rs);
            }
            else {
                conn.rollback();
                throw new DBException("ERROR: no rows affected");
            }
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public List<Person> findAll() {
        String query = "SELECT * FROM person JOIN expenses ON Expense_Id = IDe";
        ResultSet rs = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            rs = pstmt.executeQuery();
            return ImplementationService.createAndFillPersonList(rs);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }
    }

    @Override
    public void update(Person person) {
        String query = "UPDATE person SET Name = ?, Expense_Id = ? WHERE IDp = ?";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, person.getName());
            pstmt.setInt(2, person.getExpense().getId());
            pstmt.setInt(3, person.getId());
            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void delete(Integer ID) {
        String query = "DELETE FROM person WHERE IDp = ?";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ID);

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected == 0)
                throw new IllegalArgumentException("Person Id " + ID + " doesn't exists in database");
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public Optional<Person> findById(Integer ID) {
        String query = "SELECT * FROM person JOIN expenses ON Expense_Id = IDe WHERE IDp = ?";
        ResultSet rs = null;
        Person person = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ID);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Expense expense = ImplementationService.instantiateExpense(rs);
                person = ImplementationService.instantiatePerson(rs, expense);
            }
            return Optional.ofNullable(person);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }
    }

    @Override
    public List<Person> findByCategory(Categories category) {
        String query = "SELECT * FROM person JOIN expenses ON Expense_Id = IDe WHERE Category = ?";
        ResultSet rs = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, category.name());

            rs = pstmt.executeQuery();
            return ImplementationService.createAndFillPersonList(rs);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }
    }
}