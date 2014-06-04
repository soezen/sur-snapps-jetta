package sur.snapps.jetta.selenium.elements;

import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Iterator;
import java.util.List;

/**
 * User: SUR
 * Date: 4/06/14
 * Time: 19:21
 */
public class RowCriteria {

    private Table table;
    private WebDriver driver;
    private List<String> constraints;

    public RowCriteria(Table table, WebDriver driver) {
        this.table = table;
        this.driver = driver;
        constraints = Lists.newArrayList();
    }

    public RowCriteria columnHasValue(String columnName, String value) {
        // TODO validate that columnName is present in list of columns
        constraints.add("td[" + columnIndex(columnName) + "][text()='" + value + "']");
        return this;
    }

    public RowCriteria columnHasLinks(String columnName, int nbrOfLinks) {
        constraints.add("td[" + columnIndex(columnName) + "][count(a)=" + nbrOfLinks + "]");
        return this;
    }

    public RowCriteria rowHasNumberOfColumns(int nbrOfColumns) {
        constraints.add("count(td)=" + nbrOfColumns);
        return this;
    }

    public WebElement row() {
        try {
            return driver.findElement(By.xpath(xpathForRow()));
        } catch (NoSuchElementException ignored) {
        }
        return null;
    }

    public WebElement cell(String columnName) {
        return driver.findElement(By.xpath(xpathForCell(columnName)));
    }

    public WebElement link(String columnName, String actionName) {
        for (WebElement link : cell(columnName).findElements(By.tagName("a"))) {
            if (actionName.equals(link.getAttribute("name"))) {
                return link;
            }
        }
        return null;
    }

    private String xpathForRow() {
        StringBuilder xpath = new StringBuilder("//table[@id='" + table.id() + "']/tbody/tr[");
//table[@id='users']/tbody/tr[td[1][text()='John Smith'] and td[2][text()='hannibal@a-team.com'] and td[3][count(a)=3]/td[3]
        Iterator<String> it = constraints.iterator();
        while (it.hasNext()) {
            String constraint = it.next();

            xpath.append(constraint);
            if (it.hasNext()) {
                xpath.append(" and ");
            }
        }

        xpath.append("]");
        return xpath.toString();
    }

    private String xpathForCell(String columnName) {
        return xpathForRow()
                .concat("/td[")
                .concat(String.valueOf(columnIndex(columnName)))
                .concat("]");
    }

    private int columnIndex(String columnName) {
        return table.columns().get(columnName).index();
    }
}
