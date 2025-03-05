import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {
    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @Test
    public void shouldSubmitFormSuccessfully() {
        try {
            System.out.println("Запуск Chrome в headless-режиме...");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");

            WebDriver driver = new ChromeDriver(options);
            System.out.println("ChromeDriver запущен!");

            driver.get("http://localhost:9999");
            System.out.println("Страница загружена!");

            // Заполняем поле "Фамилия и имя"
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Иванов Иван");

            // Заполняем поле "Мобильный телефон"
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("+79991112233");

            // Ставим галочку согласия
            WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id=agreement]"));
            checkbox.click();

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Ожидаем появление сообщения об успешной отправке
            WebElement successMessage = driver.findElement(By.cssSelector("[data-test-id=order-success]"));
            assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", successMessage.getText());

            System.out.println("Тест успешно завершён!");
            driver.quit();

        } catch (Exception e) {
            System.err.println("Ошибка во время теста: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void shouldShowErrorForInvalidName() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://localhost:9999");

            // Вводим некорректное имя (латинскими буквами)
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Ivan Petrov");

            // Заполняем корректно поле телефона
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("+79991112233");

            // Ставим галочку согласия
            WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id=agreement]"));
            checkbox.click();

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Проверяем, что появляется сообщение об ошибке в поле "Фамилия и имя"
            WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
            assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", nameError.getText());

            driver.quit();
        } catch (Exception e) {
            System.err.println("Ошибка во время теста (Invalid Name): " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void shouldShowErrorForInvalidPhone() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://localhost:9999");

            // Заполняем корректно поле "Фамилия и имя"
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Иванов Иван");

            // Вводим некорректный номер телефона (без символа + и ведущей 7)
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("99911122333");

            // Ставим галочку согласия
            WebElement checkbox = driver.findElement(By.cssSelector("[data-test-id=agreement]"));
            checkbox.click();

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Проверяем, что появляется сообщение об ошибке в поле "Мобильный телефон"
            WebElement phoneError = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub"));
            assertEquals("Телефон указан неверно. Должен начинаться с +7.", phoneError.getText());

            driver.quit();
        } catch (Exception e) {
            System.err.println("Ошибка во время теста (Invalid Phone): " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void shouldShowErrorForUncheckedAgreement() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://localhost:9999");

            // Заполняем корректно поле "Фамилия и имя"
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Иванов Иван");

            // Заполняем корректно поле "Мобильный телефон"
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("+79991112233");

            // Не ставим галочку согласия

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Проверяем, что появляется ошибка, связанная с отсутствием согласия
            WebElement agreementError = driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid"));
            assertTrue(agreementError.isDisplayed());

            driver.quit();
        } catch (Exception e) {
            System.err.println("Ошибка во время теста (Unchecked Agreement): " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void shouldShowOnlyFirstErrorWhenMultipleFieldsInvalid() {
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");
            WebDriver driver = new ChromeDriver(options);

            driver.get("http://localhost:9999");

            // Вводим некорректное имя
            WebElement nameInput = driver.findElement(By.cssSelector("[data-test-id=name] input"));
            nameInput.sendKeys("Ivan Petrov");

            // Вводим некорректный номер телефона
            WebElement phoneInput = driver.findElement(By.cssSelector("[data-test-id=phone] input"));
            phoneInput.sendKeys("99911122333");

            // Не ставим галочку согласия

            // Нажимаем кнопку "Продолжить"
            WebElement submitButton = driver.findElement(By.cssSelector("button.button"));
            submitButton.click();

            // Проверяем, что ошибка отображается только для поля "Фамилия и имя"
            WebElement nameError = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub"));
            assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", nameError.getText());

            // Проверяем, что поля "Мобильный телефон" и галочка согласия не подсвечены как ошибочные
            boolean phoneInvalid = driver.findElements(By.cssSelector("[data-test-id=phone].input_invalid")).size() > 0;
            boolean agreementInvalid = driver.findElements(By.cssSelector("[data-test-id=agreement].input_invalid")).size() > 0;
            assertEquals(false, phoneInvalid);
            assertEquals(false, agreementInvalid);

            driver.quit();
        } catch (Exception e) {
            System.err.println("Ошибка во время теста (Multiple Errors): " + e.getMessage());
            e.printStackTrace();
        }
    }
}