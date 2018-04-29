/*
This code takes up names of 200 cities from wikipedia
and using each city name sends a Http request to openwhether.org
and extracts the temperature, pressure and humidity of that city
obtained in the form of XML and saves the data in an excel sheet
*/
/*
The entire code implements principles of automation
*/
import java.net.*;
import java.io.*;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.*;

import java.io.FileOutputStream;
import org.apache.poi.xssf.usermodel.*;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class APIIntegration {

    public static void main(String[] args) throws Exception {
        String apiKey = "c63c2675a08570c577b84f521c449f0d";
        String url = "http://api.openweathermap.org/data/2.5/weather";
        String mode = "xml";
        String units = "metric";
        int row = 0;
//Creating excel workbook        
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet s = wb.createSheet("Sheet1");
        Set<String> keys = new HashSet<String>() {
            {
                add("city");
                add("temperature");
                add("pressure");
                add("humidity");
            }
        };
        // Creating header
        Iterator<String> itr = keys.iterator();
        int col = 0;
        XSSFRow r = s.createRow(row++);
        while (itr.hasNext()) {
            XSSFCell cell = r.createCell(col++);
            cell.setCellValue(itr.next());

        }
        List<String> paramNames = new ArrayList<String>() {
            {
                add("q");
                add("appid");
                add("mode");
                add("units");
            }
        };
        List<String> info = new ArrayList<String>() {
            {
                add("city");
                add("temperature");
                add("pressure");
                add("humidity");
            }
        };
        APIIntegration obj = new APIIntegration();
        //openig website
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://en.wikipedia.org/wiki/List_of_national_capitals_by_population");
        String c = "";
        int i = 0;
        while (row <= 200) {

            try {
                WebElement e = driver.findElement(By.xpath("//*[@id=\"mw-content-text\"]/div/table[2]/tbody/tr[" + Integer.toString(++i) + "]/td[3]/b/a"));
                String cityName = e.getText();
                c=cityName;
                //String cityName = "Kolkata";
                List<String> paramVals = new ArrayList<String>() {
                    {
                        add(cityName);
                        add(apiKey);
                        add(mode);
                        add(units);
                    }
                };
                String r1 = obj.apiRequest(url, paramNames, paramVals);
                Map<String, String> r2 = obj.xmlParser(r1, info);
                col = 0;
                itr = keys.iterator();
                r = s.createRow(row++);
                for (int j = 0; j < r2.size(); j++) {
                    XSSFCell cell = r.createCell(col++);
                    cell.setCellValue(r2.get(itr.next()));
                }
                FileOutputStream fos = new FileOutputStream("list.xlsx");
                wb.write(fos);
                fos.close();
            } catch (Exception e) {
                System.out.println(c);
                continue;
            }
        }
        driver.close();
    }

    public Map<String, String> xmlParser(String xmlData, List<String> info) throws Exception {
        //Xml parsing
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource in = new InputSource(new StringReader(xmlData));
        Document doc = builder.parse(in);
        Map<String, String> data = new LinkedHashMap<>();
        for (String s : info) {
            NodeList nodes = doc.getElementsByTagName(s);
            Node node = nodes.item(0);
            NamedNodeMap nodeMap = node.getAttributes();
            String key = "";
            String value = "";
            if (s == "city") {
                key = node.getNodeName();
                value = nodeMap.getNamedItem("name").getNodeValue();
            } else {
                key = node.getNodeName();
                value = nodeMap.getNamedItem("value").getNodeValue();
            }
            data.put(key, value);
        }
        return data;
    }

    public String apiRequest(String url, List<String> paramNames, List<String> paramVals) throws Exception {
        //Sending http request
        url += "?" + paramNames.get(0) + "=" + paramVals.get(0);
        for (int i = 1; i < paramNames.size(); i++) {
            url += "&" + paramNames.get(i) + "=" + paramVals.get(i);
        }
        //System.out.println(url);
        URL res = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) res.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response = "";
        while (br.readLine() != null) {
            response += br.readLine();
        }
        return response;
    }
}
