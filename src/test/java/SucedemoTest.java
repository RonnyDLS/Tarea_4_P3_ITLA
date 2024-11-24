import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SucedemoTest {
    static WebDriver driver;
    static ExtentReports extent;
    static ExtentTest testLogin;
    static ExtentTest testProductos;
    static ExtentTest testProductoDescripcion;
    static ExtentTest testAddCarrito;
    static ExtentTest testVerCarritoCompra;
    static ExtentTest testDireccion;
    static ExtentTest testDetalleCompra;
    static ExtentTest testCompraFinalizada;
    static ExtentTest testBtnMenu;
    static ExtentTest testBtnSalir;

    @BeforeAll
    public static void config(){
        System.setProperty("webdriver.chrome.driver","src/main/resources/chromedriver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        // Configuración del reporte de ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("reporte-Sucedemo.html");  // Especificamos la ruta del reporte
        sparkReporter.config().setDocumentTitle("Test Reporte - Saucedemo");  // Título del reporte
        sparkReporter.config().setReportName("Tests Saucedemo");  // Nombre del reporte

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);  // Se adjunta el reporter de tipo Spark
    }

    @Test
    public void TestSucedemo(){

        driver.get("https://www.saucedemo.com/v1/index.html");

        // CREAR TEST - REPORTE
        testLogin = extent.createTest("Login Test", "Prueba de inicio de sesión");
        try {
            // NOMBRE DE USUARIO
            WebElement user = driver.findElement(By.id("user-name"));
            user.sendKeys("standard_user");
            testLogin.info("Nombre de usuario ingresado");

            // CONTRASEÑA DEL USUARIO
            WebElement password = driver.findElement(By.id("password"));
            password.sendKeys("secret_sauce");
            testLogin.info("Contraseña ingresada");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "login";
            capturaPantalla(nombre);
            testLogin.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");

            // HACER CLIC EN EL BOTON LOGIN
            WebElement btnLogin = driver.findElement(By.id("login-button"));
            btnLogin.click();
            testLogin.info("Botón de login clickeado");

            // VERIFICAR SI EL USUARIO INICIO SECCION
            WebElement txtProducts = driver.findElement(By.xpath("//*[@id=\"inventory_filter_container\"]/div"));
            assertEquals("Products",txtProducts.getText(),"El usuario no pudo iniciar sesión");
            testLogin.pass("Login exitoso, productos visibles");

        } catch (Exception e) {
            testLogin.fail("La prueba del login fallo, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - PRODUCTOS
        testProductos = extent.createTest("Productos Test", "Prueba de visibilidad de productos");
        try {
            // CREAR CAPTURA DE PANTALLA
            String nombre = "productos";
            capturaPantalla(nombre);
            testProductos.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
            testProductos.info("Productos visibles");
        } catch (Exception e) {
            testProductos.fail("Prueba visibilidad de los productos, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - PRODUCTOS DESCRIPCION
        testProductoDescripcion = extent.createTest("Productos Descripción Test", "Prueba de visibilidad descripción de producto");
        try {
            // LISTA DE LOS PRODUCTOS
            List<WebElement> listaArticulos = driver.findElements(By.xpath("/html/body/div/div[2]/div[2]/div/div[2]/div/div"));
            WebElement producto = null;
            for(WebElement element : listaArticulos ){
                if (element.getText().contains("Sauce Labs Backpack")){
                    producto = driver.findElement(By.xpath("/html/body/div/div[2]/div[2]/div/div[2]/div/div/div[2]/a/div"));
                }
            }
            producto.click();
            testProductoDescripcion.info("Descripción del producto visible");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "decripciónProducto";
            capturaPantalla(nombre);
            testProductoDescripcion.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
        } catch (Exception e) {
            testProductoDescripcion.fail("Prueba visibilidad de descripción de productos, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - ADD PRODUCTOS AL CARRITO
        testAddCarrito = extent.createTest("Productos Descripción Test", "Prueba de visibilidad descripción de producto");
        try {
            // AGREGAR PRODUCTO AL CARRITO
            WebElement addCarrito = driver.findElement(By.xpath("//*[@id=\"inventory_item_container\"]/div/div/div/button"));
            addCarrito.click();
            testAddCarrito.info("Clickear boton add carrito");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "productoCarrito";
            capturaPantalla(nombre);
            testAddCarrito.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
        } catch (Exception e) {
            testAddCarrito.fail("Prueba agregar producto al carrito, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - VER CARRITO DE COMPRA
        testVerCarritoCompra = extent.createTest("Carrito De Compra Test", "Ver los articulos del carrito de compra");
        try {
            // VER CARRITO DE COMPRA
            WebElement carrito = driver.findElement(By.id("shopping_cart_container"));
            carrito.click();
            testVerCarritoCompra.info("Clickear boton carrito de compra");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "carritoCompra";
            capturaPantalla(nombre);
            testVerCarritoCompra.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
        } catch (Exception e) {
            testVerCarritoCompra.fail("Prueba ver carrito de compra, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - ADD DIRECCION
        testDireccion = extent.createTest("Dirección Test", "Agregar la dircción del usuario");
        try {
            // AGREGAR DIRECCION
            WebElement direccion = driver.findElement(By.xpath("//*[@id=\"cart_contents_container\"]/div/div[2]/a[2]"));
            direccion.click();
            testDireccion.info("Clickear botón checkout");

            // AGREGAR DIRECCION - NOMBRE
            WebElement pNombre = driver.findElement(By.id("first-name"));
            pNombre.sendKeys("Ronny");
            testDireccion.info("Ingresar nombre");

            // AGREGAR DIRECCION - APELLIDO
            WebElement sApellido = driver.findElement(By.id("last-name"));
            sApellido.sendKeys("De Los Santos");
            testDireccion.info("Ingresar apellido");

            // AGREGAR DIRECCION - CODIGO POSTAL
            WebElement zipPostalCode = driver.findElement(By.id("postal-code"));
            zipPostalCode.sendKeys("487487");
            testDireccion.info("Ingresar codigo postal");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "direcciónEntrega";
            capturaPantalla(nombre);
            testDireccion.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");

            // AGREGAR DIRECCION - BTN CONTINUAR
            WebElement btnCont = driver.findElement(By.xpath("//*[@id=\"checkout_info_container\"]/div/form/div[2]/input"));
            btnCont.click();
            testDireccion.info("Clicker boton continuar");
        } catch (Exception e) {
            testDireccion.fail("Prueba dirección, error: "+e.getMessage());
        }

        // ------------------------------------------

        // TIEMPO
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // ------------------------------------------

        // CREAR TEST - DETALLE COMPRA
        testDetalleCompra = extent.createTest("Detalle Compra Test", "Mostrar el detalle de la compra");
        try {
            // CREAR CAPTURA DE PANTALLA
            String nombre = "detalleCompra";
            capturaPantalla(nombre);
            testDetalleCompra.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");

            // FINALIZAR COMPRA
            WebElement fCompra = driver.findElement(By.xpath("//*[@id=\"checkout_summary_container\"]/div/div[2]/div[8]/a[2]"));
            fCompra.click();
            testDetalleCompra.info("Clicker boton finish");
        } catch (Exception e) {
            testDetalleCompra.fail("Prueba detalle compra, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - COMPRA FINALIZADA
        testCompraFinalizada = extent.createTest("Compra Finalizada Test", "Mostrar un mensaje de la compra finalizada");
        try {
            // COMPRA FINALIZADA
            WebElement cFinalizada = driver.findElement(By.xpath("//*[@id=\"checkout_complete_container\"]/h2"));
            assertEquals("THANK YOU FOR YOUR ORDER",cFinalizada.getText(),"Error al finalizar la compra");
            testCompraFinalizada.pass("Mensaje visible");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "compraFinalizada";
            capturaPantalla(nombre);
            testCompraFinalizada.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");

        } catch (Exception e) {
            testCompraFinalizada.fail("Prueba compra finalizada, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - BOTON MENU
        testBtnMenu = extent.createTest("Boton Menu Test", "Hacer clic en el boton menu");
        try {
            // BOTON MENU
            WebElement menu = driver.findElement(By.xpath("//*[@id=\"menu_button_container\"]/div/div[3]/div/button"));
            menu.click();
            testBtnMenu.info("Clickear botón menu");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "menu";
            capturaPantalla(nombre);
            testBtnMenu.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
        } catch (Exception e) {
            testBtnMenu.fail("Prueba compra finalizada, error: "+e.getMessage());
        }

        // ------------------------------------------

        // CREAR TEST - BOTON SALIR
        testBtnSalir = extent.createTest("Boton Salir Test", "Hacer clic en el boton para salir");
        try {
            // SALIR
            WebElement salir = driver.findElement(By.id("logout_sidebar_link"));
            salir.click();
            testBtnSalir.info("Clickear botón logout");

            // CREAR CAPTURA DE PANTALLA
            String nombre = "salirLogin";
            capturaPantalla(nombre);
            testBtnSalir.addScreenCaptureFromPath("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png");
        } catch (Exception e) {
            testBtnSalir.fail("Prueba botón salir, error: "+e.getMessage());
        }

    }

    @AfterAll
    public static void finish(){

        try {
            Thread.sleep(1000);
            driver.quit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // FINALIZAR EL REPORTE
        extent.flush();
    }

    // GET FECHA
    public String fecha(){
        DateFormat formatoFecha = new SimpleDateFormat("dd-MM-yy");
        Date fecha = new Date();
        return  formatoFecha.format(fecha);
    }

    // CREAR CAPTURA DE PANTALLA
    public void capturaPantalla(String nombre){
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(screenshot, new File("src/main/resources/capturasPantalla/Secedemo-" + nombre + "-" + fecha() + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
