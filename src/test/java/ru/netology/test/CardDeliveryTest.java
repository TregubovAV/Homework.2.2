package ru.netology.test;

import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSendFormWithValidData() {
        $("[data-test-id=city] input").setValue("Казань");

        String planningDate = LocalDate.now().plusDays(3)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

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
    void shouldSendFormWithComplexElements() {
        $("[data-test-id=city] input").setValue("Ка");
        $$(".menu-item").findBy(text("Казань")).click();

        // Выбор даты через календарь с проверкой месяца
        $("[data-test-id=date] input").click();
        LocalDate plannedDate = LocalDate.now().plusDays(7);
        String day = String.valueOf(plannedDate.getDayOfMonth());
        String expectedMonth = plannedDate.format(DateTimeFormatter.ofPattern("MMMM"));

        // Проверяем, нужно ли переключать месяц
        String currentMonth = $(".calendar__name").text();
        if (!currentMonth.contains(expectedMonth)) {
            // Проверяем, можно ли кликнуть по стрелке
            if ($(".calendar__arrow").exists() && !$(".calendar__arrow").getAttribute("data-disabled").equals("true")) {
                $(".calendar__arrow").click();
            }
        }

        // Кликаем по нужному дню
        $$(".calendar__day").findBy(text(day)).click();

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
                        plannedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
    }
}