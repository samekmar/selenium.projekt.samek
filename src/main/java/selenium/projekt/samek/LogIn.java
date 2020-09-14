package selenium.projekt.samek;
//--------------------------------------------------------------------------------------
// UJ - studia podyplomowe
// Automatyzacja testów
// praca zaliczeniowa
// Marek Samek
// data utworzenia :   09-09-2020
// dat aaktualizacji : 14-09-2020
// STATUS : w przygotowaniu
//--------------------------------------------------------------------------------------

import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class LogIn {

    public WebDriver driver;
    static Alert alert;


    @Test
    public void openMainWindow() throws InterruptedException, FileNotFoundException {

        //parametry logowania do instancji testowej STA
        String BASE_URL = "http://126.193.3.86/PzPWeb/";
        String BASE_LOGIN = "samekmarkat";
        String BASE_PASSWORD = "Test2020!";

        // lokalizacja dla logu zdarzeń scenariusza automatycznego
        PrintWriter ZapisLog = new PrintWriter("C:\\Users\\samekmar\\Desktop\\zapisLog.txt");

        // usatwienie raodzaju i lokalizacji WebDrivera
        System.setProperty("webdriver.ie.driver", "C:\\Users\\samekmar\\IdeaProjects\\SeleniumProjektSamek\\src\\main\\resources\\IEDriverServer.exe");
        // zainicjowanie obiektu opisującego dodatkowe własciwości obsługi sesji drivera IE
        DesiredCapabilities attributeIE = new DesiredCapabilities();
        // ustawienia atrybutu sprawdzenia powiększenia obrazu przeglądarki
        attributeIE.setCapability("ignoreZoomSetting", true);
        // ustawienia atrybutu dla zdarzenia nieobsłużonego alertem
        attributeIE.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.IGNORE);

        // utworzenie obiektu driver IE
        InternetExplorerDriver driver = new InternetExplorerDriver(attributeIE);
        // otwarcie okna przeglądarki przez driver
        driver.navigate().to(BASE_URL);
        // maksymalizacj aaktualnego okna obsługwanego przez driver
        driver.manage().window().maximize();

        // pobranie klucza uchwytu aktualnego okna obsługiwanego przez drivera
        String mainWindow = driver.getWindowHandle();
        // pobranie nazwy/tytułu aktualnego okna obsługowanego przez driver
        String mainWindowName = driver.getTitle();

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        System.out.println("1. dostępne okno logowania, nazwa okna - " + mainWindowName);
        ZapisLog.println("1. dostępne okno logowania, nazwa okna - " + mainWindowName);

        // wyszukanie elementów wymaganych do obsłużenia kroku logowania
        WebElement login = driver.findElement(By.id("login"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginSubmit = driver.findElement(By.id("login_submit"));

        // wypełnienie formularz logowania
        login.sendKeys(BASE_LOGIN);
        password.sendKeys(BASE_PASSWORD);

        // autoryzacja
        loginSubmit.click();

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        ZapisLog.println("2. uzupełnione dane logowania");
        System.out.println("2. uzupełnione dane logowania");

        //obsługa wyjątku związanego z brakiem autoryzacji
        try {
            Alert message = driver.switchTo().alert();
            if(message.getText().equals("Niepoprawny login lub hasło")) {
                System.out.println("Logowanie niemozliwe - błędne login lub hasło");
                ZapisLog.println("Logowanie niemozliwe - błędne login lub hasło");
                message.accept();
                driver.close();
                System.exit(0);
            }
        } catch(NoAlertPresentException noe) {
            System.out.println("3. autoryzacja");
            ZapisLog.println("3. autoryzacja");
        }

        // pobierz uchwyty do wszystkich otwartych okien drivera i wstaw do kolekcji
        Set<String> allWindow = driver.getWindowHandles();
        // wyznacz liczbę otwartych okien drivera
        int numberOfWindow = allWindow.size();

        // pokazuje liczbę aktywnych okien
        //System.out.println("Ilość otwartych okien : " + numberOfWindow);

        //znajdź i przełącz się na okno komunkatu informacyjnego ChM
        for(String myWindow: allWindow) {
            if(!myWindow.equals(mainWindow)) {
                driver.switchTo().window(myWindow);
            }
        }
        //wyslij komendę ENTER dla wyznaczonego okna komunikatu informacyjnego aby potwierdzić i zamknąć okna ChM
        driver.getKeyboard().sendKeys(Keys.ENTER);

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        System.out.println("4. zamknięcie okna informacyjnego ChM");
        ZapisLog.println("4. zamknięcie okna informacyjnego ChM");

        // poczekaj na odświeżenie okine (widoków)
        TimeUnit.SECONDS.sleep(25);

        // pobierz uchwyty do wszystkich otwartych okien drivera i wstaw do kolekcji
        Set<String> actualWindow = driver.getWindowHandles();

        //znajdź i przełącz sie na okno widoku głownego (dyspozytora)
        for(String newWindow: actualWindow) {
            if(!newWindow.equals(mainWindow)) {
                                driver.switchTo().window(newWindow);
            }
        }

        // zachowaj klucz uchwytu do okna głownego (widoku dyspozytora)
        String dispatcherWindow = driver.getWindowHandle();

        //maksymalizuj okno widoku Dyspozytora
        driver.manage().window().maximize();

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        System.out.println("5. powrót do okna Dyzposytora");
        ZapisLog.println("5. powrót do okna Dyzposytora");

        //----------------------------------------------------------------------------------------------------------
        // AKCJA :
        // - wprzejście do okna ekip, na poziom ekipy o nazwie MOBILE
        // - przejście do zlecenia o numerze 'zlecenie/
        // - uruchomienie js odpowiadającego za przejście do okna formatowani SMS
        // - wysłanie wiadomosci (treść wg szablonu)
        WebDriverWait waiting = new WebDriverWait(driver, 5);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String orderNumber = "KAW008/N/12998971/2020/1";
        String smsHandleWindow;

        // czekaj na uwidoczenie okna ekip oraz kolejki zleceń ekipy
        waiting.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'MOBILE')]"))).click();
        waiting.until((ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[(@colname='NRZ') and contains(text(), '"+orderNumber+"')]")))).click();

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        ZapisLog.println("6. przejście do okna ekip i wyszukanie zlecenia " + orderNumber);
        System.out.println("6. przejście do okna ekip i wyszukanie zlecenia " + orderNumber);

        // uruchom skrypt owierającuy okno edysji komunikatu SMS
        js.executeScript("send_sms();");
        Thread.sleep(2000);

        // znajdź i przełącz się na okno edycji waodmosci SMS
        for (String winHandle : driver.getWindowHandles()) {
            if (!mainWindow.contains(winHandle)) {
                driver.switchTo().window(winHandle);
            }
        }

        // maksymalizacja okna edycji wiadomosci SMS
        driver.manage().window().maximize();
        smsHandleWindow = driver.getWindowHandle();
        Thread.sleep(2000);

        // czekaj na uwidoczenie obiektu (przycisku wyslij) i uruchom 'wyślij'
        waiting.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='button_wyslij']"))).click();

        // wyświetlenie i uzupełnienie logu dla kroku/stanu scenariusza testowego
        ZapisLog.println("7. wysłanie standardowego SMS dla zlecenia " + orderNumber);
        System.out.println("7. wysłanie standardowego SMS dla zlecenia " + orderNumber);

        // czekaj na komunikat potwerdzający wysłanie SMSa
        waiting.until(ExpectedConditions.alertIsPresent());

        // przełącz się na komunikat informacyjny i oklikaj zamkniecie
        alert = driver.switchTo().alert();
        alert.accept();
        Thread.sleep(2000);

        //powrót do okna Dyspozytora
        driver.switchTo().window(dispatcherWindow);


        //----------------------------------------------------------------------------------------------------------
        // AKCJA :
        // - weryfikacja akcji wysłania komunnikatu SMS
        // - wywołanie połączenie do DB PzP
        // - uruchomienie zapytaniat
        // - określenie status i treści komunikatu SMS
        // dane do połączenie do bazy PZP


        //----------------------------------------------------------------------------------------------------------

        TimeUnit.SECONDS.sleep(10);
        driver.close();
        //driver.quit();

    }
    //-------------

    //-------------
}
//------------------------------------------------------------------------------------------------------------------