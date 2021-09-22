package FirstTestCase;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.junit.TextReport;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Step;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class TestBase {
    WebDriver driver;
    private static final String PHONE_NUMBER_LOGIN = "+79969797537";
    private static final String TEST_NAME = "Test";
    private static final String TEST_EMAIL_ADRESS = "test@test.ru";
    private static final String TEST_PHONE_NUMBER_LOGIN = "+77777777777";
    private static final String ADRESS_STREET = "улица Говорова, Одинцово";
    private static final String ADRESS_HOME = "85";
    private static final String ORDER_COMMENT = "Тест. Не готовить";
    private static final String HOW_MONEY_TO_COURIER = "5000";
    private static final String HOW_MANY_USERS = "3";


    @Before
    public void setUp() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(true));
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("videoName", "PizzaLisaTestCaseForFirstSendOrder.mp4");
        capabilities.setCapability("name", "PizzaLisaTestCaseForFirstSendOrder");
        capabilities.setCapability("browserName", "chrome");
        capabilities.setCapability("browserVersion", "91.0");

        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true

        ));
        RemoteWebDriver driver = null;
        try {
            driver = new RemoteWebDriver(
                    new URL("http://192.168.1.17:8080/wd/hub"),
                    capabilities
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        WebDriverRunner.setWebDriver(driver);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @After
    public void end() {
        closeWebDriver();
    }

    @Rule
    public TextReport report = new TextReport();

    @Step("Открываю сайт")
    public void openURL() {
        open("https://pizzalisa.ru/");
    }

    @Step("Тыкаю на переход к главной странице")
    public void goMainPage (){
        $x("//div[contains(@class, \"h-left\")]/div[@class = \"logo\"]").click();
    }

    @Step("Перехожу в рандомный пункт меню для оформления тестового заказа")
    public void mathRandomHead() {
        List<SelenideElement> mathRandomHead = elements(By.xpath("//a[contains(@class, \"scroll-nav_link\") and not (@href = \"/menu/pizza-30-sm\") and not (@href = \"/menu/akciya\") and not (@href = \"/menu/napitki\") and not (@href = \"/menu/rolly\") and not (@href = \"/menu/sousy\") and not (@href = \"/menu/zakuski\") and not (@href = \"/menu/salaty\") and not (@href = \"/menu/deserti\")]"));
        int i = (int) (Math.random() * mathRandomHead.size());
        mathRandomHead.get(i).click();
    }

    @Step("Добавляем в корзину карточку товара")
    public void pickRandCards() {
        List<SelenideElement> clickRandomCards = elements(By.xpath("//button[contains(@class, \"add-to-basket\")]"));
        int i = (int) (Math.random() * clickRandomCards.size());
        clickRandomCards.get(i).scrollTo();
        clickRandomCards.get(i).click();


    }

    @Step("Переходим в корзину")
    public void goBasket() {
        $x("//a[contains(@class, \"btn-basket\")]").click();
    }

    @Step("Выбираем тип доставки самовывоз")
    public void selectDeliveryTypePickUp() {
        $(By.xpath("//label[@class = \"last\"]")).click();
    }


    @Step("Выбираем пункт самовывоза")
    public void selectTerminalForPickUp() {
        List<SelenideElement> options = elements(By.xpath("//select[@id = \"order_terminal-no-shipment\"]/option"));
        int i = (int) (Math.random() * options.size());
        options.get(i).click();
    }

    @Step("Заполняем поля")
    public void fillInFields() {
        $x("//input[@id = \"order_name\"]").scrollTo().setValue(TEST_NAME);
        $x("//input[@id = \"order_phone\"]").scrollTo().setValue(TEST_PHONE_NUMBER_LOGIN);
        $x("//input[@id = \"order_email\"]").scrollTo().setValue(TEST_EMAIL_ADRESS);
        $x("//input[@id = \"order_comment\"]").scrollTo().setValue(ORDER_COMMENT);
        $x("//input[@id = \"order_person-count\"]").scrollTo().setValue(HOW_MANY_USERS);
    }

    @Step("Выбираем способ оплаты (Налик)")
    public void selectPayType() {
        $x("//div[@class = \"payment-wrapper\"][1]").scrollTo().click();
        $("#change").setValue(HOW_MONEY_TO_COURIER);
    }
    
    @Step("Тыкаем на отправку заказа")
    public void sendOrder() {
        $x("//div[@class = \"item-cart-buttons\" ]/button[contains(@class, \"btn\")]").scrollTo().click();
    }

    @Step("Ждём перехода в статус принят")
    public void waitForComplete() {
        $x("//span[contains(text(),'Принят') or (contains(text(),'Поступил')) ]").shouldBe(visible);
    }


}


