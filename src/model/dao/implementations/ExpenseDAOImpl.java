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
import database.DBIntegrityException;
import model.dao.GenericDAO;
import model.entities.Expense;
import model.entities.enums.Categories;

public class ExpenseDAOImpl implements GenericDAO<Expense> {

    @Override
    public void insert(Expense expense) {
        String query = "INSERT INTO expenses VALUES (DEFAULT, ?, ?, ?, ?)";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            conn.setAutoCommit(false);
                        
            pstmt.setString(1, expense.getDescription());
            pstmt.setObject(2, expense.getDate());
            pstmt.setBigDecimal(3, expense.getValue());
            pstmt.setString(4, expense.getCategory().name());

            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if(rs.next()) {
                    expense.setId(rs.getInt(1));
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
    public List<Expense> findAll() {
        String query = "SELECT * FROM expenses";
        ResultSet rs = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            rs = pstmt.executeQuery();
            return ImplementationService.createAndFillExpenseList(rs);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }
    }

    @Override
    public void update(Expense expense) {
        String query = "UPDATE expenses SET Description = ?, Date = ?, Value = ?, Category = ? WHERE IDe = ?";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, expense.getDescription());
            pstmt.setObject(2, expense.getDate());
            pstmt.setBigDecimal(3, expense.getValue());
            pstmt.setString(4, expense.getCategory().name());
            pstmt.setInt(5, expense.getId());
            pstmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override 
    public void delete(Integer ID) {
        String query = "DELETE FROM expenses WHERE IDe = ?";

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, ID);
            
            int rowsAffected = pstmt.executeUpdate();
            if(rowsAffected == 0)
                throw new IllegalArgumentException("Expense Id " + ID + " doesn't exists in database");
        }
        catch(SQLException e) {
            throw new DBIntegrityException(e.getMessage()); //Integrity error occurs because it is a foreign key of Person.
        } 
    }

    @Override
    public Optional<Expense> findById(Integer ID) {
        String query = "SELECT * FROM expenses WHERE IDe = ?";
        ResultSet rs = null;
        Expense expense = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
    
            pstmt.setInt(1, ID);
            rs = pstmt.executeQuery();
            if(rs.next())
                expense = ImplementationService.instantiateExpense(rs);
            return Optional.ofNullable(expense);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }   
    }

    @Override
    public List<Expense> findByCategory(Categories category) {
        String query = "SELECT * FROM expenses WHERE Category = ?";
        ResultSet rs = null;

        try(Connection conn = DBConfig.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, category.name());

            rs = pstmt.executeQuery();
            return ImplementationService.createAndFillExpenseList(rs);
        }
        catch(SQLException e) {
            throw new DBException(e.getMessage());
        }
        finally {
            DBConfig.closeResultSet(rs);
        }
    }   
}