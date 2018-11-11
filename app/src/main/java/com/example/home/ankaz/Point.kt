package com.example.home.ankaz

class Point {
    /// Model class
    var Point_Name : String? = null
    var Status_Name : String? = null
    var Serial_Number : String? = null
    var AssetModel_Name : String? = null
    var Address_Name : String? = null

    constructor(){

    }

    constructor(Point_Name: String?, Status_Name: String?, Serial_Number: String?,AssetModel_Name: String?,Address_Name: String?) {
        this.Point_Name = Point_Name
        this.Status_Name = Status_Name
        this.Serial_Number = Serial_Number
        this.AssetModel_Name = AssetModel_Name
        this.Address_Name = Address_Name
    }

}