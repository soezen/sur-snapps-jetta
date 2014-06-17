package sur.snapps.jetta.selenium.elements;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * User: SUR
 * Date: 4/06/14
 * Time: 19:21
 */
public class RowCriteria {

    private static final String CHECKED_ICON_CLASS = "fa-check-circle-o";
    private static final String NOT_CHECKED_ICON_CLASS = "fa-circle-o";

    private Table table;
    private WebDriver driver;
    private Wait<WebDriver> wait;
    private List<String> constraints;

    public RowCriteria(Table table, WebDriver driver) {
        this.table = table;
        this.driver = driver;
        wait = new FluentWait<WebDriver>(driver)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .withTimeout(3, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class);
        constraints = Lists.newArrayList();
    }

    public RowCriteria columnHasLinkWithText(String columnName, String linkText) {
        constraints.add(xpathSelectColumn(columnName) + "[a/span[normalize-space()='" + linkText + "']]");
        return this;
    }

    public RowCriteria columnHasValue(String columnName, String value) {
        // TODO validate that columnName is present in list of columns
        constraints.add(xpathSelectColumn(columnName) + "[normalize-space()='" + value + "']");
        return this;
    }

    public RowCriteria columnHasCheckIcon(String columnName, boolean checked) {
        // TODO allow this to be configured by client
        String classPresent = checked ? CHECKED_ICON_CLASS : NOT_CHECKED_ICON_CLASS;
        constraints.add(xpathSelectColumn(columnName) + "[i[contains(@class, '" + classPresent + "')]]");
        return this;
    }

    public RowCriteria columnHasLinks(String columnName, int nbrOfLinks) {
        constraints.add(xpathSelectColumn(columnName) + "[count(a[not(contains(@style, 'display: none;'))])=" + nbrOfLinks + "]");
        return this;
    }

    private String xpathSelectColumn(String columnName) {
        return "td[" + columnIndex(columnName) + "]";
    }

    public RowCriteria rowHasNumberOfColumns(int nbrOfColumns) {
        constraints.add("count(td)=" + nbrOfColumns);
        return this;
    }

    public WebElement row() {
        final String xpathExpression = xpathForRow();
        try {
            return wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(@Nullable WebDriver driver) {
                    return driver == null ? null : driver.findElement(By.xpath(xpathExpression));
                }
            });
        } catch (TimeoutException timeoutException) {
            // TODO only do this if logging is turned on
            System.out.println("NO SUCH ELEMENT: " + xpathExpression);
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
