package com.example.home.ankaz

import android.location.Address

class DealBalances {
    /// Model class
    var Period_Name : String? = null
    var StartBalance_Value : String? = null
    var Consumption_Value : String? = null
    var Debit_Value : String? = null
    var Credit_Value : String? = null
    var EndBalance_Value : String? = null

    constructor(){

    }

    constructor(Period_Name: String?, StartBalance_Value: String?, Consumption_Value: String?,Debit_Value: String?,Credit_Value: String?,EndBalance_Value: String?) {
        this.Period_Name = Period_Name
        this.StartBalance_Value = StartBalance_Value
        this.Consumption_Value = Consumption_Value
        this.Debit_Value = Debit_Value
        this.Credit_Value = Credit_Value
        this.EndBalance_Value = EndBalance_Value
    }

}