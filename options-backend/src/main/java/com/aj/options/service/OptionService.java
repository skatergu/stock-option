package com.aj.options.service;

import com.aj.options.entity.Option;
import com.aj.options.entity.OptionChain;
import com.aj.options.entity.Stock;
import com.aj.options.service.model.ModelService;
import com.aj.options.service.model.ModelType;
import com.aj.options.service.model.PriceType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OptionService {
    private final String yahooFinance = "https://query2.finance.yahoo.com/v7/finance/options/";

    @Autowired
    private ModelService modelService;

    public OptionService() {}

    public List<Long> getExpirys(String ulSymbol) throws IOException {
        String url = this.yahooFinance + ulSymbol;
        JSONObject optionJson = this.loadResultJson(url);
        JSONArray expiryJson = optionJson.getJSONArray("expirationDates");

        ArrayList<Long> expirations = new ArrayList<Long>();

        for (int i=0; i<expiryJson.length(); i++) {
            long longDate = expiryJson.getLong(i);
            expirations.add(longDate);
        }

        return expirations;
    }

    public List<Double> getStrikes(String ulSymbol, long expiry) throws IOException {
        String url = this.yahooFinance + ulSymbol + "?date=" + expiry;
        JSONObject optionJson = this.loadResultJson(url);
        JSONArray strikeJson = optionJson.getJSONArray("strikes");

        ArrayList<Double> strikeList = new ArrayList<Double>();

        for (int i=0; i<strikeJson.length(); i++) {
            double strike = strikeJson.getDouble(i);
            strikeList.add(strike);
        }

        return strikeList;
    }

    public List<Option> getOptions(String ulSymbol, long expiry, String type, PriceType priceType, ModelType model) throws IOException {
        String url = this.yahooFinance + ulSymbol + "?date=" + expiry;
        JSONObject resultJson = this.loadResultJson(url);
        OptionChain optionChain = parseOptionChain(resultJson, priceType);

        List<Option> options = optionChain.getAllOptions().stream()
                                   .filter((option -> option.getExpirationDate()==expiry && option.getType().equalsIgnoreCase(type) && Math.abs(option.getPrice()) > 0.01 ))
                                   .collect(Collectors.toList());

        modelService.calcModelData(options, model);
        return options;
    }

    public List<String> getModelTypes() {
        return Stream.of(ModelType.values()).map(Enum::name).collect(Collectors.toList());
    }

    public Option getOption(String ulSymbol, long expiry, double strike, String optionType) throws IOException {
        String url = this.yahooFinance + ulSymbol + "?date=" + expiry;
        PriceType priceType = PriceType.last;

        JSONObject resultJson = this.loadResultJson(url);
        OptionChain optionChain = parseOptionChain(resultJson, priceType);

        Option result = optionChain.getAllOptions().stream()
                                 .filter(option -> ( option.getExpirationDate() == expiry &&
                                                     MathUtils.compareDouble(option.getStrike(), strike) == 0 &&
                                                     option.getType().equals(optionType)))
                                 .findFirst()
                                 .get();
        return result;
    }


    private OptionChain parseOptionChain(JSONObject resultJson, PriceType priceType) {
        OptionChain optionChain = new OptionChain();

        Stock ulStock = parseUlStock(resultJson.getJSONObject("quote"));
        optionChain.setUlStock(ulStock);

        ArrayList<Option> options = parseOptions(resultJson.getJSONArray("options"), priceType, ulStock.getPrice());
        optionChain.setAllOptions(options);

        return optionChain;
    }

    private Stock parseUlStock(JSONObject ulQuoteJson) {
        Stock stock = new Stock();
        stock.setSymbol(ulQuoteJson.getString("symbol"));
        stock.setPrice(ulQuoteJson.getDouble("regularMarketPrice"));
        return stock;
    }

    private ArrayList<Option> parseOptions(JSONArray allOptionsJson, PriceType priceType, double ulPx) {
        ArrayList<Option> options = new ArrayList<>();
        for (int i=0; i<allOptionsJson.length(); i++) {
            JSONObject expiryOptionJson = allOptionsJson.getJSONObject(i);
            JSONArray callOptionsJSON = expiryOptionJson.getJSONArray("calls");
            JSONArray putOptionsJSON = expiryOptionJson.getJSONArray("puts");
            ArrayList<Option> callOptions = parseTypeOptions(callOptionsJSON, "call", priceType, ulPx);
            ArrayList<Option> putOptions = parseTypeOptions(putOptionsJSON, "put", priceType, ulPx);
            options.addAll(callOptions);
            options.addAll(putOptions);
        }
        return options;
    }

    private ArrayList<Option> parseTypeOptions(JSONArray optionsJson, String optionType, PriceType priceType, double ulPx) {
        ArrayList<Option> options = new ArrayList<>();
        for (int i=0; i<optionsJson.length(); i++) {
            try {
                JSONObject optJson = optionsJson.getJSONObject(i);
                Option option = new Option();
                option.setType(optionType);
                option.setSymbol(optJson.getString("contractSymbol"));
                option.setStrike(optJson.getInt("strike"));
                option.setBid(optJson.getDouble("bid"));
                option.setAsk(optJson.getDouble("ask"));
                option.setModelValue((option.getAsk() + option.getBid()) / 3.0);
                option.setExpirationDate(optJson.getLong("expiration"));
                option.setIv(optJson.getDouble("impliedVolatility"));
                option.setItm(optJson.getBoolean("inTheMoney"));
                option.setLastPrice(optJson.getDouble("lastPrice"));
                switch (priceType) {
                    case ask -> option.setPrice(option.getAsk());
                    case bid -> option.setPrice(option.getBid());
                    case mid -> option.setPrice((option.getAsk() + option.getBid()) / 2.0);
                    case last -> option.setPrice(option.getLastPrice());
                    default -> option.setPrice(option.getLastPrice());
                }

                option.setUlPrice(ulPx);
                if (option.isItm())
                    options.add(option);
            }
            catch (Exception e) {
                System.err.println(e.toString());
            }
        }
        return options;
    }

    private JSONObject loadResultJson(String url) throws IOException {
        URL optUrl = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(optUrl.openStream()));

        StringBuilder sb = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            sb.append(inputLine);
        }
        JSONObject optionJson = new JSONObject(sb.toString());
        JSONObject resultJson = optionJson.getJSONObject("optionChain").getJSONArray("result").getJSONObject(0);

        return resultJson;
    }

}
