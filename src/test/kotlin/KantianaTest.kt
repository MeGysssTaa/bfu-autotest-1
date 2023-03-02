import io.github.bonigarcia.wdm.WebDriverManager
import io.qameta.allure.Step
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import kotlin.test.assertEquals
import kotlin.test.fail

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KantianaTest {
    private lateinit var driver: WebDriver

    @BeforeAll
    fun setUp() {
        WebDriverManager.firefoxdriver().setup()
        driver = FirefoxDriver()
        driver.manage().window().maximize()
    }

    @AfterAll
    fun tearDown() {
        driver.quit()
    }

    @Step("Open website \"{url}\"")
    fun openWebsite(url: String) = driver.get(url)

    @Step("Hover element \"{debugName}\"")
    fun hoverElement(@Suppress("UNUSED_PARAMETER") debugName: String, elemFinder: By) {
        val elem = driver.findElement(elemFinder)
        Actions(driver).moveToElement(elem).perform()
    }

    @Step("Click element \"{debugName}\"")
    fun clickElement(@Suppress("UNUSED_PARAMETER") debugName: String, elemFinder: By) {
        val elem = driver.findElement(elemFinder)
        Actions(driver).moveToElement(elem).perform() // fixes "element ... could not be scrolled into view"
        elem.click()
    }

    @Step("Ensure that we landed on page \"{url}\"")
    fun ensureLandedOnPage(url: String) = assertEquals(driver.currentUrl, url)

    @Step("Ensure that element \"{debugName}\" exists and its text is equal to \"text\"")
    fun checkElemText(@Suppress("UNUSED_PARAMETER") debugName: String, elemFinder: By, text: String) {
        val elem = try {
            driver.findElement(elemFinder)
        } catch (_: NoSuchElementException) {
            fail("element \"$debugName\" could not be found")
        }
        assertEquals(elem.text, text)
    }

    @Test
    fun `test open kantiana and navigate to admission dates leads us to the proper page`() {
        openWebsite("https://kantiana.ru")
        hoverElement("Drop-down list/button 'Enrollee'", By.xpath(
            "//div[" +
                    "contains(@class, 'menu-main__item--lvl-1') " +
                    "and contains(.//span, 'Абитуриенту')" +
                    "]"
        ))
        clickElement("Button 'Admission dates'", By.xpath(
            "//div[" +
                    "contains(@class, 'menu-main__item--lvl-3') " +
                    "and contains(.//span, 'Сроки проведения')" +
                    "]"
        ))
        ensureLandedOnPage("https://kantiana.ru/enrollee/admission-committee/sroki-provedeniya-priema/#")
        checkElemText("Title 'Admission dates'", By.xpath(
            "//h1[contains(@class, 'page__title')]"
        ), "Сроки проведения приема")
    }
}
