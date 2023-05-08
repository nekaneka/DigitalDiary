package com.spba.ediary.intefaces;

import java.util.ArrayList;

public interface MultipleChoiceListener {
    void positiveButtonClicker(String[] list, ArrayList<String> selectedCategories);
    void negativeButtonClicker();
}
