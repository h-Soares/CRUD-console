package model.dao.implementations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.entities.Expense;
import model.entities.Person;
import model.entities.enums.Categories;

public class ImplementationService {
    protected static Expense instantiateExpense(ResultSet rs) throws SQLException {
        Expense expense = new Expense();
        expense.setId(rs.getInt("IDe"));
        expense.setDescription(rs.getString("Description"));
        expense.setDate(rs.getTimestamp("Date").toLocalDateTime());
        expense.setValue(rs.getBigDecimal("Value"));
        expense.setCategory(Categories.valueOf(rs.getString("Category")));
        return expense;
    }

    protected static Person instantiatePerson(ResultSet rs, Expense expense) throws SQLException {
        Person person = new Person();
        person.setId(rs.getInt("IDp"));
        person.setName(rs.getString("Name"));
        person.setExpense(expense);
        return person;
    }

    protected static List<Expense> createAndFillExpenseList(ResultSet rs) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        while(rs.next())
            expenses.add(ImplementationService.instantiateExpense(rs));
        return expenses;
    }

    protected static List<Person> createAndFillPersonList(ResultSet rs) throws SQLException {
        List<Person> persons = new ArrayList<>();
        while(rs.next()) {
            Expense expense = ImplementationService.instantiateExpense(rs);
            Person person = ImplementationService.instantiatePerson(rs, expense);
            persons.add(person);
        }
        return persons;
    }
}