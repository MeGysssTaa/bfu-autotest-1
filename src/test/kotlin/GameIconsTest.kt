import io.github.bonigarcia.wdm.WebDriverManager
import io.qameta.allure.Step
import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.interactions.Actions
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameIconsTest {
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

    @Test
    fun `test find and download capybara png image from game icons leads to direct file url`() {
        openWebsite("https://game-icons.net")
        searchUsingField("algolia-search", "capybara")
        ensureLandedOnPage("https://game-icons.net/1x1/caro-asercion/capybara.html")
        clickDownloadButton(2) // PNG
        ensureLandedOnPage("https://game-icons.net/icons/ffffff/000000/1x1/caro-asercion/capybara.png")
        checkContainsImage(By.xpath("/html/body/img"))
    }

    @Step("Open website \"{url}\"")
    fun openWebsite(url: String) = driver.get(url)

    @Step("Search for \"{text}\" (first match) using text field with element ID \"{fieldId}\"")
    fun searchUsingField(fieldId: String, text: String) {
        val textField = driver.findElement(By.id(fieldId))
        Actions(driver).moveToElement(textField).perform() // fixes "Element ... is not reachable by keyboard"
        textField.sendKeys(text)
        Thread.sleep(1000)
        textField.sendKeys(Keys.DOWN)
        Thread.sleep(100)
        textField.sendKeys(Keys.ENTER)
        Thread.sleep(100)
    }

    @Step("Ensure that we landed on page \"{url}\"")
    fun ensureLandedOnPage(url: String) = assertEquals(url, driver.currentUrl)

    @Step("Click download button #{buttonNum}")
    fun clickDownloadButton(buttonNum: Int) {
        val downloadButtonsList = driver.findElement(By.className("download"))
        val targetBtn = downloadButtonsList.findElement(By.xpath("./li[$buttonNum]"))
        targetBtn.click()
    }

    @Step("Check that the current page contains an image")
    fun checkContainsImage(imageFinder: By) {
        if (driver.findElements(imageFinder).isEmpty()) {
            fail("image not found")
        }
    }
}
