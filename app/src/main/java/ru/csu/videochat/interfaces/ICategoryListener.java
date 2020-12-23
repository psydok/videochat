package ru.csu.videochat.interfaces;

import android.util.Pair;

import java.util.List;

public interface ICategoryListener {
    void showThemes(List<Pair<String, String>> themes);
}
