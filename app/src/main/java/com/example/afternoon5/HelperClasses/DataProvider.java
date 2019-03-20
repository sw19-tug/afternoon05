package com.example.afternoon5.HelperClasses;

class DataProvider {
    private static final DataProvider ourInstance = new DataProvider();

    static DataProvider getInstance() {
        return ourInstance;
    }

    private DataProvider() {
    }
}
