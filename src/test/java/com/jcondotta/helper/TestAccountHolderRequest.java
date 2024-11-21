package com.jcondotta.helper;

public enum TestAccountHolderRequest {

    JEFFERSON("Jefferson Condotta"),
    VIRGINIO("Virginio Condotta"),
    PATRIZIO("Patrizio Condotta");

    private final String accountHolderName;

    TestAccountHolderRequest(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}