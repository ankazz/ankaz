package com.example.home.ankaz

class Deal {


    /// Model class
    var deal_num : String? = null
    var subject_name : String? = null
    var address_name : String? = null

    constructor(){

    }

    constructor(deal_num: String?, subject_name: String?, address_name: String?) {
        this.deal_num = deal_num
        this.subject_name = subject_name
        this.address_name = address_name
    }

}