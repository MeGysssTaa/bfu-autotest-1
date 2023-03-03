
import io.github.bonigarcia.wdm.WebDriverManager
import io.qameta.allure.Step
import org.junit.jupiter.api.*
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.firefox.FirefoxDriver
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeeplTranslateTest {
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

    @Step("Expand output languages selector")
    fun expandOutputLanguagesSelector() {
        val selectors = driver.findElements(By.className("lmt__language_select__active__title"))
        val outputSelector = selectors[1] // target language
        outputSelector.click()
    }

    @Step("Select output language using button at ({langButtonRow}, {langButtonCol})")
    fun selectOutputLanguage(langButtonCol: Int, langButtonRow: Int) {
        val langsCol = driver.findElements(By.className("lmt__language_select_column"))[langButtonCol - 1]
        val langBtn = langsCol.findElements(By.xpath(".//*"))[langButtonRow + 1]
        langBtn.click()
    }

    @Step("Enter text \"{textToTranslate}\" as input")
    fun enterInputTextAnyLanguage(textToTranslate: String) {
        val inputTextAreaContainer = driver.findElements(By.className("lmt__inner_textarea_container"))[0]
        inputTextAreaContainer.click()
        val inputTextArea = inputTextAreaContainer.findElement(By.xpath("./d-textarea/div"))
        inputTextArea.sendKeys(textToTranslate)
    }

    @Step("Check that output is similar to \"{expectedTranslation}\"")
    fun checkOutput(expectedTranslation: String) {
        val outputTextAreaContainer = driver.findElements(By.className("lmt__inner_textarea_container"))[1]
        val outputTextArea = outputTextAreaContainer.findElement(By.xpath("./d-textarea/div"))
        assertEquals(expectedTranslation.lowercase().trim(), outputTextArea.text.lowercase().trim())
    }

    @Test
    fun `test deepl website translates english hello world to german hallo welt correctly`() {
        openWebsite("https://www.deepl.com/translator")
        expandOutputLanguagesSelector()
        selectOutputLanguage(2, 3) // German/Deutsch
        enterInputTextAnyLanguage("Hello, world!") // input-lang should likely differ from output-lang ^
        Thread.sleep(1000)
        checkOutput("hallo, welt!")
    }
}
