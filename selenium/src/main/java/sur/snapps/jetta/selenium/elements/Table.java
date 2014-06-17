package sur.snapps.jetta.selenium.elements;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public Map<String, Column> columns() {
        return columns;
    }

    public String id() {
        return id;
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

        return (cell != null) ? cell.findElements(By.tagName("a")) : Lists.<WebElement>newArrayList();
    }

    public WebElement cell(String columnName, int rowIndex) {
        return cell(row(rowIndex), columnName);
    }

    public WebElement cell(WebElement row, String columnName) {
        Column column = columns.get(columnName);
        int columnIndex = column.index() - 1;
        List<WebElement> cells = cells(row);

        if (columnIndex >= cells.size()) {
            if (column.optional()) {
                return null;
            }
            throw new IllegalArgumentException("not that many columns in table");
        }
        return cells.get(columnIndex);
    }

    private List<WebElement> cells(WebElement row) {
        return row.findElements(By.tagName("td"));
    }

    private WebElement row(int rowIndex) {
        if (rowIndex > numberOfRows()) {
            throw new IllegalArgumentException("not that many rows in table");
        }
        return rows().get(rowIndex - 1);
    }

    public RowCriteria rowCriteria() {
        return new RowCriteria(this, driver);
    }

}
