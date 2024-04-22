package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class App {

    public static void main(String[] args) {

        // Calling the fetching

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode areas = readAreaDataFromTheAPIURL(objectMapper);
        HashMap<String, String> municipalityNamesToCodesMap = createMunicipalityNamesToCodesMap(areas);

        ObjectMapper objectMapperEmp = new ObjectMapper();
        JsonNode areasEmp = readAreaDataFromTheAPIURLEmp(objectMapperEmp);
        HashMap<String, String> municipalityNamesToCodesMapEmp = createMunicipalityNamesToCodesMap(areasEmp);

        ObjectMapper objectMapperSelf = new ObjectMapper();
        JsonNode areasSelf = readAreaDataFromTheAPIURSelf(objectMapperSelf);
        HashMap<String, String> municipalityNamesToCodesMapSelf = createMunicipalityNamesToCodesMap(areasSelf);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Fetch population data for a municipality");
            System.out.println("2. Fetch self sufficiency data for a municipality");
            System.out.println("3. Fetch employment rate data for a municipality");
            System.out.println("4. Fetch weather data for a municipality");
            System.out.println("5. Play quiz about Finland");
            System.out.println("6. Mayor funds allocation");
            System.out.println("7. Compare 2 municipalities");
            System.out.println("8. Covid stats by week");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    fetchPopulationData(objectMapper, municipalityNamesToCodesMap, sc);
                    break;
                case 2:
                    CSVReader.searchCSV("SelfSuffFetch.csv");
                    //fetchPopulationDataSelf(objectMapperSelf, municipalityNamesToCodesMapSelf, sc); - would love to work, but i suck
                    break;
                case 3:
                    CSVReader.searchCSV("EmploymentRate.csv");
                    //fetchPopulationDataEmp(objectMapperEmp, municipalityNamesToCodesMapEmp, sc); - would love to work, but i suck
                    break;
                case 4:
                    CSVReader.searchCSVMore("Weather observation.csv");
                    break;
                case 5:
                    System.out.print("\nEnter the municipality name for the quiz: ");
                    String municipalityForQuiz = sc.nextLine();
                    Quiz quiz = new Quiz();
                    quiz.buildQuiz(municipalityForQuiz, sc);
                    break;
                case 6:
                    System.out.print("\nEnter your municipality name: ");
                    String municipality = sc.nextLine();
                    Allocation.allocateFunds(sc, municipality);
                    break;
                case 7:
                    System.out.println("\nConparison 1: ");
                    fetchPopulationData(objectMapper, municipalityNamesToCodesMap, sc);
                    fetchPopulationData(objectMapper, municipalityNamesToCodesMap, sc);
                    System.out.println("\nConparison 2: ");
                    CSVReader.searchCSV("SelfSuffFetch.csv");
                    CSVReader.searchCSV("SelfSuffFetch.csv");
                    System.out.println("\nConparison 3: ");
                    CSVReader.searchCSV("EmploymentRate.csv");
                    CSVReader.searchCSV("EmploymentRate.csv");
                    break;
                case 8:
                    CSVReader.readCSV("covid.csv");
                    break;
                case 9:
                    System.out.println("Exiting the program...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a numer from 1 to 9.");
            }
        }
    }


    

//--------------------------Employment Rate

