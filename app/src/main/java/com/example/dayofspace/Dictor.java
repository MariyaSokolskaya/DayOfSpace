package com.example.dayofspace;

import androidx.annotation.NonNull;

class Dictor {
    String ShortName;
    String Gender;
    String Locale;

    @NonNull
    @Override
    public String toString() {
        return "ShortName: " + ShortName + "\nGender: " + Gender +
                "\nLocale: " + Locale + "\n\n";
    }
}
