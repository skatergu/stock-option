package com.aj.options.controller;

import com.aj.options.entity.Option;
import com.aj.options.service.OptionService;
import com.aj.options.service.model.ModelType;
import com.aj.options.service.model.PriceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/option")
public class OptionController {

    @Autowired
    OptionService optionService;

    @GetMapping("/expiry/{ulSymbol}")
    public List<Long> getExpirys(@PathVariable("ulSymbol") String ulSymbol) throws IOException {
        return optionService.getExpirys(ulSymbol);
    }

    @GetMapping("/strike/{ulSymbol}/{expiry}")
    public List<Double> getStrikes(@PathVariable("ulSymbol") String ulSymbol, @PathVariable("expiry") String expiry) throws IOException {
        return optionService.getStrikes(ulSymbol, Long.valueOf(expiry));
    }

    @GetMapping("/models")
    public List<String> getModelTypes() {
        return optionService.getModelTypes();
    }

    @GetMapping("/getOptions")
    public List<Option> getOptions(@RequestParam String ulSymbol, @RequestParam String expiry, @RequestParam String optionType,
                                   @RequestParam String priceType, @RequestParam String model, @RequestParam String isInTheMoney) throws IOException {
        ModelType modelType = ModelType.valueOf(model);
        PriceType pxType = PriceType.valueOf(priceType);
        boolean itm = Boolean.valueOf(isInTheMoney);
        return optionService.getOptions(ulSymbol, Long.valueOf(expiry), optionType, pxType, modelType, itm);
    }

    @GetMapping("/getOption")
    public Option getOption (@RequestParam String ulSymbol, @RequestParam String expiry,
                             @RequestParam double strike, @RequestParam String optionType) throws IOException {
        return optionService.getOption(ulSymbol, Long.valueOf(expiry), strike, optionType);
    }

}

