package com.curtisnewbie;

import com.curtisnewbie.module.ioc.context.ContextFactory;

public class App {

    public static void main(String[] args) {
        ContextFactory.getContextInitializer().initialize(App.class);
    }
}
