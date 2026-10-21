package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.Report;
import com.example.devs.domain.report.domain.ReportWeek;
import com.example.devs.domain.report.domain.TechTrend;
import com.example.devs.domain.report.domain.Trend;
import com.example.devs.domain.report.domain.WeeklyReportMail;
import com.example.devs.domain.report.domain.WeeklyReportStatistics.PopularTechStack;
import com.example.devs.domain.report.domain.WeeklyReportStatistics.TechMention;
import com.example.devs.domain.report.presentation.dto.response.ReportWeekRangeResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class WeeklyReportMailRenderer {
    private static final String TEMPLATE_PATH = "templates/mail/weekly-report.html";
    private static final String TEMPLATE = loadTemplate();
    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{(\\w+)}}");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private static final String SERVICE_URL = "https://kangyu.shop/";
    private static final int BAR_LIMIT = 5;
    private static final int RANK_LIMIT = 8;
    private static final int BAR_MAX_HEIGHT = 100;
    private static final int BAR_MIN_HEIGHT = 2;

    private static final String TREND_CHANGE = """
            <img src="cid:{{arrow}}" width="10" height="9" alt="" style="display: inline-block; width: 10px; height: 9px; border: 0; vertical-align: middle" />
            <span style="font-size: 14px; font-weight: 600">{{rate}}</span><span style="font-size: 12px">%</span>""";

    private static final String CHART_BAR = """
            <td class="chart-group" width="80" height="100" align="center" valign="bottom">
              <table role="presentation" cellpadding="0" cellspacing="0" border="0">
                <tr>
                  <td valign="bottom" style="padding-right: 5px">
                    <div style="width: 20px; height: {{previousHeight}}px; background-color: #343a40; border-radius: 2px 2px 0 0"></div>
                  </td>
                  <td valign="bottom" style="padding-left: 5px">
                    <div
                      class="bar-current"
                      style="width: 20px; height: {{currentHeight}}px; background-color: #aa5ef1; background-image: linear-gradient(225deg, #e1c4fb 2%, #ce9af7 15%, #aa5ef1 66%, #7726bf 115%); border-radius: 2px 2px 0 0"
                    ></div>
                  </td>
                </tr>
              </table>
            </td>
            """;

    private static final String CHART_LABEL = """
            <td class="chart-group" width="80" align="center">{{name}}</td>
            """;

    private static final String RANK_ROW = """
            <tr>
              <td height="24" style="{{paddingTop}}line-height: 1.5; white-space: nowrap">
                <span style="font-size: 12px; color: #adb5bd">{{rank}}</span>&nbsp;&nbsp;
                <span style="font-size: 14px; font-weight: 600; color: #ffffff">{{name}}</span>
              </td>
              <td height="24" align="right" style="{{paddingTop}}font-size: 12px; line-height: 1.5; white-space: nowrap">
                <span style="color: #adb5bd">{{count}}건</span>&nbsp;&nbsp;&nbsp;
                <img src="cid:{{arrow}}" width="10" height="9" alt="" style="display: inline-block; width: 10px; height: 9px; border: 0; vertical-align: middle" />
                <span style="color: {{color}}">{{diff}}</span>
              </td>
            </tr>
            """;

    private static final String EMPTY_CHART = """
            <td style="font-size: 12px; line-height: 1.5; color: #868e96">이번 주 언급량 데이터가 없어요.</td>
            """;

    private static final String EMPTY_CHART_LABEL = "<td></td>";

    private static final String EMPTY_RANK = """
            <tr>
              <td colspan="2" style="font-size: 12px; line-height: 1.5; color: #868e96">이번 주 데이터가 없어요.</td>
            </tr>
            """;

    private static final String EMPTY_SUMMARY = "이번 주 AI 요약을 준비하고 있어요.";

    public WeeklyReportMail render(Report report, String majorName, String email) {
        ReportWeekRangeResponse week = ReportWeekRangeResponse.from(ReportWeek.from(report.getWeekStartDate()));
        String title = "%d월 %d주차 %s 리포트".formatted(week.month(), week.week(), majorName);
        TechTrend rise = report.getMaxIncreaseTech();
        TechTrend fall = report.getMaxDecreaseTech();
        List<TechMention> mentions = firstItems(report.getTechMentions(), BAR_LIMIT);

        Map<String, String> values = new HashMap<>();
        values.put("preheader", escape(preheader(title, rise, fall)));
        values.put("reportYear", String.valueOf(week.year()));
        values.put("reportMonth", String.valueOf(week.month()));
        values.put("reportWeek", String.valueOf(week.week()));
        values.put("majorName", escape(majorName));
        values.put("startDate", week.startDate().format(DATE_FORMAT));
        values.put("endDate", week.endDate().format(DATE_FORMAT));
        values.put("topRiseName", rise == null ? "-" : escape(rise.skillName()));
        values.put("topRiseChange", rise == null ? "" : trendChange("arrow-up", "+" + Math.round(rise.changeRate())));
        values.put("topFallName", fall == null ? "-" : escape(fall.skillName()));
        values.put("topFallChange", fall == null ? "" : trendChange("arrow-down", String.valueOf(Math.round(fall.changeRate()))));
        values.put("chartBars", chartBars(mentions));
        values.put("chartLabels", chartLabels(mentions));
        values.put("rankRows", rankRows(report.getPopularTechStacks()));
        values.put("summaryBody", summaryBody(report.getLlmReport()));
        values.put("totalPostings", formatCount(report.getWeeklyCollectedPostingCount()));
        values.put("email", escape(email));

        return new WeeklyReportMail("[Devs] " + title, fill(TEMPLATE, values), plainText(title, week));
    }

    private String preheader(String title, TechTrend rise, TechTrend fall) {
        List<String> parts = new ArrayList<>();
        if (rise != null) {
            parts.add("최대 상승 %s +%d%%".formatted(rise.skillName(), Math.round(rise.changeRate())));
        }
        if (fall != null) {
            parts.add("최대 하락 %s %d%%".formatted(fall.skillName(), Math.round(fall.changeRate())));
        }
        return parts.isEmpty() ? title : "이번주 " + String.join(" · ", parts);
    }

    private String trendChange(String arrow, String rate) {
        return fill(TREND_CHANGE, Map.of("arrow", arrow, "rate", rate));
    }

    private String chartBars(List<TechMention> mentions) {
        if (mentions.isEmpty()) {
            return EMPTY_CHART;
        }
        long max = mentions.stream()
                .flatMapToLong(mention -> LongStream.of(mention.previous(), mention.current()))
                .max()
                .orElse(0);
        return mentions.stream()
                .map(mention -> fill(CHART_BAR, Map.of(
                        "previousHeight", String.valueOf(barHeight(mention.previous(), max)),
                        "currentHeight", String.valueOf(barHeight(mention.current(), max))
                )))
                .collect(Collectors.joining());
    }

    private long barHeight(long value, long max) {
        if (value <= 0 || max <= 0) {
            return 0;
        }
        return Math.max(BAR_MIN_HEIGHT, Math.round(value * (double) BAR_MAX_HEIGHT / max));
    }

    private String chartLabels(List<TechMention> mentions) {
        if (mentions.isEmpty()) {
            return EMPTY_CHART_LABEL;
        }
        return mentions.stream()
                .map(mention -> fill(CHART_LABEL, Map.of("name", escape(mention.name()))))
                .collect(Collectors.joining());
    }

    private String rankRows(List<PopularTechStack> stacks) {
        List<PopularTechStack> topStacks = stacks == null ? List.of() : stacks.stream()
                .sorted(Comparator.comparingInt(PopularTechStack::rank))
                .limit(RANK_LIMIT)
                .toList();
        if (topStacks.isEmpty()) {
            return EMPTY_RANK;
        }

        StringBuilder rows = new StringBuilder();
        for (int i = 0; i < topStacks.size(); i++) {
            PopularTechStack stack = topStacks.get(i);
            TrendMark mark = TrendMark.from(stack.trend());
            rows.append(fill(RANK_ROW, Map.of(
                    "paddingTop", i == 0 ? "" : "padding-top: 8px; ",
                    "rank", "%02d".formatted(stack.rank()),
                    "name", escape(stack.name()),
                    "count", formatCount(stack.searchCount()),
                    "arrow", mark.arrow(),
                    "color", mark.color(),
                    "diff", String.valueOf(Math.abs(stack.changeCount()))
            )));
        }
        return rows.toString();
    }

    private String summaryBody(String llmReport) {
        if (llmReport == null || llmReport.isBlank()) {
            return EMPTY_SUMMARY;
        }
        return escape(llmReport.strip())
                .replace("\r\n", "\n")
                .replace("\n", "<br />");
    }

    private String plainText(String title, ReportWeekRangeResponse week) {
        return """
                Devs %s
                %s ~ %s

                Devs에서 더 많은 정보 확인하기: %s
                """.formatted(
                title,
                week.startDate().format(DATE_FORMAT),
                week.endDate().format(DATE_FORMAT),
                SERVICE_URL
        );
    }

    private static <T> List<T> firstItems(List<T> items, int limit) {
        return items == null ? List.of() : items.stream().limit(limit).toList();
    }

    private static String formatCount(Long count) {
        return String.format(Locale.KOREA, "%,d", count == null ? 0 : count);
    }

    private static String escape(String value) {
        return value == null ? "" : HtmlUtils.htmlEscape(value, StandardCharsets.UTF_8.name());
    }

    private static String fill(String template, Map<String, String> values) {
        return PLACEHOLDER.matcher(template).replaceAll(match -> {
            String value = values.get(match.group(1));
            if (value == null) {
                throw new IllegalStateException("메일 템플릿 값이 없습니다: " + match.group(1));
            }
            return Matcher.quoteReplacement(value);
        });
    }

    private static String loadTemplate() {
        try {
            return StreamUtils.copyToString(
                    new ClassPathResource(TEMPLATE_PATH).getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private record TrendMark(String arrow, String color) {
        private static TrendMark from(Trend trend) {
            if (trend == null) {
                return new TrendMark("arrow-same", "#adb5bd");
            }
            return switch (trend) {
                case UP -> new TrendMark("arrow-up", "#ff7272");
                case DOWN -> new TrendMark("arrow-down", "#62caf4");
                case SAME -> new TrendMark("arrow-same", "#adb5bd");
            };
        }
    }
}
