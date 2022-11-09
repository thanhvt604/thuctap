package com.globits.da.support;

public enum StatusMessage {

    PROVINCE_IS_NULL(1, "Province is null !"),
    PROVINCE_CODE_IS_NULL(2, "Province code can't be null !"),
    ID_PROVINCE_IS_NULL(7, "province id is null  !"),
    PROVINCE_ID_NOT_EXISTS(21, "id province not exists"),
    PROVINCE_CODE_IS_EXIST(22, "province code is exists !"),
    PROVINCE_LIST_IS_EMPTY(51,"province list is empty !"),
    PROVINCE_NAME_IS_EXISTS(65,"province name is exists !"),

    DISTRICT_LIST_IS_NULL(50,"District list is null !"),
    DISTRICT_IS_NULL(8, "District is null !"),
    DISTRICT_CODE_IS_NULL(9, "District code can't be null !"),
    DISTRICT_CODE_IS_EXIST(24, "District code is exists !"),
    DISTRICT_ID_NOT_EXISTS(29, "District id not exists !"),
    DISTRICT_OUT_OF_PROVINCE(38, "District out of province !"),
    DISTRICT_LIST_IS_EMPTY(55,"District list is empty !"),
    DISTRICT_ID_IS_NULL(56,"District id is null !"),
    DISTRICT_NAME_IS_EXIST(66,"District name is exist"),

    COMMUNE_IS_NULL(11, "Commune is null !"),
    COMMUNE_CODE_IS_NULL(12, "Commune code can't be null !"),
    COMMUNE_ID_IS_NOT_EXIST(25, "Commune id is not exists ! "),
    COMMUNE_CODE_IS_EXIST(26, "Commune code is exist"),
    COMMUNE_NAME_IS_NULL(27, "Commune name is null !"),
    COMMUNE_OUT_OF_DISTRICT(39, "Commune out of district !"),
    COMMUNE_LIST_IS_EMPTY(51,"Commune list is empty !"),
    COMMUNE_ID_IS_NULL(63,"Commune id is null !"),
    COMMUNE_NAME_IS_EXISTS(66,"Commune name is exists"),

    CERTIFICATE_IS_NULL(14, "Certificate is null !"),
    CERTIFICATE_CODE_IS_NULL(15, "Certificate code can't be null !"),
    EFFECTIVE_DATE_IS_NULL(17, "effective date is null or blank !"),
    EXPIRATION_DATE_IS_NULL(18, "expiration date is null or blank !"),
    WRONG_POSITION_FOR_DATE(19, "effective date must before expiration date "),
    EXPIRATION_IS_BEFORE_NOW(41, "expiration date must be after now !"),
    CERTIFICATE_ID_IS_NULL(44, "Certificate id is null !"),
    CERTIFICATE_ID_NOT_EXISTS(45, "Certificate id not exists !"),
    CERTIFICATE_LIST_IS_EMPTY(52,"Certificate list is empty !"),
    CERTIFICATE_CODE_IS_EXIST(61 ,"Certificate code is exist"),

    NAME_IS_NULL(4, "Name is null"),
    ID_NOT_EXISTS(6, " Id is not exists !"),


    EMPLOYEE_NAME_IS_NULL(30, "Employee name is null !"),
    EMPLOYEE_CODE_IS_NULL(31, "Employee code is null !"),
    EMPLOYEE_EMAIL_IS_NULL(32, "Employee email is null !"),
    EMPLOYEE_AGE_IS_NULL(33, "Employee age is null !"),
    AGE_CAN_NOT_NEGATIVE(40, "Age can't negative !"),
    EMPLOYEE_CODE_IS_EXISTS(34, "Employee code is exists !"),
    EMPLOYEE_IS_NULL(35, "Employee is null !"),
    EMPLOYEE_PHONE_NUMBER_IS_NULL(37, "Phone number is null ! "),
    EMPLOYEE_ID_IS_NULL(42, "Employee id is null !"),
    EMPLOYEE_ID_NOT_EXISTS(43, "Employee id not exists !"),
    EMPLOYEE_LIST_IS_EMPTY(53,"Employee list is empty !"),

    WRONG_EMAIL_FORMAT(36, "Wrong email format !"),
    WRONG_PHONE_NUMBER_FORMAT(38, "Wrong phone number format !"),
    WRONG_DATE_FORMAT(56,"Wrong date format !"),
    WRONG_CODE_FORMAT(58,"Wrong code format !"),

    EMPLOYEE_HAS_CERTIFICATE_NOT_EXIST(60,"Employee has certificate not exist !"),
    EMPLOYEE_HAS_CERTIFICATE_OF_PROVINCE(46, "Employee has certificate of province !"),
    EMPLOYEE_HAS_THREE_CERTIFICATE_SAME_TYPE(47, "Employee has three certificate same type !"),
    CAN_NOT_GENERATE_EXCEL_FILE(48, "Can't generate excel file !"),
    CAN_N0T_ACCESS_EXCEL_FILE(49, "Can't access excel file !"),
    NOT_EMPLOYEE_HAS_CERTIFICATE(54,"Not employee has certificate"),

    SUCCESS(200, "Success!");

    private final int code;
    private final String message;

    StatusMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
