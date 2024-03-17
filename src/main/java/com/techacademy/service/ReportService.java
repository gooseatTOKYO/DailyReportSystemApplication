package com.techacademy.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    // 日報保存
    @Transactional
    public ErrorKinds save(Report report) {

      //日報テーブルに、「ログイン中の従業員 かつ 入力した日付」の日報データが1件でも存在する場合はエラー、0件なら登録
        List<Report> reportList = reportRepository.findByReportDateAndEmployee(report.getReportDate(), report.getEmployee());
        int reportListCount = reportList.size();

            if (reportListCount >= 1) {
                return ErrorKinds.DUPLICATE_DATE_ERROR;
                }

            report.setDeleteFlg(false);

            LocalDateTime now = LocalDateTime.now();
            report.setCreatedAt(now);
            report.setUpdatedAt(now);

            reportRepository.save(report);
            return ErrorKinds.SUCCESS;
    }

    // 日報一覧表示処理
    public List<Report> findAll() {
        return reportRepository.findAll();
    }

    // 1件を検索
    public Report findById(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    //日報テーブルから従業員に紐づいた日報を取得する
     public List<Report> findByEmployee(Employee employee) {
         return reportRepository.findByEmployee(employee);
    }

    //更新
    @Transactional
    public ErrorKinds update(Report report) {
        Report existReport = findById(report.getId());

        //
        if(existReport.getReportDate().equals(report.getReportDate())) {

        } else {
            List<Report> reportList = reportRepository.findByReportDateAndEmployee(report.getReportDate(), report.getEmployee());
            int reportListCount = reportList.size();
                if (reportListCount >= 1) {
                    return ErrorKinds.DUPLICATE_DATE_ERROR;
                    }
        }


        report.setDeleteFlg(false);

        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);//更新日時
        report.setCreatedAt(existReport.getCreatedAt());//テーブル内容の登録日時の更新

        reportRepository.save(report);
        return ErrorKinds.SUCCESS;
    }

    // 日報削除
    @Transactional
    public ErrorKinds delete(Integer id) {
        Report report = findById(id);
        LocalDateTime now = LocalDateTime.now();
        report.setUpdatedAt(now);
        report.setDeleteFlg(true);

        return ErrorKinds.SUCCESS;
    }
}
