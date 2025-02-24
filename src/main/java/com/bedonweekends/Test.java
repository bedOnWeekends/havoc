package com.bedonweekends;


import com.google.gson.Gson;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws IOException {
        double S0 = 100.0;              // 초기 주가
        double sigma = 2.0;             // 연간 변동성
        double mu = 0.5 * sigma * sigma;              // 연간 드리프트 (평균 수익률)
        int dayPerYear = 31 * 24 * 60 * 60;
        double dt = 1.0 / dayPerYear; // 시뮬레이션 간격
        int simulationDays = dayPerYear;

        double eventProbability = 0.1;
        double jumpVolatility = 5.0 / Math.sqrt(86400);

        Random random = new Random();
        double price = S0;
        String jsonPath = "/Users/sihoonyoo/json/";
        String chartPath = "/Users/sihoonyoo/chart/";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String companyName = br.readLine();
        String fileName = companyName + "_";
        Stock stock = new Stock(companyName, new HashMap<>());
        LocalDateTime now = LocalDateTime.now();
        List<Long> priceList = new ArrayList<>();
        for (int second = 0; second < simulationDays; second++) {
            LocalDateTime localDateTime = now.plusSeconds(second);
            String timestamp = localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            double epsilon = random.nextGaussian();
            double gbmFactor = (mu - 0.5 * sigma * sigma) * dt + sigma * Math.sqrt(dt) * epsilon;
            double newPrice = price * Math.exp(gbmFactor);
            if (random.nextDouble() < eventProbability) {
                double jumpMagnitude = random.nextGaussian() * jumpVolatility;
                double jumpFactor = Math.exp(jumpMagnitude);
                newPrice *= jumpFactor;
            }

            price = newPrice;
            priceList.add(Math.round(price));
            stock.getStockPricePerTimeMap().put(timestamp, Math.round(price));
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
                DefaultCategoryDataset dataset = new DefaultCategoryDataset();
                for (int i = 0; i < priceList.size(); i++) {
                    dataset.addValue(priceList.get(i), "price", String.valueOf(i));
                }
                JFreeChart chart = ChartFactory.createLineChart("Stock Price", "Time", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);
                File chartFile = new File(chartPath + fileName + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".png");
                try {
                    ChartUtilities.saveChartAsPNG(chartFile, chart, 800, 600);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                priceList.clear();
            }


        }

    }
}

