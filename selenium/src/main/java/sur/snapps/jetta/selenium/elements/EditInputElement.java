package sur.snapps.jetta.selenium.elements;

import com.google.common.collect.Lists;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * User: SUR
 * Date: 5/07/14
 * Time: 23:10
 */
public class EditInputElement {

    private static final String DATA_EDIT_GROUP_READONLY = "data-edit-group-readonly";
    private static final String DATA_EDIT_GROUP = "data-edit-group";

    private String model;
    private String readOnlyField;
    private List<String> editFields;
    private WebDriver driver;

    public EditInputElement(WebDriver driver, EditField editField) {
        this.model = editField.model();
        this.readOnlyField = editField.readOnlyField();
        this.editFields = Lists.newArrayList(editField.editFields());
        this.driver = driver;
    }

    public void edit(String value) {
        getEditButton().click();
        WebElement editField = getSingleEditField();
        editField.clear();
        editField.sendKeys(value);
        getSubmitButton().click();
    }

    public WebElement getSingleEditField() {
        if (editFields.size() != 1) {
            throw new IllegalStateException("cannot ask single edit field if more than one edit field is present");
        }
        return driver.findElement(By.id(editFields.get(0)));
    }

    // TODO confirm that input is visible

    public String getReadOnlyValue() {
        return driver.findElement(By.xpath(selectReadOnlyFormGroup() + "//p")).getText().trim();
    }

    public WebElement getEditButton() {
        return driver.findElement(By.xpath(selectReadOnlyFormGroup() + "//a[contains(@onclick,'sur.edit')]"));
    }

    public WebElement getSubmitButton() {
        return driver.findElement(By.xpath(selectEditFormGroup() + "//a[contains(@onclick, 'sur.submit')]"));
    }

    public WebElement getCancelButton() {
        return driver.findElement(By.xpath(selectEditFormGroup() + "//a[contains(@onclick, 'sur.cancel')]"));
    }

    private String selectReadOnlyFormGroup() {
        return selectFormGroupForField(DATA_EDIT_GROUP_READONLY);
    }

    private String selectEditFormGroup() {
        return selectFormGroupForField(DATA_EDIT_GROUP);
    }

    private String selectFormGroupForField(String formGroup) {
        return "//div[contains(@class,'form-group')][@" + formGroup + "='" + model + "-" + readOnlyField + "']";
    }
}
