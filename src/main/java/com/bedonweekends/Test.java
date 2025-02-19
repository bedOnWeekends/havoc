package com.bedonweekends;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    public static void main(String[] args) {
        // 초기 설정
        double S0 = 100.0;              // 초기 주가
        double mu = 0.05;               // 연간 드리프트 (평균 수익률)
        double sigma = 0.2;             // 연간 변동성
        int secondPerYear = 365 * 24 * 60 * 60;   // 연간 거래일 수
        double dt = 1.0 / secondPerYear; // 하루 단위 시간 간격
        int simulationDays = secondPerYear; // 5년간 시뮬레이션

        // 이벤트 발생 확률 (예를 들어 하루에 1% 확률)
        double eventProbability = 0.1;
        // 이벤트 발생 시 적용할 점프 분포의 크기 (평균 0, 표준편차 0.1로 가정, 즉 약 ±10% 충격)
        double jumpVolatility = 0.1;

        Random random = new Random();
        double price = S0;
        List<Integer> days = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        for (int day = 0; day < simulationDays; day++) {
            days.add(day);
            // 표준 GBM 업데이트
            double epsilon = random.nextGaussian();
            double gbmFactor = (mu - 0.5 * sigma * sigma) * dt + sigma * Math.sqrt(dt) * epsilon;
            double newPrice = price * Math.exp(gbmFactor);

            // 이벤트 기반 가격 점프 적용
            if (random.nextDouble() < eventProbability) {
                double jumpMagnitude = random.nextGaussian() * jumpVolatility;
                // 점프 요인은 exp(jumpMagnitude)로 계산 (양수면 상승, 음수면 하락)
                double jumpFactor = Math.exp(jumpMagnitude);
                newPrice *= jumpFactor;
            }

            price = newPrice;
            prices.add(price);
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < days.size(); i++) {
            dataset.addValue(prices.get(i), "price", days.get(i));
        }

        JFreeChart chart = ChartFactory.createLineChart("Stock Simulation", "Days", "Price", dataset, PlotOrientation.VERTICAL, true, true, false);
        try {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String fileName = "output_" + now + ".png";
            ChartUtilities.saveChartAsPNG(new File(fileName), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

