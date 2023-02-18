package model.entities;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.entities.enums.Categories;

public class Expense {
    private Integer Id;
    private String description;
    private LocalDateTime date;
    private BigDecimal value;
    private Categories category;

    private static DateTimeFormatter dfmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static NumberFormat nfmt = NumberFormat.getCurrencyInstance();

    public Expense() {
    }

    public Expense(String description, LocalDateTime date, BigDecimal value, Categories category) {
        this.description = description;
        this.date = date;
        this.value = value;
        this.category = category;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: " + Id + ", ");
        sb.append("Description: " + description + ", ");
        sb.append("Date: " + date.format(dfmt) + ", ");
        sb.append("Value: " + nfmt.format(value) + ", ");
        sb.append("Category: " + category);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Expense other = (Expense) obj;
        if (Id == null) {
            if (other.Id != null)
                return false;
        } else if (!Id.equals(other.Id))
            return false;
        return true;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }
}