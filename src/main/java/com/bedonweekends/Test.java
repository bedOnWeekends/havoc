package com.bedonweekends;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        double S0 = 100.0;              // 초기 주가
        double mu = 2.0;              // 연간 드리프트 (평균 수익률)
        double sigma = 0.2  ;             // 연간 변동성
        int dayPerYear = 31 * 24 * 60 * 60;
        double dt = 1.0 / dayPerYear; // 시뮬레이션 간격
        int simulationDays = dayPerYear;

        double eventProbability = 0.01;
        double jumpVolatility = 0.1 / Math.sqrt(86400);

        Random random = new Random();
        double price = S0;
        String jsonPath = "C:/Users/dbals/Documents/json/";
        String fileName = "주침정보통신_";
        Stock stock = new Stock("주침정보통신", new HashMap<>());
        LocalDateTime now = LocalDateTime.now();
        for (int second = 0; second < simulationDays; second++) {
            LocalDateTime localDateTime = now.plusSeconds(second);
            String timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            double epsilon = random.nextGaussian();
            double gbmFactor = (mu - 0.5 * sigma * sigma) * dt + sigma * Math.sqrt(dt) * epsilon;
            double newPrice = price * Math.exp(gbmFactor);
            boolean jump = false;
            if (random.nextDouble() < eventProbability) {
                double jumpMagnitude = random.nextGaussian() * jumpVolatility;
                double jumpFactor = Math.exp(jumpMagnitude);
                newPrice *= jumpFactor;
                jump = true;
            }

            price = newPrice;
            StockPricePerTime stockPricePerTime = new StockPricePerTime(Math.round(price), jump);
            stock.getStockPricePerTimeMap().put(timestamp, stockPricePerTime);
            if (localDateTime.getHour() == 23 && localDateTime.getMinute() == 59 && localDateTime.getSecond() == 59) {
                Gson gson = new Gson();
                String json = gson.toJson(stock);
                if (json == null) {
                    continue;
                }
                if (!new File(jsonPath).exists()) {
                    new File(jsonPath).mkdir();
                }
                File file = new File(jsonPath + fileName + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".json");
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stock = new Stock("주침정보통신", new HashMap<>());
            }
        }


    }
}

