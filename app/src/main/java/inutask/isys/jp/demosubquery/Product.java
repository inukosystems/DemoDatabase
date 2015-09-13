package inutask.isys.jp.demosubquery;

/**
 * Created by Yellow on 2015/08/22.
 */
public class Product {
    private String title;
    private String category;
    private int year;

    public Product(String title, String category, int year) {
        this.title = title;
        this.category = category;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getYear() {
        return year;
    }
}
