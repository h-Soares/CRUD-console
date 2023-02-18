package app;

import java.util.Locale;
import java.util.Optional;
import model.dao.GenericDAO;
import model.dao.implementations.ExpenseDAOImpl;
import model.dao.implementations.PersonDAOImpl;
import model.entities.Expense;
import model.entities.Person;
import model.entities.enums.Categories;
 
public class App {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        GenericDAO<Expense> test = new ExpenseDAOImpl();

        test.findAll().forEach(System.out::println);
        
        Optional<Expense> expense = test.findById(6);
        if(expense.isPresent())
            System.out.println(expense.get());
        else
            System.out.println("This expense doesn't exists!"); 
        test.findByCategory(Categories.EDUCATION).forEach(System.out::println);

        GenericDAO<Person> test2 = new PersonDAOImpl();
        test2.findAll().forEach(System.out::println);
        test2.findByCategory(Categories.EDUCATION).forEach(System.out::println);

        /* And more tests.... */
    }
}