public static void fetchAndPrintDataEmp(String municipalityEmp) {
    try {
        String urlStrEmp = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        URL urlEmp = new URL(urlStrEmp);
        HttpURLConnection connEmp = (HttpURLConnection) urlEmp.openConnection();
        connEmp.setRequestMethod("GET");
        int responseCodeEmp = connEmp.getResponseCode();

        if (responseCodeEmp == HttpURLConnection.HTTP_OK) {
            BufferedReader inEmp = new BufferedReader(new InputStreamReader(connEmp.getInputStream()));
            String inputLineEmp;
            StringBuilder responseEmp = new StringBuilder();

            while ((inputLineEmp = inEmp.readLine()) != null) {
                responseEmp.append(inputLineEmp);
            }
            inEmp.close();

            System.out.println("Data for " + municipalityEmp + ":");
            System.out.println(responseEmp.toString());
        } else {
            System.out.println("Failed to fetch data. Response Code: " + responseCodeEmp);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private static void fetchPopulationDataEmp(ObjectMapper objectMapperEmp, HashMap<String, String> municipalityNamesToCodesMapEmp, Scanner sc) {
    String municipalityNameEmp;
    String codeEmp;

    System.out.print("Enter municipality name (q for returning to the menu): ");
    municipalityNameEmp = sc.nextLine();

    if (municipalityNameEmp.equals("q")) {
        return;
    }

    codeEmp = municipalityNamesToCodesMapEmp.get(municipalityNameEmp);

    if (codeEmp == null) {
        System.out.println("Municipality not found");
        return;
    }

    try {
        JsonNode jsonQueryEmp = objectMapperEmp.readTree(new File("aqueryEmpa.json"));
        ((ObjectNode) jsonQueryEmp.findValue("query").get(0).get("selection")).putArray("values").add(codeEmp);
        HttpURLConnection conEmp = connectToAPIAndSendPostRequestEmp(objectMapperEmp, jsonQueryEmp);

        try (BufferedReader brEmp = new BufferedReader(new InputStreamReader(conEmp.getInputStream(), "utf-8"))) {
            StringBuilder responseEmp = new StringBuilder();
            String responseLineEmp;
            while ((responseLineEmp = brEmp.readLine()) != null) {
                responseEmp.append(responseLineEmp.trim());
            }

            JsonNode municipalityDataEmp = objectMapperEmp.readTree(responseEmp.toString());
            ArrayList<String> yearsEmp = new ArrayList<>();
            JsonNode populationsEmp = municipalityDataEmp.get("value");
            
            for (JsonNode nodeEmp : municipalityDataEmp.get("dimension").get("Alue").get("category").get("label")) {
                yearsEmp.add(nodeEmp.asText());
            }

            ArrayList<EmploymentData> populationDataEmp = new ArrayList<>();

            for (int i = 0; i < populationsEmp.size(); i++) {
                Double populationEmp = populationsEmp.get(i).asDouble();
                populationDataEmp.add(new EmploymentData(Double.parseDouble(yearsEmp.get(i)), populationEmp));
            }

            System.out.println(municipalityNameEmp);
            System.out.println("==========================");

            for (EmploymentData dataEmp : populationDataEmp) {
                System.out.print(dataEmp.getYear() + ": " + dataEmp.getEmploymentRate() + " ");

                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static HttpURLConnection connectToAPIAndSendPostRequestEmp(ObjectMapper objectMapperEmp, JsonNode jsonQueryEmp)
        throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
    URL urlEmp = new URL ("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px");

    HttpURLConnection conEmp = (HttpURLConnection)urlEmp.openConnection();

    conEmp.setRequestMethod("POST");
    conEmp.setRequestProperty("Content-Type", "application/json; utf-8");
    conEmp.setRequestProperty("Accept", "application/json");
    conEmp.setDoOutput(true);

    try(OutputStream osEmp = conEmp.getOutputStream()) {
        byte[] input = objectMapperEmp.writeValueAsBytes(jsonQueryEmp);
        osEmp.write(input, 0, input.length);			
    }
    return conEmp;
}

private static HashMap<String, String> createMunicipalityNamesToCodesMapEmp(JsonNode areasEmp) {
    JsonNode codesEmp = null;
    JsonNode namesEmp = null;

    for (JsonNode nodeEmp : areasEmp.findValue("variables")) {
        if (nodeEmp.findValue("text").asText().equals("Area")) {
            codesEmp = nodeEmp.findValue("values");
            namesEmp = nodeEmp.findValue("valueTexts");
            break;
        }
    }

    if (codesEmp == null || namesEmp == null) {
        System.out.println("Failed to fetch municipality data.");
        return new HashMap<>();
    }

    HashMap<String, String> municipalityNamesToCodesMapEmp = new HashMap<>();

    for (int i = 0; i < namesEmp.size(); i++) {
        String nameEmp = namesEmp.get(i).asText();
        String codeEmp = codesEmp.get(i).asText();
        municipalityNamesToCodesMapEmp.put(nameEmp, codeEmp);
    }
    return municipalityNamesToCodesMapEmp;
}



private static JsonNode readAreaDataFromTheAPIURLEmp(ObjectMapper objectMapperEmp) {
    JsonNode areasEmp = null;
    try {
        areasEmp = objectMapperEmp.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px"));
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return areasEmp;
}

//-------------------------------Self sufficiency
    
public static void fetchAndPrintDataSelf(String municipalitySelf) {
    try {
        String urlStrSelf = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
        URL urlSelf = new URL(urlStrSelf);
        HttpURLConnection connSelf = (HttpURLConnection) urlSelf.openConnection();
        connSelf.setRequestMethod("GET");
        int responseCodeSelf = connSelf.getResponseCode();

        if (responseCodeSelf == HttpURLConnection.HTTP_OK) {
            BufferedReader inSelf = new BufferedReader(new InputStreamReader(connSelf.getInputStream()));
            String inputLineSelf;
            StringBuilder responseSelf = new StringBuilder();

            while ((inputLineSelf = inSelf.readLine()) != null) {
                responseSelf.append(inputLineSelf);
            }
            inSelf.close();

            System.out.println("Data for " + municipalitySelf + ":");
            System.out.println(responseSelf.toString());
        } else {
            System.out.println("Failed to fetch data. Response Code: " + responseCodeSelf);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private static void fetchPopulationDataSelf(ObjectMapper objectMapperSelf, HashMap<String, String> municipalityNamesToCodesMapSelf, Scanner sc) {
    String municipalityNameSelf;
    String codeSelf;

    System.out.print("Enter municipality name (q for returning to the menu): ");
    municipalityNameSelf = sc.nextLine();

    if (municipalityNameSelf.equals("q")) {
        return;
    }

    codeSelf = municipalityNamesToCodesMapSelf.get(municipalityNameSelf);

    if (codeSelf == null) {
        System.out.println("Municipality not found");
        return;
    }

    try {
        JsonNode jsonQuerySelf = objectMapperSelf.readTree(new File("querySelf.json"));
        ((ObjectNode) jsonQuerySelf.findValue("query").get(0).get("selection")).putArray("values").add(codeSelf);
        HttpURLConnection conSelf = connectToAPIAndSendPostRequest(objectMapperSelf, jsonQuerySelf);

        try (BufferedReader brSelf = new BufferedReader(new InputStreamReader(conSelf.getInputStream(), "utf-8"))) {
            StringBuilder responseSelf = new StringBuilder();
            String responseLineSelf;
            while ((responseLineSelf = brSelf.readLine()) != null) {
                responseSelf.append(responseLineSelf.trim());
            }

            JsonNode municipalityDataSelf = objectMapperSelf.readTree(responseSelf.toString());
            ArrayList<String> yearsSelf = new ArrayList<>();
            JsonNode populationsSelf = municipalityDataSelf.get("value");
            
            for (JsonNode nodeSelf : municipalityDataSelf.get("dimension").get("Vuosi").get("category").get("label")) {
                yearsSelf.add(nodeSelf.asText());
            }

            ArrayList<SelfSufficiencyData> populationDataSelf = new ArrayList<>();

            for (int i = 0; i < populationsSelf.size(); i++) {
                Double populationSelf = populationsSelf.get(i).asDouble();
                populationDataSelf.add(new SelfSufficiencyData());
            }

            System.out.println(municipalityNameSelf);
            System.out.println("==========================");

            for (SelfSufficiencyData dataSelf : populationDataSelf) {
                System.out.print(dataSelf.getCity() + ": " + dataSelf.getPercentage() + " ");

                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private static HttpURLConnection connectToAPIAndSendPostRequestSelf(ObjectMapper objectMapperSelf, JsonNode jsonQuerySelf)
        throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
    URL urlSelf = new URL ("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px");

    HttpURLConnection conSelf = (HttpURLConnection)urlSelf.openConnection();

    conSelf.setRequestMethod("POST");
    conSelf.setRequestProperty("Content-Type", "application/json; utf-8");
    conSelf.setRequestProperty("Accept", "application/json");
    conSelf.setDoOutput(true);

    try(OutputStream osSelf = conSelf.getOutputStream()) {
        byte[] input = objectMapperSelf.writeValueAsBytes(jsonQuerySelf);
        osSelf.write(input, 0, input.length);			
    }
    return conSelf;
}

private static HashMap<String, String> createMunicipalityNamesToCodesMapSelf(JsonNode areasSelf) {
    JsonNode codesSelf = null;
    JsonNode namesSelf = null;

    for (JsonNode nodeSelf :areasSelf.findValue("variables")) {
        if (nodeSelf.findValue("text").asText().equals("Area")) {
            codesSelf =  nodeSelf.findValue("values");
            namesSelf = nodeSelf.findValue("valueTexts");
        }
    }

    HashMap<String, String> municipalityNamesToCodesMap = new HashMap<>();

    for (int i = 0; i < namesSelf.size(); i++) {
        String nameSelf = namesSelf.get(i).asText();
        String codeSelf = codesSelf.get(i).asText();
        municipalityNamesToCodesMap.put(nameSelf, codeSelf);

    }
    return municipalityNamesToCodesMap;
}



private static JsonNode readAreaDataFromTheAPIURSelf(ObjectMapper objectMapper) {
    JsonNode areasSelf = null;
    try {
        areasSelf = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px"));
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return areasSelf;
}

//----------------------------------------Population----------------------------------

    public static void fetchAndPrintData(String municipality) {
        try {
            String urlStr = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/inn/statfin_inn_pxt_12kp.px";
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println("Data for " + municipality + ":");
                System.out.println(response.toString());
            } else {
                System.out.println("Failed to fetch data. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fetchPopulationData(ObjectMapper objectMapper, HashMap<String, String> municipalityNamesToCodesMap, Scanner sc) {
        String municipalityName;
        String code;

        System.out.print("\nEnter municipality name (q for returning to the menu): ");
        municipalityName = sc.nextLine();

        if (municipalityName.equals("q")) {
            return;
        }

        code = municipalityNamesToCodesMap.get(municipalityName);

        if (code == null) {
            System.out.println("\nMunicipality not found");
            return;
        }

        try {
            JsonNode jsonQuery = objectMapper.readTree(new File("query.json"));
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);
            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode municipalityData = objectMapper.readTree(response.toString());
                ArrayList<String> years = new ArrayList<>();
                JsonNode populations = municipalityData.get("value");
                
                for (JsonNode node : municipalityData.get("dimension").get("Vuosi").get("category").get("label")) {
                    years.add(node.asText());
                }

                ArrayList<MunicipalityData> populationData = new ArrayList<>();

                for (int i = 0; i < populations.size(); i++) {
                    Integer population = populations.get(i).asInt();
                    populationData.add(new MunicipalityData(Integer.parseInt(years.get(i)), population));
                }

                System.out.println("\n"+municipalityName);
                System.out.println("==========================");

                for (MunicipalityData data : populationData) {
                    System.out.print(data.getYear() + ": " + data.getPopulation() + " ");

                    System.out.println();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {
        URL url = new URL ("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px");

        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);			
        }
        return con;
    }

    private static HashMap<String, String> createMunicipalityNamesToCodesMap(JsonNode areas) {
        JsonNode codes = null;
        JsonNode names = null;

        for (JsonNode node :areas.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                codes =  node.findValue("values");
                names = node.findValue("valueTexts");
            }
        }

        HashMap<String, String> municipalityNamesToCodesMap = new HashMap<>();

        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            municipalityNamesToCodesMap.put(name, code);

        }
        return municipalityNamesToCodesMap;
    }

    

    private static JsonNode readAreaDataFromTheAPIURL(ObjectMapper objectMapper) {
        JsonNode areas = null;
        try {
            areas = objectMapper.readTree(new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return areas;
    }
}
