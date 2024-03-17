package com.techacademy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.constants.ErrorKinds;
import com.techacademy.constants.ErrorMessage;
import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;

import com.techacademy.service.ReportService;

import com.techacademy.service.UserDetail;

@Controller
@RequestMapping("reports")
public class ReportsController {

    private final ReportService reportService;

    @Autowired
    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 日報一覧画面
    @GetMapping
    public String list(@AuthenticationPrincipal UserDetail userDetail,Model model) {

      if( userDetail.getEmployee().getRole() == Employee.Role.ADMIN ){
          model.addAttribute("listSize", reportService.findAll().size());
          model.addAttribute("reportList", reportService.findAll());
      }else{
          model.addAttribute("listSize", reportService.findByEmployee(userDetail.getEmployee()).size());
          model.addAttribute("reportList", reportService.findByEmployee(userDetail.getEmployee()));
      }
        return "reports/list";
    }

    // 日報詳細画面
    @GetMapping(value = "/{id}/")
    public String detail(@PathVariable Integer id, Model model) {

        model.addAttribute("report", reportService.findById(id));
        return "reports/detail";
    }

    // 日報新規登録画面
    @GetMapping(value = "/add")
    public String create(@ModelAttribute Report report,@AuthenticationPrincipal UserDetail userDetail) {
        report.setEmployee(userDetail.getEmployee());
        return "reports/new";
    }
    // 日報新規登録処理
    @PostMapping(value = "/add")
    public String add(@Validated Report report, BindingResult res, Model model,@AuthenticationPrincipal UserDetail userDetail) {

     // 入力チェック
        if (res.hasErrors()) {
            return create(report,userDetail);
        }

        // 論理削除を行った従業員番号を指定すると例外となるためtry~catchで対応
        // (findByIdでは削除フラグがTRUEのデータが取得出来ないため)
        try {
            report.setEmployee(userDetail.getEmployee());
            ErrorKinds result = reportService.save(report);

            if (ErrorMessage.contains(result)) {
                model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
                return create(report,userDetail);
            }

        } catch (DataIntegrityViolationException e) {
            model.addAttribute(ErrorMessage.getErrorName(ErrorKinds.DUPLICATE_DATE_ERROR),
                    ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_DATE_ERROR));
            return create(report,userDetail);
        }

        return "redirect:/reports";
    }
//
    //　日報削除処理
    @PostMapping(value = "/{id}/delete")
    public String delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetail userDetail, Model model) {

        ErrorKinds result = reportService.delete(id);

        if (ErrorMessage.contains(result)) {
            model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
            model.addAttribute("report", reportService.findById(id));
            return detail(id, model);
        }

        return "redirect:/reports";
    }

    // 日報更新画面
    @GetMapping(value = "/{id}/update")
    public String edit(@PathVariable Integer id,Report report,Model model) {
        if(id != null) {
            report =  reportService.findById(id);
        }
        model.addAttribute("report", report);

        return "reports/update";
    }

// // 日報更新登録処理
    @PostMapping(value = "/{id}/update")
    public String update(@PathVariable Integer id,@Validated Report report, BindingResult res, Model model) {

        // 入力チェック
         if (res.hasErrors()) {
             Report reportdate = reportService.findById(report.getId());
             report.setEmployee(reportdate.getEmployee());
             model.addAttribute("report", report);
             return "reports/update";
         }
         ErrorKinds result = reportService.update(report);

         if (ErrorMessage.contains(result)) {
              model.addAttribute(ErrorMessage.getErrorName(result), ErrorMessage.getErrorValue(result));
              ErrorMessage.getErrorValue(ErrorKinds.DUPLICATE_DATE_ERROR);

              return "reports/update";
          }
        return "redirect:/reports";
    }

}

