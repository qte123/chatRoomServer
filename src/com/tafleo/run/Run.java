package com.tafleo.run;

import com.tafleo.common.ServerBase;
import com.tafleo.dao.BaseDao;
import com.tafleo.view.menu.Console;

import java.io.*;

public class Run extends BaseDao {
    public Run() {
    }
    public void start() {
        new Console();
    }

    public static void main(String[] args) {
        Run run = new Run();
        run.start();
    }
}
