package sur.snapps.jetta.modules.selenium;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sur.snapps.jetta.modules.selenium.annotations.Column;

import java.util.List;
import java.util.Map;

/**
 * User: SUR
 * Date: 26/05/14
 * Time: 16:37
 */
public class Table {

    private final String id;
    private final WebDriver driver;

    private List<WebElement> rows;
    private Map<String, Column> columns;

    public Table(WebDriver driver, String id) {
        this.id = id;
        this.driver = driver;
        columns = Maps.newHashMap();
    }

    public boolean isPresent() {
        return !driver.findElements(By.id(id)).isEmpty();
    }

    public List<WebElement> rows() {
        if (rows == null) {
            rows = driver.findElements(By.xpath("//table[@id = '" + id + "']/tbody/tr"));
        }
        return rows;
    }

    public int numberOfRows() {
        return rows().size();
    }

    public String cellValue(String columnName, int rowIndex) {
        return cell(columnName, rowIndex).getText();
    }

    public List<WebElement> links(String columnName, int rowIndex) {
        WebElement cell = cell(columnName, rowIndex);

        return cell == null
                ? Lists.<WebElement>newArrayList()
                : cell.findElements(By.tagName("a"));
    }

    public WebElement cell(String columnName, int rowIndex) {
        Column column = columns.get(columnName);
        int columnIndex = column.index();
        List<WebElement> cells = cells(rowIndex);

        if (columnIndex >= cells.size()) {
            if (column.optional()) {
                return null;
            }
            throw new IllegalArgumentException("not that many columns in table");
        }
        return cells.get(columnIndex);
    }

    private List<WebElement> cells(int rowIndex) {
        return row(rowIndex).findElements(By.tagName("td"));
    }

    private WebElement row(int rowIndex) {
        if (rowIndex > numberOfRows()) {
            throw new IllegalArgumentException("not that many rows in table");
        }
        return rows().get(rowIndex - 1);
    }

}
