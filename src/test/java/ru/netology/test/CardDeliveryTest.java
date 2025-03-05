package ru.netology.test;

import java.time.Duration;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;

import org.openqa.selenium.Keys;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Управление порядком тестов
public class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        Configuration.headless = true;
    }

    @Test
    @Order(1) // Запускаем этот тест первым
    void shouldSendFormWithValidData() {
        open("http://localhost:9999");

        $("[data-test-id=city] input").setValue("Казань");

        String planningDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        $("[data-test-id=date] input").click();
        $("[data-test-id=date] input").sendKeys(Keys.END);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=date] input").setValue(planningDate);

        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79991112233");
        $("[data-test-id=agreement]").click();

        $$(".button").findBy(exactText("Забронировать")).click();

        $("[data-test-id=notification] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно"));

        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + planningDate));
    }

    @Test
    @Order(2) // Запускаем этот тест вторым
    void shouldSendFormWithComplexElements() {
        open("http://localhost:9999");

        // Выбор города через выпадающий список
        $("[data-test-id=city] input").setValue("Ка");
        $$(".menu-item").findBy(text("Казань")).click();

        // Выбор даты через календарь
        $("[data-test-id=date] input").click();
        $$(".calendar__day").findBy(text(String.valueOf(LocalDate.now().plusDays(7).getDayOfMonth()))).click();

        $("[data-test-id=name] input").setValue("Иван Петров");
        $("[data-test-id=phone] input").setValue("+79991112233");
        $("[data-test-id=agreement]").click();

        $$(".button").findBy(exactText("Забронировать")).click();

        $("[data-test-id=notification] .notification__title")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Успешно"));

        $("[data-test-id=notification] .notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(text("Встреча успешно забронирована на " + 
                        LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}