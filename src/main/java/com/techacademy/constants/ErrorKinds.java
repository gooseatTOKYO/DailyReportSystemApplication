package com.techacademy.constants;

// エラーメッセージ定義
public enum ErrorKinds {

    // エラー内容
    // 空白チェックエラー
    BLANK_ERROR,
    // 半角英数字チェックエラー
    HALFSIZE_ERROR,
    // 桁数(8桁~16桁以外)チェックエラー
    RANGECHECK_ERROR,
    // 重複チェックエラー(例外あり)
    DUPLICATE_EXCEPTION_ERROR,
    // 重複チェックエラー(例外なし)
    DUPLICATE_ERROR,
    // ログイン中削除チェックエラー
    LOGINCHECK_ERROR,
    // 日付チェックエラー
    DATECHECK_ERROR,


    //日付空白チェックエラー
    DATEBLANKCHECK_ERROR,
    //日付重複エラー
    DUPLICATE_DATE_ERROR,
    //タイトル空白チェックエラー
    TITLEBLANK_ERROR,
    //タイトル桁数チェックエラー
    TITLERANGECHECK_ERROR,
    //内容空白チェックエラー
    CONTENTBLANK_ERROR,
    //内容桁数チェックエラー
    CONTENTRANGE_ERROR,

    // チェックOK
    CHECK_OK,
    // 正常終了
    SUCCESS;

}
