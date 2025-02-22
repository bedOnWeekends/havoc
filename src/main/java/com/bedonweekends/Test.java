package com.bedonweekends;


import com.google.gson.Gson;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        double S0 = 100.0;              // 초기 주가
        double mu = 0.05;              // 연간 드리프트 (평균 수익률)
        double sigma = 0.2  ;             // 연간 변동성
        int dayPerYear = 365 * 24 * 60 * 60;
        double dt = 1.0 / dayPerYear; // 시뮬레이션 간격
        int simulationDays = dayPerYear;

        double eventProbability = 0.01;
        double jumpVolatility = 0.1 / Math.sqrt(86400);

        Random random = new Random();
        double price = S0;
        List<Integer> seconds = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        String jsonPath = "/Users/sihoonyoo/IdeaProjects/havoc/src/main/resources/json/";
        String fileName = "주침컴퍼니_";
        Stock stock = new Stock("주침컴퍼니", new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        for (int second = 0; second < simulationDays; second++) {
            seconds.add(second);
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
            prices.add(price);
            StockPricePerTime stockPricePerTime = new StockPricePerTime(timestamp, price, jump);
            stock.getStockPricePerTimeList().add(stockPricePerTime);
            if (localDateTime.getHour() == 23 && localDateTime.getMinute() == 59 && localDateTime.getSecond() == 59) {
                Gson gson = new Gson();
                String json = gson.toJson(stock);
                if (json == null) {
                    continue;
                }
                if (!new File(jsonPath).exists()) {
                    new File(jsonPath).mkdir();
                }
                File file = new File(jsonPath + fileName + timestamp + ".json");
                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stock = new Stock("주침컴퍼니", new ArrayList<>());
            }
        }


    }
}

